package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.BadElementException;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"main-bookstore"</li>
 * <li>"add-book-form"</li>
 * <li>"confirm-book-page"</li>
 * <li>"book-update book-details"</li>
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
@RequestMapping("/book")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class BookController {

	/**
	 * The names of the search parameters
	 */
	private final String[] NAMES_OF_SEARCH_PARAMETERS = { "bookSearchParamTitle", "bookSearchParamAuthor",
			"bookSearchParamPublisher", "bookSearchParamIsbn", "bookSearchParamId" };

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The BookService interface
	 */
	private BookService bookService;

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
	 * Constructs a BookController with the Environment, BookService, PdfService,
	 * AccessLevelControl, ReservationService and SearchEngineUtils.
	 * 
	 * @param env
	 *            The Environment interface
	 * @param bookService
	 *            The BookService interface
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
	public BookController(Environment env, BookService bookService, PdfService pdfService,
			AccessLevelControl accessLevelControl, ReservationService reservationService,
			SearchEngineUtils searchEngineUtils) {

		this.env = env;
		this.bookService = bookService;
		this.pdfService = pdfService;
		this.accessLevelControl = accessLevelControl;
		this.reservationService = reservationService;
		this.searchEngineUtils = searchEngineUtils;

	}

	/**
	 * Returns the view of "main-bookstore" with model attributes:<br>
	 * <ul>
	 * <li>userReservationsCount - The number of user reservations</li>
	 * <li>startResult - The value of first index of the results</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>showMoreLinkValue - The value of the "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>showLessLinkValue - The value of the "showLessLink"</li>
	 * <li>booksList - The list of the books</li>
	 * <li>userAccessLevel - The access level of the user</li>
	 * <li>systemMessage - The system message</li>
	 * </ul>
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param title
	 *            The String containing the title
	 * @param author
	 *            The String containing the author
	 * @param publisher
	 *            The String containing the publisher
	 * @param isbn
	 *            The String containing the ISBN
	 * @param bookId
	 *            The String containing the id of the book
	 * @param systemMessage
	 *            The String containing the system message
	 * @param startResult
	 *            The Integer containing the value of first index of the results
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/main-bookstore")
	public String showMainBookstore(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(required = false, name = "id") String bookId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "startResult") Integer startResult) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// The Array containing the search parameters
		String[] searchParametersValue = { title, author, publisher, isbn, bookId };

		// The Array containing the search parameters ready to search
		String[] searchBookParameters = searchEngineUtils.prepareTableToSearch(session, NAMES_OF_SEARCH_PARAMETERS,
				searchParametersValue);

		// Get the startResult from the session. If session doesn't contain that
		// attribute set default value
		if (startResult == null)
			startResult = (Integer) session.getAttribute("startResult");
		if (startResult == null)
			startResult = 0;
		session.setAttribute("startResult", startResult);

		// Check whether the searchBookParameters array contains any parameters and get
		// the results and number of the results.
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(searchBookParameters);
		List<Book> booksList = hasAnyParameters
				? bookService.getListOfBooksForGivenSearchParams(searchBookParameters, startResult)
				: bookService.getListOfAllBooks(startResult);
		long amountOfResults = hasAnyParameters
				? bookService.getNumberOfBooksForGivenSearchParams(searchBookParameters)
				: bookService.getNumberOfAllBooks();

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(startResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(startResult, amountOfResults, showMoreLinkValue,
				searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(startResult, searchResultLimit);

		// Get the list of the reservation
		List<Reservation> userReservationList = reservationService
				.getListOfReservationByUserId((int) session.getAttribute("userId"));

		// Set model and session attributes
		session.setAttribute("userReservationsCount", userReservationList.size());
		theModel.addAttribute("startResult", startResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("booksList", booksList);
		theModel.addAttribute("userAccessLevel", session.getAttribute("userAccessLevel"));
		theModel.addAttribute("systemMessage", systemMessage);

		return "main-bookstore";

	}

	/**
	 * Clears search parameters and redirects to the view of "main-bookstore".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/clearSearchParameters")
	public String clearSearchParameters(HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clean search parameters
		searchEngineUtils.clearSearchParameters(session, NAMES_OF_SEARCH_PARAMETERS);

		return "redirect:/book/main-bookstore";
	}

	/**
	 * Returns the view of "add-book-form" with model attributes:<br>
	 * <ul>
	 * <li>book - The Book object</li>
	 * </ul>
	 * 
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/add-book-form")
	public String showBookForm(Model theModel, HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Create a new blank book object
		Book tempBook = new Book();

		// Set model and session attributes
		theModel.addAttribute("book", tempBook);

		return "add-book-form";
	}

	/**
	 * Saves the given book and redirects to the view of "" with redirect
	 * attributes:<br>
	 * <ul>
	 * <li>bookId- The id of the book</li>
	 * </ul>
	 * 
	 * @param tempBook
	 *            The Book containing the book object
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@PostMapping("/saveBook")
	public String saveBook(@ModelAttribute("book") Book tempBook, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Save the book
		bookService.saveBook(tempBook);

		// Set model and session attributes
		redirectAttributes.addAttribute("bookId", tempBook.getId());

		return "redirect:/book/confirm-book-page";
	}

	/**
	 * Returns the view of "confirm-book-page" with model attributes:<br>
	 * <ul>
	 * <li>successMessage - The system message</li>
	 * <li>tempBook - The Book object</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The int containing the id of the book
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/confirm-book-page")
	public String showConfirmationPage(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request,
			Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the book with given id
		Book tempBook = bookService.getBookById(bookId);

		// Set model and session attributes
		theModel.addAttribute("successMessage",
				env.getProperty(locale.getLanguage() + ".controller.BookController.confirmBookPage.success.1"));
		theModel.addAttribute("tempBook", tempBook);

		return "confirm-book-page";

	}

	/**
	 * Creates a Reservation and redirects to the view of "book-details" with
	 * redirect attributes:<br>
	 * <ul>
	 * <li>systemErrorMessage - The System message</li>
	 * <li>systemSuccessMessage - The System message</li>
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
	@RequestMapping("/reservation")
	public String bookReservation(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the value of reservation limit and the number of user reservations
		int reservationLimit = Integer.valueOf(env.getProperty("reservation.limit"));
		int userReservationsCount = (int) session.getAttribute("userReservationsCount");

		// If the number of user reservations is higher then limit return a message,
		// otherwise create a reservation.
		if (userReservationsCount >= reservationLimit)
			redirectAttributes.addAttribute("systemErrorMessage",
					env.getProperty(locale.getLanguage() + ".controller.BookController.bookReservation.error.1"));
		else {
			int userId = (int) session.getAttribute("userId");
			Book tempBook = bookService.getBookById(bookId);

			if (tempBook.getIsAvailable()) {
				reservationService.createReservation(tempBook, userId);
				redirectAttributes.addAttribute("systemSuccessMessage",
						env.getProperty(locale.getLanguage() + ".controller.BookController.bookReservation.success.1"));
				redirectAttributes.addAttribute("bookId", bookId);
			} else {
				redirectAttributes.addAttribute("systemErrorMessage",
						env.getProperty(locale.getLanguage() + ".controller.BookController.bookReservation.error.2"));
				redirectAttributes.addAttribute("bookId", bookId);
			}
		}
		return "redirect:/book/book-details";
	}

	/**
	 * Deletes the reservation and redirects to the view of "-" with redirect
	 * attributes:<br>
	 * <ul>
	 * <li>systemSuccessMessage- The system message</li>
	 * </ul>
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 * @param deleteReservationWayBack
	 *            The String containing the name of the returned view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/deleteReservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId,
			@RequestParam(required = false, name = "deleteReservationWayBack") String deleteReservationWayBack,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the reservation with given id then delete it
		Reservation reservation = reservationService.getReservationById(reservationId);
		reservationService.deleteReservationByUser(reservation);
		redirectAttributes.addAttribute("systemSuccessMessage",
				env.getProperty(locale.getLanguage() + ".controller.BookController.deleteReservation.success.1"));

		if (deleteReservationWayBack.equals("main-bookstore"))
			return "redirect:/book/main-bookstore";
		else
			return "redirect:/user/users-books";
	}

	/**
	 * Returns the view of "book-update" with model attributes:<br>
	 * <ul>
	 * <li>book - The Book object</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The int containing the id of the book
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/updateBook")
	public String showUpdateBookForm(@RequestParam("bookId") int bookId, Model theModel, HttpServletRequest request,
			Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the book with given id
		Book tempBook = bookService.getBookById(bookId);

		// Set model and session attributes
		theModel.addAttribute("book", tempBook);

		return "book-update";
	}

	/**
	 * Updates the given book and redirects to the view of "main-bookstore" with
	 * redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The system message</li>
	 * </ul>
	 * 
	 * @param book
	 *            The Book containing the Book object
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@PostMapping("/update-book")
	public String updateBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes,
			HttpServletRequest request, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Update the book
		try {
			bookService.updateBook(book);
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.BookController.updateBook.success.1"));
		} catch (Exception exc) {
			exc.printStackTrace();
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.BookController.updateBook.error.1"));
		}

		return "redirect:/book/main-bookstore";
	}

	/**
	 * Deletes the book with given id and redirects to the view of "main-bookstore"
	 * with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The system message</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The int containing id of the book
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/deleteBook")
	public String deleteBook(@RequestParam("bookId") int bookId, RedirectAttributes redirectAttributes,
			HttpServletRequest request, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Delete the book if the IsAvailable status of the book is equal to TRUE,
		// otherwise return the error message
		Book tempBook = bookService.getBookById(bookId);
		if (tempBook.getIsAvailable()) {
			bookService.deleteBook(tempBook);
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.BookController.deleteBook.success.1"));
		} else {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.BookController.deleteBook.error.1"));
		}

		return "redirect:/book/main-bookstore";
	}

	/**
	 * Returns the view of "book-details" with model attributes:<br>
	 * <ul>
	 * <li>tempBook - The Book object</li>
	 * <li>systemErrorMessage - The system error message</li>
	 * <li>systemSuccessMessage - The system success message</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The Integer containing the id of the book
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @param systemErrorMessage
	 *            The String containing the system message
	 * @param systemSuccessMessage
	 *            The String containing the system message
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/book-details")
	public String showBookDetails(@RequestParam(required = false, name = "bookId") Integer bookId, Model theModel,
			HttpServletRequest request, Locale locale,
			@RequestParam(required = false, name = "systemErrorMessage") String systemErrorMessage,
			@RequestParam(required = false, name = "systemSuccessMessage") String systemSuccessMessage) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// If the requested param bookId is not null set it to the session attribute,
		// otherwise get it form the session
		if (bookId != null)
			session.setAttribute("bookId", bookId);
		if (bookId == null)
			bookId = (Integer) session.getAttribute("bookId");

		Book tempBook = bookService.getBookById(bookId);

		// Set model and session attributes
		theModel.addAttribute("tempBook", tempBook);
		theModel.addAttribute("systemErrorMessage", systemErrorMessage);
		theModel.addAttribute("systemSuccessMessage", systemSuccessMessage);

		return "book-details";
	}

	/**
	 * Returns the label of the book as a PDF File
	 * 
	 * @param bookId
	 *            The int containing the id of the book
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return A File representing the label of the book
	 * @throws IOException
	 *             A IOException is thrown when the file can't be created
	 * @throws MalformedURLException
	 *             A MalformedURLException is thrown when URL is incorrect
	 * @throws BadElementException
	 *             A BadElementException is thrown when created element has
	 *             incorrect form.
	 */
	@RequestMapping("/generate-book-label")
	public ResponseEntity<InputStreamResource> generateAndDownloadBookLabel(@RequestParam("bookId") int bookId,
			HttpServletRequest request) throws BadElementException, MalformedURLException, IOException {

		HttpSession session = request.getSession();

		String userName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		Book tempBook = bookService.getBookById(bookId);
		File tempFile = pdfService.getBookLabel(tempBook, userName);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
	}

}
