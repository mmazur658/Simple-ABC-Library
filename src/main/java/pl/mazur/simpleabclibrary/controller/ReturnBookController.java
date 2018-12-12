package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"return-book-choose-user"</li>
 * <li>"return-book-choose-books"</li>
 * <li>"return-book-confirmation"</li>
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
@RequestMapping("/return-book")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class ReturnBookController {

	/**
	 * The BookService interface
	 */
	private BookService bookService;

	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * The PdfService interface
	 */
	private PdfService pdfService;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The AccessLevelControl interface
	 */
	private AccessLevelControl accessLevelControl;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * Constructs a ReturnBookController with the BookService, UserService,
	 * PdfService, Environment, AccessLevelControl and SearchEngineUtils.
	 * 
	 * @param bookService
	 *            The BookService interface
	 * @param userService
	 *            The UserService interface
	 * @param pdfService
	 *            The PdfService interface
	 * @param env
	 *            The Environment interface
	 * @param accessLevelControl
	 *            The AccessLevelControl interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 */
	@Autowired
	public ReturnBookController(BookService bookService, UserService userService, PdfService pdfService,
			Environment env, AccessLevelControl accessLevelControl, SearchEngineUtils searchEngineUtils) {

		this.bookService = bookService;
		this.userService = userService;
		this.pdfService = pdfService;
		this.env = env;
		this.accessLevelControl = accessLevelControl;
		this.searchEngineUtils = searchEngineUtils;

	}

	/**
	 * Returns the view of "return-book-choose-user" with model attributes:<br>
	 * <ul>
	 * <li>returnBookStartResult - The value of first index of the results</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>usersList -</li>
	 * </ul>
	 * 
	 * @param locale
	 *            The Locale containing the user's locale
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param returnBookSelectedUserId
	 *            The String containing the id of the user
	 * @param returnBookFirstName
	 *            The String containing the first name of the user
	 * @param returnBookLastName
	 *            The String containing the last name of the user
	 * @param returnBookEmail
	 *            The String containing the email of the user
	 * @param returnBookPesel
	 *            The String containing the PESEL of the user
	 * @param returnBookStartResult
	 *            The Integer containing the first index of the results
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/return-book-choose-user")
	public String showBorrowBookForm(Locale locale, Model theModel, HttpServletRequest request,
			@RequestParam(required = false, name = "returnBookSelectedUserId") String returnBookSelectedUserId,
			@RequestParam(required = false, name = "returnBookFirstName") String returnBookFirstName,
			@RequestParam(required = false, name = "returnBookLastName") String returnBookLastName,
			@RequestParam(required = false, name = "returnBookEmail") String returnBookEmail,
			@RequestParam(required = false, name = "returnBookPesel") String returnBookPesel,
			@RequestParam(required = false, name = "returnBookStartResult") Integer returnBookStartResult) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// The Arrays containing the names and values of search parameters
		String[] searchParametersName = { "returnBookSelectedUserId", "returnBookFirstName", "returnBookLastName",
				"returnBookEmail", "returnBookPesel" };
		String[] searchParametersValue = { returnBookSelectedUserId, returnBookFirstName, returnBookLastName,
				returnBookEmail, returnBookPesel };

		// The Array containing the search parameters ready to search
		String[] userSearchParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);

		// Get the returnBookStartResult from the session. If session doesn't contain
		// that
		// attribute set default value
		if (returnBookStartResult == null)
			returnBookStartResult = (Integer) session.getAttribute("returnBookStartResult");
		if (returnBookStartResult == null)
			returnBookStartResult = 0;
		session.setAttribute("returnBookStartResult", returnBookStartResult);

		// Check whether the userSearchParameters array contains any parameters and get
		// the results and number of the results.
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(userSearchParameters);
		List<User> usersList = hasAnyParameters
				? userService.getListOfUserByGivenSearchParams(userSearchParameters, returnBookStartResult)
				: userService.getListOfAllUsers(returnBookStartResult);
		long amountOfResults = usersList.size();

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(returnBookStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(returnBookStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(returnBookStartResult, searchResultLimit);

		// Get the list of the reservation
		session.setAttribute("returnBookStartResult", returnBookStartResult);
		theModel.addAttribute("returnBookStartResult", returnBookStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("usersList", usersList);

		return "return-book-choose-user";
	}

	/**
	 * Cleans the search parameters and redirects to the view of
	 * "return-book-choose-user".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/clearReturnBookUserSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clear search parameters
		String[] searchParametersName = { "returnBookStartResult", "returnBookSelectedUserId", "returnBookFirstName",
				"returnBookLastName", "returnBookEmail", "returnBookPesel" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/return-book/return-book-choose-user";
	}

	/**
	 * Prepares lists to return books and redirects to the view of
	 * "return-book-choose-books".
	 * 
	 * @param selectedUserId
	 *            The int containing the id of the user
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/prepareForReturn")
	public String returnBookPreparation(@RequestParam(required = false, name = "userId") int selectedUserId,
			HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the list of BorrowedBook for given user id
		List<BorrowedBook> userBorrowedBooksList = bookService.getListOfBorrowedBooksByUserId(selectedUserId);

		// Create an empty list of Books
		List<Book> tempReturnedBookList = new ArrayList<>();

		// Set model and session attributes
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);
		session.setAttribute("selectedUserId", selectedUserId);

		return "redirect:/return-book/return-book-choose-books";
	}

	/**
	 * Returns the view of "return-book-choose-books" with model attributes:<br>
	 * <ul>
	 * <li>theUser - The User object</li>
	 * <li>tempReturnedBookList - The temporary list of the books to return</li>
	 * <li>userBorrowedBooksList - The list of borrowed books</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param locale
	 *            The Locale containing the user's locale
	 * @param systemMessage
	 *            The String containing the system message
	 * @return The String representing the name of the view
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/return-book-choose-books")
	public String showReturnBookChooseBooks(HttpServletRequest request, Model theModel, Locale locale,
			@RequestParam(required = false, name = "systemMessage") String systemMessage) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the is of the user from the session
		int theUserId = (int) session.getAttribute("selectedUserId");

		// Get the user with given id
		User theUser = userService.getUserById(theUserId);

		// Get the list of BorrowedBooks and Books
		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		// Set model and session attributes
		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("tempReturnedBookList", tempReturnedBookList);
		theModel.addAttribute("userBorrowedBooksList", userBorrowedBooksList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "return-book-choose-books";
	}

	/**
	 * Deletes the book form the list and redirects to the view of
	 * "return-book-choose-books" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param bookId
	 *            The id containing the id of the book
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/deleteReturnedBookFromList")
	public String deleteReturnedBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Remove the book from the list
		bookService.deleteReturnedBookFromList(session, bookId);

		// Set redirect attributes
		redirectAttributes.addAttribute("systemMessage", env.getProperty(
				locale.getLanguage() + ".controller.ReturnBookController.deleteReturnedBookFromList.success.1"));

		return "redirect:/return-book/return-book-choose-books";
	}

	/**
	 * Adds the book to the list and redirects to the view of
	 * "return-book-choose-books" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
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
	@RequestMapping("/addReturnedBookToList")
	public String addReturnedBookToList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Add the book to the list
		bookService.addReturnedBookToList(session, bookId);

		// Set redirect attributes
		redirectAttributes.addAttribute("systemMessage", env.getProperty(
				locale.getLanguage() + ".controller.ReturnBookController.addReturnedBookToList.success.1"));

		return "redirect:/return-book/return-book-choose-books";
	}

	/**
	 * Adds all books to the list and redirects to the view of
	 * "return-book-choose-books" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/addAllBorrowedBookToList")
	public String addAllBorrowedBookToList(HttpServletRequest request, RedirectAttributes redirectAttributes,
			Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Add All books to the list
		bookService.addAllBorrowedBookToList(session);

		// Set redirect attributes
		redirectAttributes.addAttribute("systemMessage", env.getProperty(
				locale.getLanguage() + ".controller.ReturnBookController.addAllBorrowedBookToList.success.1"));

		return "redirect:/return-book/return-book-choose-books";
	}

	/**
	 * Returns the view of "return-book-confirmation" with model attributes:<br>
	 * <ul>
	 * <li>returnedBookInfo - The information displayed on the web page</li>
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
	@RequestMapping("/return-book")
	public String returnBook(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel,
			Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the list of the Book
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		// Check the size of the list. If the the list is smaller then 1 then return the
		// error message
		if (tempReturnedBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.ReturnBookController.returnBook.error.1"));
			return "redirect:/return-book/return-book-choose-books";
		}
		String returnedBookInfo = bookService.returnBooks(session);

		// Set model and session attributes
		theModel.addAttribute("returnedBookInfo", returnedBookInfo);
		session.setAttribute("selectedUserId", null);
		session.setAttribute("tempReturnedBookList", null);
		session.setAttribute("userBorrowedBooksList", null);

		return "return-book-confirmation";
	}

	/**
	 * Returns the confirmation as a PDF File.
	 * 
	 * @param returnedBookInfo
	 *            The String containing the information about returned books.
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 * @throws FileNotFoundException
	 *             A FileNotFoundException is thrown when th file cannot be found.
	 */
	@RequestMapping("/generate-return-book-confirmation")
	public ResponseEntity<InputStreamResource> generateReturnBookConfirmation(
			@RequestParam("returnedBookInfo") String returnedBookInfo, HttpServletRequest request)
			throws FileNotFoundException {

		HttpSession session = request.getSession();

		StringTokenizer st = new StringTokenizer(returnedBookInfo);
		List<Book> bookList = new ArrayList<>();
		User tempUser = userService.getUserById(Integer.valueOf(st.nextToken()));
		int bookId;

		while (st.hasMoreTokens()) {
			bookId = Integer.parseInt(st.nextToken());
			bookList.add(bookService.getBookById(bookId));
		}

		String employeeName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		File tempFile = pdfService.generateConfirmationForReturnBook(tempUser, employeeName, bookList);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Clears the search parameters and redirects to the view of "main".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/cancel-book-returning")
	public String cancelBookReturning(HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clear search parameters
		String[] searchParametersName = { "returnBookSelectedUserId", "returnBookFirstName", "returnBookLastName",
				"returnBookEmail", "returnBookPesel", "returnBookStartResult", "tempBookList", "selectedUserId",
				"userBorrowedBooksList", "tempReturnedBookList" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/user/main";
	}
}
