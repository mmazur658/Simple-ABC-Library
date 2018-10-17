package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Controller
@Scope("session")
@RequestMapping("/book")
public class BookController {

	@Autowired
	BookService bookService;

	@Autowired
	UserService userService;

	@Autowired
	PdfService pdfService;

	@Autowired
	AccessLevelControl accessLevelControl;

	@Autowired
	ReservationService reservationService;

	@Autowired
	SearchEngineUtils searchEngineUtils;

	@RequestMapping("/main-bookstore")
	public String mainBookstore(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(required = false, name = "id") String bookId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "startResult") Integer startResult) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "bookSeachParamTitle", "bookSeachParamAuthor", "bookSeachParamPublisher",
				"bookSeachParamIsbn", "bookSeachParamId" };
		String[] searchParametersValue = { title, author, publisher, isbn, bookId };

		String[] searchBookParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(searchBookParameters);

		if (startResult == null)
			startResult = (Integer) session.getAttribute("startResult");
		if (startResult == null)
			startResult = 0;
		session.setAttribute("startResult", startResult);

		List<Book> booksList = hasAnyParameters ? bookService.bookSearchResult(searchBookParameters, startResult)
				: bookService.getAllBooks(startResult);
		long amountOfResults = hasAnyParameters ? bookService.getAmountOfSearchResult(searchBookParameters)
				: bookService.getAmountOfAllBooks();

		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(startResult, amountOfResults, 10);
		String resultRange = searchEngineUtils.generateResultRange(startResult, amountOfResults, showMoreLinkValue, 10);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(startResult, 10);

		List<Reservation> userReservationList = reservationService
				.getUserReservations((int) session.getAttribute("userId"));

		session.setAttribute("userReservationsCount", userReservationList.size());
		theModel.addAttribute("startResult", startResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("startResult", startResult);
		theModel.addAttribute("booksList", booksList);
		theModel.addAttribute("userAccessLevel", session.getAttribute("userAccessLevel"));
		theModel.addAttribute("systemMessage", systemMessage);

		return "main-bookstore";

	}

	@RequestMapping("/clearSearchParameters")
	public String clearSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParameters = { "bookSeachParamTitle", "bookSeachParamId", "bookSeachParamAuthor",
				"bookSeachParamPublisher", "bookSeachParamIsbn", "bookSeachParamIsAvailable" };
		searchEngineUtils.clearSearchParameters(session, searchParameters);

		return "redirect:/book/main-bookstore";
	}

	@RequestMapping("/add-book-form")
	public String addBookForm(Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Book tempBook = new Book();
		theModel.addAttribute("book", tempBook);

		return "add-book-form";
	}

	@PostMapping("/saveBook")
	public String saveBook(@ModelAttribute("book") Book tempBook, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.saveBook(tempBook);

		redirectAttributes.addAttribute("bookId", tempBook.getId());

		return "redirect:/book/confirm-book-page";
	}

	@RequestMapping("/confirm-book-page")
	public String confirmBookPage(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Book tempBook = bookService.getBook(bookId);

		theModel.addAttribute("tempBook", tempBook);

		return "confirm-book-page";

	}

	@RequestMapping("/reservation")
	public String bookReservation(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		int userReservationsCount = (int) session.getAttribute("userReservationsCount");
		if (userReservationsCount >= 3)
			redirectAttributes.addAttribute("systemErrorMessage", "Nie mo¿na zarezerwowaæ wiêcej jak 3 ksi¹¿ki");
		else {
			int userId = (int) session.getAttribute("userId");
			Book tempBook = bookService.getBook(bookId);

			if (tempBook.getIsAvailable()) {
				reservationService.createReservation(tempBook, userId);
				redirectAttributes.addAttribute("systemSuccessMessage", "Rezerwacja zosta³a dodana.");
				redirectAttributes.addAttribute("bookId", bookId);
			} else {
				redirectAttributes.addAttribute("systemErrorMessage", "Nie mo¿na zarezerwowaæ niedostêpnej ksi¹¿ki.");
				redirectAttributes.addAttribute("bookId", bookId);
			}
		}
		return "redirect:/book/book-details";
	}

	@RequestMapping("/deleteReservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId,
			@RequestParam(required = false, name = "deleteReservationWayBack") String deleteReservationWayBack,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Reservation reservation = reservationService.getReservation(reservationId);
		reservationService.deleteReservationByUser(reservation);
		redirectAttributes.addAttribute("systemSuccessMessage", "Rezerwacja zosta³a usuniêta");

		if (deleteReservationWayBack.equals("main-bookstore"))
			return "redirect:/book/main-bookstore";
		else
			return "redirect:/user/users-books";
	}

	@RequestMapping("/updateBook")
	public String updateBookForm(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Book tempBook = bookService.getBook(bookId);
		theModel.addAttribute("book", tempBook);

		return "book-update";
	}

	@PostMapping("/update-book")
	public String updateBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		try {
			bookService.updateBook(book);
			redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a pomyœlnie zaktualizowana!");
		} catch (Exception exc) {
			exc.printStackTrace();
			redirectAttributes.addAttribute("systemMessage", "B³¹d aktualizacji ksi¹¿ki!");
		}

		return "redirect:/book/main-bookstore";
	}

	@RequestMapping("/deleteBook")
	public String deleteBook(@RequestParam("bookId") int bookId, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Book tempBook = bookService.getBook(bookId);
		if (tempBook.getIsAvailable()) {
			bookService.deleteBook(tempBook);
			redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a usuniêta.");
		} else {
			redirectAttributes.addAttribute("systemMessage",
					"Nie mo¿na usun¹æ wypo¿yczonej / zarezerwowanej ksi¹¿ki !!");
		}

		return "redirect:/book/main-bookstore";
	}

	@RequestMapping("/book-details")
	public String bookDetails(@RequestParam(required = false, name = "bookId") Integer bookId, Model theModel,
			HttpServletRequest request,
			@RequestParam(required = false, name = "systemErrorMessage") String systemErrorMessage,
			@RequestParam(required = false, name = "systemSuccessMessage") String systemSuccessMessage) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (bookId != null)
			session.setAttribute("bookId", bookId);
		if (bookId == null)
			bookId = (Integer) session.getAttribute("bookId");

		Book tempBook = bookService.getBook(bookId);

		theModel.addAttribute("tempBook", tempBook);
		theModel.addAttribute("systemErrorMessage", systemErrorMessage);
		theModel.addAttribute("systemSuccessMessage", systemSuccessMessage);

		return "book-details";
	}

	@RequestMapping("/generate-book-label")
	public ResponseEntity<InputStreamResource> generateAndDownloadBookLabel(@RequestParam("bookId") int bookId,
			HttpServletRequest request) throws FileNotFoundException {

		HttpSession session = request.getSession();

		String userName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		Book tempBook = bookService.getBook(bookId);
		File tempFile = pdfService.getBookLabel(tempBook, userName);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
	}

}
