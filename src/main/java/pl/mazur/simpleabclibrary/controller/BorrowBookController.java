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
import org.springframework.context.annotation.Scope;
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
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;

@Controller
@Scope("session")
@RequestMapping("/borrow-book")
public class BorrowBookController {

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
	ForbiddenWords forbiddenWords;

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

		String[] userSearchParameters = userService.prepareTableToSearch(session, "borrowBook",
				borrowBookSelectedUserId, borrowBookFirstName, borrowBookLastName, borrowBookEmail, borrowBookPesel);

		borrowBookStartResult = (borrowBookStartResult == null)
				? ((session.getAttribute("borrowBookStartResult") != null)
						? (Integer) session.getAttribute("borrowBookStartResult")
						: 0)
				: 0;
		session.setAttribute("borrowBookStartResult", borrowBookStartResult);

		boolean hasAnyParameters = userService.hasTableAnyParameters(userSearchParameters);
		List<User> usersList = hasAnyParameters
				? userService.getUserSearchResult(userSearchParameters, borrowBookStartResult)
				: userService.getAllUsers(borrowBookStartResult);
		long amountOfResults = hasAnyParameters ? userService.getAmountOfSearchResult(userSearchParameters)
				: userService.getAmountOfAllUsers();

		long showMoreLinkValue = userService.generateShowMoreLinkValue(borrowBookStartResult, amountOfResults);
		String resultRange = userService.generateResultRange(borrowBookStartResult, amountOfResults, showMoreLinkValue);
		long showLessLinkValue = userService.generateShowLessLinkValue(borrowBookStartResult);
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

		userService.clearSearchParameters(session, "borrowBook");

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

		borrowBookChooseBookStartResult = (borrowBookChooseBookStartResult == null)
				? ((session.getAttribute("borrowBookChooseBookStartResult") != null)
						? (Integer) session.getAttribute("borrowBookChooseBookStartResult")
						: 0)
				: 0;
		session.setAttribute("borrowBookStartResult", borrowBookChooseBookStartResult);

		int theUserId;
		if (selectedUserId == null)
			theUserId = Integer.valueOf((String) session.getAttribute("selectedUserId"));
		else
			session.setAttribute("selectedUserId", selectedUserId);
		theUserId = Integer.valueOf(selectedUserId);

		User user = userService.getUser(theUserId);
		List<BorrowedBook> borrowedBookList = bookService.getUserBorrowedBookList(user.getId());
		@SuppressWarnings("unchecked")
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		List<Reservation> tempReservationList = reservationService.getUserReservations(theUserId);

		isAbleToBorrow = true;
		if (borrowedBookList.size() + tempBookList.size() >= 5) {
			isAbleToBorrow = false;
			extraMessage = " Limit wypo¿yczenia zosta³ osi¹gniêty";
		} else
			extraMessage = " U¿ytkownik mo¿e wypo¿yczyæ jeszcze: "
					+ (5 - borrowedBookList.size() - tempBookList.size());

		if (isAbleToBorrow) {
			for (BorrowedBook borrowedBook : borrowedBookList) {
				Long currentTimeMillis = System.currentTimeMillis();
				Long expTimeMillis = borrowedBook.getExpectedEndDate().getTime();
				if (currentTimeMillis > expTimeMillis) {
					isAbleToBorrow = false;
					extraMessage = " U¿ytkownik posiada przeterminowan¹ ksi¹¿kê";
					break;
				}
			}
		}

		String[] searchBookParameters = bookService.prepareTableToSearch(session, "borrowBook", title, bookId, author,
				isbn, publisher);
		boolean hasAnyParameters = bookService.hasTableAnyParameters(searchBookParameters);

		List<Book> booksList = hasAnyParameters
				? bookService.bookSearchResult(searchBookParameters, borrowBookChooseBookStartResult)
				: bookService.getAllBooks(borrowBookChooseBookStartResult);
		long amountOfResults = hasAnyParameters ? bookService.getAmountOfSearchResult(searchBookParameters)
				: bookService.getAmountOfAllBooks();

		long showMoreLinkValue = bookService.generateShowMoreLinkValue(borrowBookChooseBookStartResult,
				amountOfResults);
		String resultRange = bookService.generateResultRange(borrowBookChooseBookStartResult, amountOfResults,
				showMoreLinkValue);
		long showLessLinkValue = bookService.generateShowLessLinkValue(borrowBookChooseBookStartResult);

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

		bookService.clearBorrowedBookSearchParameters(session);

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
			redirectAttributes.addAttribute("errorMessage", "Nie mo¿na wypo¿yczyæ wiêcej ksi¹¿ek");

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
			redirectAttributes.addAttribute("errorMessage", "Nie mo¿na wypo¿yczyæ wiêcej ksi¹¿ek");

		return "redirect:/borrow-book/borrow-book-choose-books";

	}

	@RequestMapping("/deleteBookFromList")
	public String deleteBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.deleteBookFromList(session, bookId);
		redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a usuniêta z listy");

		return "redirect:/borrow-book/borlrow-book-choose-books";
	}

	@RequestMapping("/borrow-books")
	public String borrowBooks(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		@SuppressWarnings("unchecked")
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		if (tempBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage", "Lista ksi¹¿ek do wypo¿yczenia jest pusta!!");
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

		bookService.cancelBorrowedBook(session);

		return "redirect:/user/main";
	}
}