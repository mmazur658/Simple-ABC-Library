package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import pl.mazur.simpleabclibrary.entity.BookBorrowing;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.ForbiddenWordsImpl;
import pl.mazur.simpleabclibrary.utils.LoginAndAccessLevelCheck;

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
	LoginAndAccessLevelCheck loginAndAccessLevelCheck;

	@Autowired
	ReservationService reservationService;

	@Autowired
	ForbiddenWords forbiddenWords;
	
	@RequestMapping("/main-bookstore")
	public String mainBookstore(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "id") String id,
			@RequestParam(required = false, name = "startResult") Integer startResult) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		if (!(title == null))
			session.setAttribute("title", title);
		if (!(id == null))
			session.setAttribute("id", id);
		if (!(author == null))
			session.setAttribute("author", author);
		if (!(publisher == null))
			session.setAttribute("publisher", publisher);
		if (!(isbn == null))
			session.setAttribute("isbn", isbn);

		if ((title == null) && !(session.getAttribute("title") == null))
			title = (String) session.getAttribute("title");
		if ((id == null) && !(session.getAttribute("id") == null))
			id = (String) session.getAttribute("id");
		if ((author == null) && !(session.getAttribute("author") == null))
			author = (String) session.getAttribute("author");
		if ((publisher == null) && !(session.getAttribute("publisher") == null))
			publisher = (String) session.getAttribute("publisher");
		if ((isbn == null) && !(session.getAttribute("isbn") == null))
			isbn = (String) session.getAttribute("isbn");

		String[] searchParameters = { "", "", "", "", "", "" };
		searchParameters[0] = (title == null) ? "" : title.trim();
		searchParameters[1] = (author == null) ? "" : author.trim();
		searchParameters[2] = (publisher == null) ? "" : publisher.trim();
		searchParameters[3] = (isbn == null) ? "" : isbn.trim();
		searchParameters[4] = "";
		searchParameters[5] = (id == null) ? "" : id.trim();
		
		for (String word:searchParameters) {
			if(forbiddenWords.findForbiddenWords(word)) {
				return "redirect:/error";
			}
		}

		List<Book> booksList;
		long amountOfResults;

		if (startResult == null)
			startResult = (Integer) session.getAttribute("startResult");
		if (startResult == null)
			startResult = 0;

		theModel.addAttribute("startResult", startResult);
		session.setAttribute("startResult", startResult);

		if (!searchParameters[0].equals("") || !searchParameters[1].equals("") || !searchParameters[2].equals("")
				|| !searchParameters[3].equals("") || !searchParameters[4].equals("")
				|| !searchParameters[5].equals("")) {
			amountOfResults = bookService.getAmountOfSearchResult(searchParameters);
			booksList = bookService.bookSearchResult(searchParameters, startResult);
		} else {
			amountOfResults = bookService.getAmountOfAllBooks();
			booksList = bookService.getAllBooks(startResult);
		}

		String resultRange;
		long showMoreLinkValue = 0;
		if ((startResult + 10) > amountOfResults) {
			showMoreLinkValue = startResult;
			resultRange = "Wyniki od " + (startResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = startResult + 10;
			resultRange = "Wyniki od " + (startResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((startResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = startResult - 10;
		}

		int userId = (int) session.getAttribute("userId");
		List<Reservation> userReservationList = reservationService.getUserReservations(userId);
		List<BookBorrowing> userBookBorrowingList = bookService.getUserBookBorrowing(userId);

		session.setAttribute("userReservationsCount", userReservationList.size());
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("startResult", startResult);
		theModel.addAttribute("userReservationList", userReservationList);
		theModel.addAttribute("userBookBorrowingList", userBookBorrowingList);
		theModel.addAttribute("booksList", booksList);
		theModel.addAttribute("userAccessLevel", session.getAttribute("userAccessLevel"));
		theModel.addAttribute("systemMessage", systemMessage);

		return "main-bookstore";

	}

	@RequestMapping("/clearSearchParameters")
	public String clearSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		session.setAttribute("title", null);
		session.setAttribute("id", null);
		session.setAttribute("author", null);
		session.setAttribute("publisher", null);
		session.setAttribute("isbn", null);
		session.setAttribute("isAvailable", null);

		return "redirect:/book/main-bookstore";
	}

	@RequestMapping("/add-book-form")
	public String addBookForm(Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		Book tempBook = new Book();
		theModel.addAttribute("book", tempBook);

		return "add-book-form";
	}

	@PostMapping("/saveBook")
	public String saveBook(@ModelAttribute("book") Book tempBook, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		bookService.saveBook(tempBook);

		redirectAttributes.addAttribute("bookId", tempBook.getId());

		return "redirect:/book/confirm-book-page";
	}

	@RequestMapping("/confirm-book-page")
	public String confirmBookPage(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		Book tempBook = bookService.getBook(bookId);

		theModel.addAttribute("tempBook", tempBook);

		return "confirm-book-page";

	}

	@RequestMapping("/reservation")
	public String bookReservation(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		int userReservationsCount = (int) session.getAttribute("userReservationsCount");
		if (userReservationsCount >= 3) {
			redirectAttributes.addAttribute("systemMessage", "Nie mo¿na zarezerwowaæ wiêcej jak 3 ksi¹¿ki");
		} else {
			int userId = (int) session.getAttribute("userId");
			Book tempBook = bookService.getBook(bookId);

			if (tempBook.getIsAvailable()) {

				Reservation reservation = new Reservation();
				reservation.setBook(tempBook);
				reservation.setUser(userService.getUser(userId));
				reservation.setIsActive(true);

				reservation.setStartDate(new Date());

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(reservation.getStartDate());
				calendar.add(Calendar.DATE, 2);
				reservation.setEndDate(calendar.getTime());

				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

				String date = sdf.format(calendar.getTime());

				reservation.setStatus("Rezerwacja wa¿na do " + date);
				redirectAttributes.addAttribute("systemMessage", "Rezerwacja zosta³a dodana.");

				reservationService.createReservation(reservation);

			} else {
				redirectAttributes.addAttribute("systemMessage", "Nie mo¿na zarezerwowaæ niedostêpnej ksi¹¿ki . . . ");
			}
		}

		return "redirect:/book/main-bookstore";
	}

	@RequestMapping("/deleteReservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId,
			@RequestParam(required = false, name = "deleteReservationWayBack") String deleteReservationWayBack,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		Reservation reservation = reservationService.getReservation(reservationId);
		reservationService.deleteReservationByUser(reservation);

		redirectAttributes.addAttribute("systemMessage", "Rezerwacja zosta³a usuniêta");

		if (deleteReservationWayBack.equals("main-bookstore"))
			return "redirect:/book/main-bookstore";
		else
			return "redirect:/user/user-details";

	}

	@RequestMapping("/updateBook")
	public String updateBookForm(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		Book tempBook = bookService.getBook(bookId);
		theModel.addAttribute("book", tempBook);

		return "book-update";
	}

	@PostMapping("/update-book")
	public String updateBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

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
	public String deleteBook(@RequestParam("bookId") int bookId,

			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

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
	public String bookDetails(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		Book tempBook = bookService.getBook(bookId);
		theModel.addAttribute("tempBook", tempBook);
		
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
