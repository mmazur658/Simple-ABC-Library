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

@Controller
@Scope("session")
@RequestMapping("/return-book")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class ReturnBookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private Environment env;

	@Autowired
	private AccessLevelControl accessLevelControl;

	@Autowired
	private SearchEngineUtils searchEngineUtils;

	@RequestMapping("/return-book-choose-user")
	public String borrowBook(Locale locale, Model theModel, HttpServletRequest request,
			@RequestParam(required = false, name = "returnBookSelectedUserId") String returnBookSelectedUserId,
			@RequestParam(required = false, name = "returnBookFirstName") String returnBookFirstName,
			@RequestParam(required = false, name = "returnBookLastName") String returnBookLastName,
			@RequestParam(required = false, name = "returnBookEmail") String returnBookEmail,
			@RequestParam(required = false, name = "returnBookPesel") String returnBookPesel,
			@RequestParam(required = false, name = "returnBookStartResult") Integer returnBookStartResult) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "returnBookSelectedUserId", "returnBookFirstName", "returnBookLastName",
				"returnBookEmail", "returnBookPesel" };
		String[] searchParametersValue = { returnBookSelectedUserId, returnBookFirstName, returnBookLastName,
				returnBookEmail, returnBookPesel };
		String[] userSearchParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);

		if (returnBookStartResult == null)
			returnBookStartResult = (Integer) session.getAttribute("returnBookStartResult");
		if (returnBookStartResult == null)
			returnBookStartResult = 0;
		session.setAttribute("returnBookStartResult", returnBookStartResult);

		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(userSearchParameters);
		List<User> usersList = hasAnyParameters
				? userService.getUserSearchResult(userSearchParameters, returnBookStartResult)
				: userService.getAllUsers(returnBookStartResult);
		long amountOfResults = hasAnyParameters ? userService.getAmountOfSearchResult(userSearchParameters)
				: userService.getAmountOfAllUsers();

		int searchResultLimit = Integer.valueOf(env.getProperty(locale.getLanguage() + ".search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(returnBookStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(returnBookStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(returnBookStartResult, searchResultLimit);

		session.setAttribute("returnBookStartResult", returnBookStartResult);
		theModel.addAttribute("returnBookStartResult", returnBookStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("usersList", usersList);

		return "return-book-choose-user";
	}

	@RequestMapping("/clearReturnBookUserSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "returnBookStartResult", "returnBookSelectedUserId", "returnBookFirstName",
				"returnBookLastName", "returnBookEmail", "returnBookPesel" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/return-book/return-book-choose-user";
	}

	@RequestMapping("/prepareForReturn")
	public String returnBookPreparation(@RequestParam(required = false, name = "userId") int selectedUserId,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		List<BorrowedBook> userBorrowedBooksList = bookService.getUserBorrowedBookList(selectedUserId);
		List<Book> tempReturnedBookList = new ArrayList<>();
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);
		session.setAttribute("selectedUserId", selectedUserId);

		return "redirect:/return-book/return-book-choose-books";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/return-book-choose-books")
	public String returnBookChooseBooks(HttpServletRequest request, Model theModel, Locale locale,
			@RequestParam(required = false, name = "systemMessage") String systemMessage) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		int theUserId = (int) session.getAttribute("selectedUserId");
		User theUser = userService.getUser(theUserId);
		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("tempReturnedBookList", tempReturnedBookList);
		theModel.addAttribute("userBorrowedBooksList", userBorrowedBooksList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "return-book-choose-books";
	}

	@RequestMapping("/deleteReturnedBookFromList")
	public String deleteReturnedBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.deleteReturnedBookFromList(session, bookId);

		redirectAttributes.addAttribute("systemMessage", env.getProperty(
				locale.getLanguage() + ".controller.ReturnBookController.deleteReturnedBookFromList.success.1"));

		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/addReturnedBookToList")
	public String addReturnedBookToList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.addReturnedBookToList(session, bookId);
		redirectAttributes.addAttribute("systemMessage", env.getProperty(
				locale.getLanguage() + ".controller.ReturnBookController.addReturnedBookToList.success.1"));

		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/addAllBorrowedBookToList")
	public String addAllBorrowedBookToList(HttpServletRequest request, RedirectAttributes redirectAttributes,
			Locale locale) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.addAllBorrowedBookToLiest(session);
		redirectAttributes.addAttribute("systemMessage", env.getProperty(
				locale.getLanguage() + ".controller.ReturnBookController.addAllBorrowedBookToList.success.1"));

		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/return-book")
	public String returnBook(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel,
			Locale locale) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		@SuppressWarnings("unchecked")
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		if (tempReturnedBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.ReturnBookController.returnBook.error.1"));
			return "redirect:/return-book/return-book-choose-books";
		}

		String returnedBookInfo = bookService.returnBooks(session);

		theModel.addAttribute("returnedBookInfo", returnedBookInfo);
		session.setAttribute("selectedUserId", null);
		session.setAttribute("tempReturnedBookList", null);
		session.setAttribute("userBorrowedBooksList", null);

		return "return-book-confirmation";
	}

	@RequestMapping("/generate-return-book-confirmation")
	public ResponseEntity<InputStreamResource> generateReturnBookConfirmation(
			@RequestParam("returnedBookInfo") String returnedBookInfo, HttpServletRequest request)
			throws FileNotFoundException {

		HttpSession session = request.getSession();

		StringTokenizer st = new StringTokenizer(returnedBookInfo);
		List<Book> bookList = new ArrayList<>();
		User tempUser = userService.getUser(Integer.valueOf(st.nextToken()));
		int bookId;

		while (st.hasMoreTokens()) {
			bookId = Integer.parseInt(st.nextToken());
			bookList.add(bookService.getBook(bookId));
		}

		String employeeName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		File tempFile = pdfService.generateReturnBookConfirmation(tempUser, employeeName, bookList);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
	}

	@RequestMapping("/cancel-book-returning")
	public String cancelBookReturning(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "returnBookSelectedUserId", "returnBookFirstName", "returnBookLastName",
				"returnBookEmail", "returnBookPesel", "returnBookStartResult", "tempBookList", "selectedUserId",
				"userBorrowedBooksList", "tempReturnedBookList" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/user/main";
	}
}
