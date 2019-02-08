package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"borrow-book-choose-user"</li>
 * <li>"borrow-book-choose-books"</li>
 * <li>"borrow-book-confirmation"</li>
 * </ul>
 * 
 * <br>
 * <br>
 * 
 * This controller also perform the actions on the books.
 * 
 * @author Marcin Mazur
 *
 */
@Controller
@Scope("session")
@RequestMapping("/borrow-book")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class BorrowBookController {

	/**
	 * The array containing the names of book search parameters
	 */
	private final String[] NAMES_OF_BOOK_SEARCH_PARAMETERS = { "borrowBookSelectedUserId", "borrowBookFirstName",
			"borrowBookLastName", "borrowBookEmail", "borrowBookPesel" };

	/**
	 * The array containing the names of borrowed book search parameters
	 */
	private final String[] NAMES_OF_BORROW_BOOK_SEARCH_PARAMETERS = { "borrowBookSeachParamTitle",
			"borrowBookSeachParamId", "borrowBookSeachParamAuthor", "borrowBookSeachParamIsbn",
			"borrowBookSeachParamPublisher" };

	/**
	 * The array containing the names of session attributes to clean
	 */
	private final String[] NAMES_OF_SESSION_ATTRIBUTES_TO_CLEAN = { "borrowBookSelectedUserId", "borrowBookFirstName",
			"borrowBookLastName", "borrowBookEmail", "borrowBookPesel", "borrowBookStartResult",
			"borrowBookStartResult", "tempBookList", "borrowBookChooseBookStartResult", "title", "id", "author" };

	/**
	 * The BookService interface
	 */
	private BookService bookService;

	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The PdfService interface
	 */
	private PdfService pdfService;

	/**
	 * The AccessLevelControl interface
	 */
	private AccessLevelControl accessLevelControl;

	/**
	 * The ReservationService interface
	 */
	private ReservationService reservationService;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * Constructs a BorrowBookController with the BookService, UserService,
	 * Environment, PdfService, AccessLevelControl, ReservationService and
	 * SearchEngineUtils.
	 * 
	 * @param bookService
	 *            The BookService interface
	 * @param userService
	 *            The UserService interface
	 * @param env
	 *            The Environment interface
	 * @param pdfService
	 *            The PdfService interface
	 * @param accessLevelControl
	 *            The AccessLevelControl interface
	 * @param reservationService
	 *            The ReservationService interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 */
	@Autowired
	public BorrowBookController(BookService bookService, UserService userService, Environment env,
			PdfService pdfService, AccessLevelControl accessLevelControl, ReservationService reservationService,
			SearchEngineUtils searchEngineUtils) {

		this.bookService = bookService;
		this.userService = userService;
		this.env = env;
		this.pdfService = pdfService;
		this.accessLevelControl = accessLevelControl;
		this.reservationService = reservationService;
		this.searchEngineUtils = searchEngineUtils;

	}

	/**
	 * Returns the view of "borrow-book-choose-user" with model attributes:<br>
	 * <ul>
	 * <li>borrowBookStartResult - The value of first index of the results</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>usersList - The list of User objects</li>
	 * </ul>
	 * 
	 * @param borrowBookSelectedUserId
	 *            The String containing the id of the user
	 * @param borrowBookFirstName
	 *            The String containing the first name of the user
	 * @param borrowBookLastName
	 *            The String containing the last name of the user
	 * @param borrowBookEmail
	 *            The String containing the email of the user
	 * @param borrowBookPesel
	 *            The String containing the PESEL of the user
	 * @param borrowBookStartResult
	 *            The Integer containing the first index of the results
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/borrow-book-choose-user")
	public String showBorrowBookForm(
			@RequestParam(required = false, name = "borrowBookSelectedUserId") String borrowBookSelectedUserId,
			@RequestParam(required = false, name = "borrowBookFirstName") String borrowBookFirstName,
			@RequestParam(required = false, name = "borrowBookLastName") String borrowBookLastName,
			@RequestParam(required = false, name = "borrowBookEmail") String borrowBookEmail,
			@RequestParam(required = false, name = "borrowBookPesel") String borrowBookPesel,
			@RequestParam(required = false, name = "borrowBookStartResult") Integer borrowBookStartResult,
			Model theModel, HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// The Arrays containing the values of search parameters
		String[] searchParametersValue = { borrowBookSelectedUserId, borrowBookFirstName, borrowBookLastName,
				borrowBookEmail, borrowBookPesel };

		// The Array containing the search parameters ready to search
		String[] userSearchParameters = searchEngineUtils.prepareTableToSearch(session, NAMES_OF_BOOK_SEARCH_PARAMETERS,
				searchParametersValue);

		// Get the borrowBookStartResult from the session. If session doesn't contain
		// that attribute set default value
		if (borrowBookStartResult == null)
			borrowBookStartResult = (Integer) session.getAttribute("borrowBookStartResult");
		if (borrowBookStartResult == null)
			borrowBookStartResult = 0;
		session.setAttribute("borrowBookStartResult", borrowBookStartResult);

		// Check whether the userSearchParameters array contains any parameters and get
		// the results and number of the results.
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(userSearchParameters);
		List<User> usersList = hasAnyParameters
				? userService.getListOfUserByGivenSearchParams(userSearchParameters, borrowBookStartResult)
				: userService.getListOfAllUsers(borrowBookStartResult);
		long amountOfResults = hasAnyParameters ? userService.getNumberOfUsersForGivenSearchParams(userSearchParameters)
				: userService.getNumberOfAllUsers();

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(borrowBookStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(borrowBookStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(borrowBookStartResult, searchResultLimit);

		// Create an empty list of Books
		List<Book> tempBookList = new ArrayList<>();

		// Set model and session attributes
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

	/**
	 * Cleans the search parameters and redirects to the view of
	 * "borrow-book-choose-user".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/clearUserSearchParameters")
	public String clearUserSearchParameters(HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clean search parameters
		String[] searchParametersName = { "borrowBookStartResult", "borrowBookSelectedUserId", "borrowBookFirstName",
				"borrowBookLastName", "borrowBookEmail", "borrowBookPesel" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/borrow-book/borrow-book-choose-user";
	}

	/**
	 * Returns the view of "borrow-book-choose-books" with model attributes:<br>
	 * <ul>
	 * <li>borrowBookChooseBookStartResult - The value of first index of the
	 * results</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>isAbleToBorrow - The user status determines whether the user is able to
	 * borrow more books</li>
	 * <li>borrowedBookList - The list of the borrowed books</li>
	 * <li>userReservationList - The list of user's reservations</li>
	 * <li>tempBookList - The temporary list of the books</li>
	 * <li>bookList - The list of the books</li>
	 * <li>theUser - The User object</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * <li>extraMessage - The one of the system messages</li>
	 * <li>errorMessage - TThe one of the system messages</li>
	 * </ul>
	 * 
	 * 
	 * @param selectedUserId
	 *            The String containing the id of the user
	 * @param title
	 *            The String containing the title of the book
	 * @param errorMessage
	 *            The String containing the system message
	 * @param bookId
	 *            The String containing the id of the book
	 * @param author
	 *            The String containing the author of the book
	 * @param publisher
	 *            The String containing the publisher of the book
	 * @param isbn
	 *            The String containing the ISBN of the book
	 * @param systemMessage
	 *            The String containing the system message
	 * @param isAbleToBorrow
	 *            The boolean containing the status determines whether the user is
	 *            able to borrow more books
	 * @param extraMessage
	 *            The String containing the system message
	 * @param borrowBookChooseBookStartResult
	 *            The Integer containing the first index of the results
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/borrow-book-choose-books")
	public String showBorrowBookChooseBooksForm(
			@RequestParam(required = false, name = "selectedUserId") String selectedUserId,
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
			Model theModel, HttpServletRequest request, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Set borrowBookStartResult to null
		session.setAttribute("borrowBookStartResult", null);

		// Get the borrowBookChooseBookStartResult from the session. If session doesn't
		// contain that attribute set default value
		if (borrowBookChooseBookStartResult == null)
			borrowBookChooseBookStartResult = (Integer) session.getAttribute("borrowBookChooseBookStartResult");
		if (borrowBookChooseBookStartResult == null)
			borrowBookChooseBookStartResult = 0;
		session.setAttribute("borrowBookStartResult", borrowBookChooseBookStartResult);

		// If the requested param selectedUserId is null, get the id from the session
		int theUserId;
		if (selectedUserId == null)
			theUserId = Integer.valueOf((String) session.getAttribute("selectedUserId"));
		else {
			session.setAttribute("selectedUserId", selectedUserId);
			theUserId = Integer.valueOf(selectedUserId);
		}

		// Get the user with given id
		User user = userService.getUserById(theUserId);

		// Get the list of BorrowedBook, Book and Reservation for given user id
		List<BorrowedBook> borrowedBookList = bookService.getListOfBorrowedBooksByUserId(user.getId());
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		List<Reservation> tempReservationList = reservationService.getListOfReservationByUserId(theUserId);

		// Get the limit of borrowed books
		int borrowedBookLimit = Integer.valueOf(env.getProperty("borrowed.book.limit"));

		// Check whether the user is able to borrow more books
		isAbleToBorrow = true;
		if (borrowedBookList.size() + tempBookList.size() >= borrowedBookLimit) {
			isAbleToBorrow = false;
			extraMessage = env.getProperty(
					locale.getLanguage() + ".controller.BorrowBookController.borrowBookChooseBooks.error.1");
		} else
			extraMessage = env.getProperty(
					locale.getLanguage() + ".controller.BorrowBookController.borrowBookChooseBooks.success.1"
							+ (borrowedBookLimit - borrowedBookList.size() - tempBookList.size()));

		// Check whether the user has expired book
		if (isAbleToBorrow) {
			for (BorrowedBook borrowedBook : borrowedBookList) {
				Long currentTimeMillis = System.currentTimeMillis();
				Long expTimeMillis = borrowedBook.getExpectedEndDate().getTime();
				if (currentTimeMillis > expTimeMillis) {
					isAbleToBorrow = false;
					extraMessage = env.getProperty(
							locale.getLanguage() + ".controller.BorrowBookController.borrowBookChooseBooks.error.2");
					break;
				}
			}
		}

		// The Arrays containing the values of search parameters
		String[] searchParametersValue = { title, bookId, author, isbn, publisher };

		// The Array containing the search parameters ready to search
		String[] searchBookParameters = searchEngineUtils.prepareTableToSearch(session,
				NAMES_OF_BORROW_BOOK_SEARCH_PARAMETERS, searchParametersValue);

		// Check whether the searchBookParameters array contains any parameters and get
		// the results and number of the results.
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(searchBookParameters);
		List<Book> booksList = hasAnyParameters
				? bookService.getListOfBooksForGivenSearchParams(searchBookParameters, borrowBookChooseBookStartResult)
				: bookService.getListOfAllBooks(borrowBookChooseBookStartResult);
		long amountOfResults = hasAnyParameters ? bookService.getNumberOfBooksForGivenSearchParams(searchBookParameters)
				: bookService.getNumberOfAllBooks();

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(borrowBookChooseBookStartResult,
				amountOfResults, searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(borrowBookChooseBookStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(borrowBookChooseBookStartResult,
				searchResultLimit);

		// Get the list of the reservation
		session.setAttribute("borrowBookChooseBookStartResult", borrowBookChooseBookStartResult);
		theModel.addAttribute("borrowBookChooseBookStartResult", borrowBookChooseBookStartResult);
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

	/**
	 * Cleans the search parameters and redirects to the view of
	 * "borrow-book-choose-books".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/clearBookSearchParameters")
	public String clearBookSearchParameters(HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clear search parameters
		String[] searchParametersName = { "borrowBookChooseBookStartResult", "title", "id", "author" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	/**
	 * Adds the book with given id to the list and redirects to the view of
	 * "borrow-book-choose-books" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * <li>errorMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The int containing the id of the boos
	 * @param isAbleToBorrow
	 *            The boolean containing the status determines whether the user is
	 *            able to borrow more books
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/addBookToList")
	public String addBookToList(@RequestParam("bookId") int bookId,
			@RequestParam("isAbleToBorrow") boolean isAbleToBorrow, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Add the book with given id to the list if the user is able to borrow
		if (isAbleToBorrow) {
			String systemMessage = bookService.addBookToList(session, bookId, locale);
			redirectAttributes.addAttribute("systemMessage", systemMessage);
		} else
			redirectAttributes.addAttribute("errorMessage",
					env.getProperty(locale.getLanguage() + ".controller.BorrowBookController.addBookToList.error.1"));

		return "redirect:/borrow-book/borrow-book-choose-books";

	}

	/**
	 * Adds the reserved book to the list and redirects to the view of
	 * "borrow-book-choose-books" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * <li>errorMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 * @param isAbleToBorrow
	 *            The boolean containing the status determines whether the user is
	 *            able to borrow more books
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/addReservedBookToList")
	public String addReservedBookToList(@RequestParam("reservationId") int reservationId,
			@RequestParam("isAbleToBorrow") boolean isAbleToBorrow, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Add the reserved book to the list if user is able to borrow
		if (isAbleToBorrow) {
			String errorMessage = bookService.addReservedBookToList(session, reservationId, locale);
			redirectAttributes.addAttribute("errorMessage", errorMessage);
		} else
			redirectAttributes.addAttribute("errorMessage", env.getProperty(
					locale.getLanguage() + ".controller.BorrowBookController.addReservedBookToList.error.1"));

		return "redirect:/borrow-book/borrow-book-choose-books";

	}

	/**
	 * Deletes the book from the list and redirects to the view of
	 * "borrow-book-choose-books" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The int containing the id of the book
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/deleteBookFromList")
	public String deleteBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Remove the book from the list
		bookService.deleteBookFromList(session, bookId);
		redirectAttributes.addAttribute("systemMessage", env
				.getProperty(locale.getLanguage() + ".controller.BorrowBookController.deleteBookFromList.success.1"));

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	/**
	 * Returns the view of "borrow-book-confirmation" with model attributes:<br>
	 * <ul>
	 * <li>borrowedBookInfo - The information displayed on the web page</li>
	 * <li>systemMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/borrow-books")
	public String borrowBooks(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel,
			Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the list of Books from the session
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		// Check the size of the list. If it smaller then 1 return the system error
		// message
		if (tempBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.BorrowBookController.borrowBooks.error.1"));
			return "redirect:/borrow-book/borrow-book-choose-books";
		}
		String borrowedBookInfo = bookService.borrowBooks(session);

		// Get the list of the reservation
		theModel.addAttribute("systemMessage",
				env.getProperty(locale.getLanguage() + ".controller.BorrowBookController.borrowBooks.success.1"));
		theModel.addAttribute("borrowedBookInfo", borrowedBookInfo);
		session.setAttribute("isUserAbleToBorrow", false);
		session.setAttribute("tempBookList", null);

		return "borrow-book-confirmation";

	}

	/**
	 * Returns the confirmation as a PDF File
	 * 
	 * @param borrowedBookInfo
	 *            The String containing the information about borrowed books
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 * @throws FileNotFoundException
	 *             A FileNotFoundException is thrown then the file can not be found.
	 */
	@RequestMapping("/generate-borrowed-book-confirmation")
	public ResponseEntity<InputStreamResource> generateBorrowedBookConfirmation(
			@RequestParam("borrowedBookInfo") String borrowedBookInfo, HttpServletRequest request)
			throws FileNotFoundException {

		HttpSession session = request.getSession();

		int bookId;
		StringTokenizer st = new StringTokenizer(borrowedBookInfo);
		List<Book> bookList = new ArrayList<>();
		User tempUser = userService.getUserById(Integer.valueOf(st.nextToken()));

		while (st.hasMoreTokens()) {
			bookId = Integer.parseInt(st.nextToken());
			bookList.add(bookService.getBookById(bookId));
		}

		Date expectedEndDate = bookService.getExpectedEndDate(tempUser, bookList.get(0));
		String employeeName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		File tempFile = pdfService.generateConfirmationForBorrowedBook(bookList, tempUser, expectedEndDate,
				employeeName);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Clears search parameters and session attribute "isUserAbleToBorrow" and
	 * redirects to the view of "main"
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/cancel-borrowed-book")
	public String cancelBorrowedBook(HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Set session attribute isUserAbleToBorrow to FALSE
		session.setAttribute("isUserAbleToBorrow", false);

		// Clear search parameters
		searchEngineUtils.clearSearchParameters(session, NAMES_OF_SESSION_ATTRIBUTES_TO_CLEAN);

		return "redirect:/user/main";
	}
}