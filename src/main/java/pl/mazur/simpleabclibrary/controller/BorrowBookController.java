package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Controller
@Scope("session")
@RequestMapping("/borrow-book")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:library-configuration.properties")
public class BorrowBookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Autowired
	private Environment env;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private AccessLevelControl accessLevelControl;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private SearchEngineUtils searchEngineUtils;

	@RequestMapping("/borrow-book-choose-user")
	public String borrowBook(
			@RequestParam(required = false, name = "borrowBookSelectedUserId") String borrowBookSelectedUserId,
			@RequestParam(required = false, name = "borrowBookFirstName") String borrowBookFirstName,
			@RequestParam(required = false, name = "borrowBookLastName") String borrowBookLastName,
			@RequestParam(required = false, name = "borrowBookEmail") String borrowBookEmail,
			@RequestParam(required = false, name = "borrowBookPesel") String borrowBookPesel,
			@RequestParam(required = false, name = "borrowBookStartResult") Integer borrowBookStartResult,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "borrowBookSelectedUserId", "borrowBookFirstName", "borrowBookLastName",
				"borrowBookEmail", "borrowBookPesel" };
		String[] searchParametersValue = { borrowBookSelectedUserId, borrowBookFirstName, borrowBookLastName,
				borrowBookEmail, borrowBookPesel };

		String[] userSearchParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(userSearchParameters);

		if (borrowBookStartResult == null)
			borrowBookStartResult = (Integer) session.getAttribute("borrowBookStartResult");
		if (borrowBookStartResult == null)
			borrowBookStartResult = 0;
		session.setAttribute("borrowBookStartResult", borrowBookStartResult);

		List<User> usersList = hasAnyParameters
				? userService.getUserSearchResult(userSearchParameters, borrowBookStartResult)
				: userService.getAllUsers(borrowBookStartResult);
		long amountOfResults = hasAnyParameters ? userService.getAmountOfSearchResult(userSearchParameters)
				: userService.getAmountOfAllUsers();

		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(borrowBookStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(borrowBookStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(borrowBookStartResult, searchResultLimit);
		List<Book> tempBookList = new ArrayList<>();

		session.setAttribute("borrowBookStartResult", borrowBookStartResult);
		session.setAttribute("tempBookList", tempBookList);
		theModel.addAttribute("borrowBookStartResult", borrowBookStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("usersList", usersList);

		return "borrow-book-choose-user";
	}

	@RequestMapping("/clearUserSearchParameters")
	public String clearUserSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "borrowBookStartResult", "borrowBookSelectedUserId", "borrowBookFirstName",
				"borrowBookLastName", "borrowBookEmail", "borrowBookPesel" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/borrow-book/borrow-book-choose-user";
	}

	@RequestMapping("/borrow-book-choose-books")
	public String borrowBookChooseBooks(@RequestParam(required = false, name = "selectedUserId") String selectedUserId,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "errorMessage") String errorMessage,
			@RequestParam(required = false, name = "bookId") String bookId,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "isAbleToBorrow") boolean isAbleToBorrow,
			@RequestParam(required = false, name = "extraMessage") String extraMessage,
			@RequestParam(required = false, name = "borrowBookChooseBookStartResult") Integer borrowBookChooseBookStartResult,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		session.setAttribute("borrowBookStartResult", null);

		if (borrowBookChooseBookStartResult == null)
			borrowBookChooseBookStartResult = (Integer) session.getAttribute("borrowBookChooseBookStartResult");
		if (borrowBookChooseBookStartResult == null)
			borrowBookChooseBookStartResult = 0;
		session.setAttribute("borrowBookStartResult", borrowBookChooseBookStartResult);

		int theUserId;
		if (selectedUserId == null)
			theUserId = Integer.valueOf((String) session.getAttribute("selectedUserId"));
		else {
			session.setAttribute("selectedUserId", selectedUserId);
			theUserId = Integer.valueOf(selectedUserId);
		}

		User user = userService.getUser(theUserId);
		List<BorrowedBook> borrowedBookList = bookService.getUserBorrowedBookList(user.getId());
		@SuppressWarnings("unchecked")
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		List<Reservation> tempReservationList = reservationService.getUserReservations(theUserId);

		int borrowedBookLimit = Integer.valueOf(env.getProperty("borrowed.book.limit"));
		isAbleToBorrow = true;

		if (borrowedBookList.size() + tempBookList.size() >= borrowedBookLimit) {
			isAbleToBorrow = false;
			extraMessage = env.getProperty("controller.BorrowBookController.borrowBookChooseBooks.error.1");
		} else
			extraMessage = env.getProperty("controller.BorrowBookController.borrowBookChooseBooks.success.1"
					+ (borrowedBookLimit - borrowedBookList.size() - tempBookList.size()));

		if (isAbleToBorrow) {
			for (BorrowedBook borrowedBook : borrowedBookList) {
				Long currentTimeMillis = System.currentTimeMillis();
				Long expTimeMillis = borrowedBook.getExpectedEndDate().getTime();
				if (currentTimeMillis > expTimeMillis) {
					isAbleToBorrow = false;
					extraMessage = env.getProperty("controller.BorrowBookController.borrowBookChooseBooks.error.2");
					break;
				}
			}
		}

		String[] searchParametersName = { "borrowBookSeachParamTitle", "borrowBookSeachParamId",
				"borrowBookSeachParamAuthor", "borrowBookSeachParamIsbn", "borrowBookSeachParamPublisher" };
		String[] searchParametersValue = { title, bookId, author, isbn, publisher };
		String[] searchBookParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(searchBookParameters);

		List<Book> booksList = hasAnyParameters
				? bookService.bookSearchResult(searchBookParameters, borrowBookChooseBookStartResult)
				: bookService.getAllBooks(borrowBookChooseBookStartResult);
		long amountOfResults = hasAnyParameters ? bookService.getAmountOfSearchResult(searchBookParameters)
				: bookService.getAmountOfAllBooks();

		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(borrowBookChooseBookStartResult,
				amountOfResults, searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(borrowBookChooseBookStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(borrowBookChooseBookStartResult,
				searchResultLimit);

		theModel.addAttribute("borrowBookChooseBookStartResult", borrowBookChooseBookStartResult);
		session.setAttribute("borrowBookChooseBookStartResult", borrowBookChooseBookStartResult);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("isAbleToBorrow", isAbleToBorrow);
		theModel.addAttribute("borrowedBookList", borrowedBookList);
		theModel.addAttribute("userReservationList", tempReservationList);
		theModel.addAttribute("tempBookList", tempBookList);
		theModel.addAttribute("bookList", booksList);
		theModel.addAttribute("theUser", user);
		theModel.addAttribute("systemMessage", systemMessage);
		theModel.addAttribute("extraMessage", extraMessage);
		theModel.addAttribute("errorMessage", errorMessage);

		return "borrow-book-choose-books";
	}

	@RequestMapping("/clearBookSearchParameters")
	public String clearBookSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "borrowBookChooseBookStartResult", "title", "id", "author" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	@RequestMapping("/addBookToList")
	public String addBookToList(@RequestParam("bookId") int bookId,
			@RequestParam("isAbleToBorrow") boolean isAbleToBorrow, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (isAbleToBorrow) {
			String errorMessage = bookService.addBookToList(session, bookId);
			redirectAttributes.addAttribute("errorMessage", errorMessage);
		} else
			redirectAttributes.addAttribute("errorMessage",
					env.getProperty("controller.BorrowBookController.addBookToList.error.1"));

		return "redirect:/borrow-book/borrow-book-choose-books";

	}

	@RequestMapping("/addReservedBookToList")
	public String addReservedBookToList(@RequestParam("reservationId") int reservationId,
			@RequestParam("isAbleToBorrow") boolean isAbleToBorrow, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (isAbleToBorrow) {
			String errorMessage = bookService.addReservedBookToList(session, reservationId);
			redirectAttributes.addAttribute("errorMessage", errorMessage);
		} else
			redirectAttributes.addAttribute("errorMessage",
					env.getProperty("controller.BorrowBookController.addReservedBookToList.error.1"));

		return "redirect:/borrow-book/borrow-book-choose-books";

	}

	@RequestMapping("/deleteBookFromList")
	public String deleteBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.deleteBookFromList(session, bookId);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty("controller.BorrowBookController.deleteBookFromList.success.1"));

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	@RequestMapping("/borrow-books")
	public String borrowBooks(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		@SuppressWarnings("unchecked")
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		if (tempBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty("controller.BorrowBookController.borrowBooks.error.1"));
			return "redirect:/borrow-book/borrow-book-choose-books";
		}

		String borrowedBookInfo = bookService.borrowBooks(session);

		theModel.addAttribute("borrowedBookInfo", borrowedBookInfo);
		session.setAttribute("isUserAbleToBorrow", false);
		session.setAttribute("tempBookList", null);

		return "borrow-book-confirmation";

	}

	@RequestMapping("/generate-borrowed-book-confirmation")
	public ResponseEntity<InputStreamResource> generateBorrowedBookConfirmation(
			@RequestParam("borrowedBookInfo") String borrowedBookInfo, HttpServletRequest request)
			throws FileNotFoundException {

		HttpSession session = request.getSession();

		int bookId;
		StringTokenizer st = new StringTokenizer(borrowedBookInfo);
		List<Book> bookList = new ArrayList<>();
		User tempUser = userService.getUser(Integer.valueOf(st.nextToken()));

		while (st.hasMoreTokens()) {
			bookId = Integer.parseInt(st.nextToken());
			bookList.add(bookService.getBook(bookId));
		}

		Date expectedEndDate = bookService.getExpectedEndDate(tempUser, bookList.get(0));
		String employeeName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		File tempFile = pdfService.generateBorrowedBookConfirmation(bookList, tempUser, expectedEndDate, employeeName);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
	}

	@RequestMapping("/cancel-borrowed-book")
	public String cancelBorrowedBook(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		session.setAttribute("isUserAbleToBorrow", false);

		String[] searchParametersName = { "borrowBookSelectedUserId", "borrowBookFirstName", "borrowBookLastName",
				"borrowBookEmail", "borrowBookPesel", "borrowBookStartResult", "borrowBookStartResult", "tempBookList",
				"borrowBookChooseBookStartResult", "title", "id", "author" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/user/main";
	}
}