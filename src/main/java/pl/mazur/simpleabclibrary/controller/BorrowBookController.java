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
import pl.mazur.simpleabclibrary.entity.BookBorrowing;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.LoginAndAccessLevelCheck;

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
	LoginAndAccessLevelCheck loginAndAccessLevelCheck;

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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		if (!(borrowBookSelectedUserId == null))
			session.setAttribute("borrowBookSelectedUserId", borrowBookSelectedUserId);
		if (!(borrowBookFirstName == null))
			session.setAttribute("borrowBookFirstName", borrowBookFirstName);
		if (!(borrowBookLastName == null))
			session.setAttribute("borrowBookLastName", borrowBookLastName);
		if (!(borrowBookEmail == null))
			session.setAttribute("borrowBookEmail", borrowBookEmail);
		if (!(borrowBookPesel == null))
			session.setAttribute("borrowBookPesel", borrowBookPesel);

		if ((borrowBookSelectedUserId == null) && !(session.getAttribute("borrowBookSelectedUserId") == null))
			borrowBookSelectedUserId = String.valueOf(session.getAttribute("borrowBookSelectedUserId"));
		if ((borrowBookFirstName == null) && !(session.getAttribute("borrowBookFirstName") == null))
			borrowBookFirstName = (String) session.getAttribute("borrowBookFirstName");
		if ((borrowBookLastName == null) && !(session.getAttribute("borrowBookLastName") == null))
			borrowBookLastName = (String) session.getAttribute("borrowBookLastName");
		if ((borrowBookEmail == null) && !(session.getAttribute("borrowBookEmail") == null))
			borrowBookEmail = (String) session.getAttribute("borrowBookEmail");
		if ((borrowBookPesel == null) && !(session.getAttribute("borrowBookPesel") == null))
			borrowBookPesel = (String) session.getAttribute("borrowBookPesel");

		String[] userSearchParameters = { "", "", "", "", "" };
		userSearchParameters[0] = (borrowBookSelectedUserId == null) ? "" : borrowBookSelectedUserId.trim();
		userSearchParameters[1] = (borrowBookFirstName == null) ? "" : borrowBookFirstName.trim();
		userSearchParameters[2] = (borrowBookLastName == null) ? "" : borrowBookLastName.trim();
		userSearchParameters[3] = (borrowBookEmail == null) ? "" : borrowBookEmail.trim();
		userSearchParameters[4] = (borrowBookPesel == null) ? "" : borrowBookPesel.trim();
		
		for (String word:userSearchParameters) {
			if(forbiddenWords.findForbiddenWords(word)) {
				return "redirect:/error";
			}
		}

		List<User> usersList;
		long amountOfResults;

		if (borrowBookStartResult == null)
			borrowBookStartResult = (Integer) session.getAttribute("borrowBookStartResult");
		if (borrowBookStartResult == null)
			borrowBookStartResult = 0;

		if (!userSearchParameters[0].equals("") || !userSearchParameters[1].equals("")
				|| !userSearchParameters[2].equals("") || !userSearchParameters[3].equals("")
				|| !userSearchParameters[4].equals("")) {
			usersList = userService.getUserSearchResult(userSearchParameters, borrowBookStartResult);
			amountOfResults = userService.getAmountOfSearchResult(userSearchParameters);
		} else {
			usersList = userService.getAllUsers(borrowBookStartResult);
			amountOfResults = userService.getAmountOfAllBooks();
		}

		String resultRange;
		long showMoreLinkValue = 0;
		if ((borrowBookStartResult + 10) > amountOfResults) {
			showMoreLinkValue = borrowBookStartResult;
			resultRange = "Wyniki od " + (borrowBookStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = borrowBookStartResult + 10;
			resultRange = "Wyniki od " + (borrowBookStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((borrowBookStartResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = borrowBookStartResult - 10;
		}

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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		session.setAttribute("borrowBookSelectedUserId", null);
		session.setAttribute("borrowBookFirstName", null);
		session.setAttribute("borrowBookLastName", null);
		session.setAttribute("borrowBookEmail", null);
		session.setAttribute("borrowBookPesel", null);
		session.setAttribute("borrowBookStartResult", null);

		return "redirect:/borrow-book/borrow-book-choose-user";
	}

	@RequestMapping("/borrow-book-choose-books")
	public String borrowBookChooseBooks(@RequestParam(required = false, name = "selectedUserId") String selectedUserId,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "bookId") String bookId,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "isAbleToBorrow") boolean isAbleToBorrow,
			@RequestParam(required = false, name = "extraMessage") String extraMessage,
			@RequestParam(required = false, name = "borrowBookChooseBookStartResult") Integer borrowBookChooseBookStartResult,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		session.setAttribute("borrowBookStartResult", null);

		if (selectedUserId != null)
			session.setAttribute("selectedUserId", selectedUserId);

		int theUserId;
		if (selectedUserId == null)
			theUserId = Integer.valueOf((String) session.getAttribute("selectedUserId"));
		else
			theUserId = Integer.valueOf(selectedUserId);

		User user = userService.getUser(theUserId);
		List<BookBorrowing> borrowedBookList = bookService.getUserBookBorrowing(user.getId());
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		List<Reservation> tempReservationList = reservationService.getUserReservations(theUserId);

		isAbleToBorrow = true;

		if (borrowedBookList.size() + tempBookList.size() >= 5) {
			isAbleToBorrow = false;
			extraMessage = " Limit wypo¿yczenia zosta³ osi¹gniêty";
		} else {
			extraMessage = " U¿ytkownik mo¿e wypo¿yczyæ jeszcze: "
					+ (5 - borrowedBookList.size() - tempBookList.size());
		}

		if (isAbleToBorrow) {
			for (BookBorrowing borrowingBook : borrowedBookList) {
				Long currentTimeMillis = System.currentTimeMillis();
				Long expTimeMillis = borrowingBook.getExpectedEndDate().getTime();

				if (currentTimeMillis > expTimeMillis) {
					isAbleToBorrow = false;
					extraMessage = " U¿ytkownik posiada przeterminowan¹ ksi¹¿kê";
					break;
				}
			}
		}

		if (!(title == null))
			session.setAttribute("title", title);
		if (!(bookId == null))
			session.setAttribute("id", bookId);
		if (!(author == null))
			session.setAttribute("author", author);

		if ((title == null) && !(session.getAttribute("title") == null))
			title = (String) session.getAttribute("title");
		if ((bookId == null) && !(session.getAttribute("id") == null))
			bookId = (String) session.getAttribute("id");
		if ((author == null) && !(session.getAttribute("author") == null))
			author = (String) session.getAttribute("author");

		String[] searchParameters = { "", "", "", "", "", "" };
		searchParameters[0] = (title == null) ? "" : title.trim();
		searchParameters[1] = (author == null) ? "" : author.trim();
		searchParameters[2] = "";
		searchParameters[3] = "";
		searchParameters[4] = "";
		searchParameters[5] = (bookId == null) ? "" : bookId.trim();
		
		for (String word:searchParameters) {
			if(forbiddenWords.findForbiddenWords(word)) {
				return "redirect:/error";
			}
		}

		List<Book> booksList;
		long amountOfResults;

		if (borrowBookChooseBookStartResult == null)
			borrowBookChooseBookStartResult = (Integer) session.getAttribute("borrowBookChooseBookStartResult");
		if (borrowBookChooseBookStartResult == null)
			borrowBookChooseBookStartResult = 0;
		
		

		if (!searchParameters[0].equals("") || !searchParameters[1].equals("") || !searchParameters[2].equals("")
				|| !searchParameters[3].equals("") || !searchParameters[4].equals("")
				|| !searchParameters[5].equals("")) {
			amountOfResults = bookService.getAmountOfSearchResult(searchParameters);
			booksList = bookService.bookSearchResult(searchParameters, borrowBookChooseBookStartResult);
		} else {
			amountOfResults = bookService.getAmountOfAllBooks();
			booksList = bookService.getAllBooks(borrowBookChooseBookStartResult);
		}

		String resultRange;
		long showMoreLinkValue = 0;
		if ((borrowBookChooseBookStartResult + 10) > amountOfResults) {
			showMoreLinkValue = borrowBookChooseBookStartResult;
			resultRange = "Wyniki od " + (borrowBookChooseBookStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = borrowBookChooseBookStartResult + 10;
			resultRange = "Wyniki od " + (borrowBookChooseBookStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((borrowBookChooseBookStartResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = borrowBookChooseBookStartResult - 10;
		}

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

		return "borrow-book-choose-books";
	}

	@RequestMapping("/clearBookSearchParameters")
	public String clearBookSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		session.setAttribute("borrowBookChooseBookStartResult", null);
		session.setAttribute("title", null);
		session.setAttribute("id", null);
		session.setAttribute("author", null);

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	@RequestMapping("/addBookToList")
	public String addBookToList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";


		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		Book theBook = bookService.getBook(bookId);
		boolean isAllreadyOnTheList = false;

		for (Book tempBook : tempBookList) {
			if (tempBook.getId() == theBook.getId())
				isAllreadyOnTheList = true;
		}

		if (!isAllreadyOnTheList) {
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a dodana do listy. ");
			return "redirect:/borrow-book/borrow-book-choose-books";
		} else {
			redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka ju¿ znajduje siê na liœcie. ");
			return "redirect:/borrow-book/borrow-book-choose-books";
		}
	}

	@RequestMapping("/addReservedBookToList")
	public String addReservedBookToList(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		Reservation reservation = reservationService.getReservation(reservationId);
		Book theBook = reservation.getBook();
		boolean isAllreadyOnTheList = false;

		for (Book tempBook : tempBookList) {
			if (tempBook.getId() == theBook.getId())
				isAllreadyOnTheList = true;
		}

		if (!isAllreadyOnTheList) {
			reservationService.deleteReservationInOrderToCreateBookBorrowing(reservation);
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a dodana do listy");
			return "redirect:/borrow-book/borrow-book-choose-books";
		} else {
			redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka ju¿ znajduje siê na liœcie. ");
			return "redirect:/borrow-book/borrow-book-choose-books";
		}
	}

	@RequestMapping("/deleteBookFromList")
	public String deleteBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		for (int index = 0; index < tempBookList.size(); index++) {
			if (tempBookList.get(index).getId() == bookId) {
				tempBookList.remove(index);
				break;
			}

		}
		
		redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a usuniêta z listy");

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	@RequestMapping("/borrow-books")
	public String borrowBooks(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		
		if (tempBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage", "Lista ksi¹¿ek do wypo¿yczenia jest pusta!!");
			return "redirect:/borrow-book/borrow-book-choose-books";
		} else {
			User tempUser = userService.getUser(Integer.valueOf((String) session.getAttribute("selectedUserId")));
			StringBuilder sb = new StringBuilder();

			sb.append(tempUser.getId());
			sb.append(" ");

			for (Book tempBook : tempBookList) {
				bookService.borrowBook(tempBook, tempUser);
				sb.append(tempBook.getId());
				sb.append(" ");
			}
			String bookBorrowingInfo = sb.toString();

			theModel.addAttribute("bookBorrowingInfo", bookBorrowingInfo);
			session.setAttribute("isUserAbleToBorrow", false);
			session.setAttribute("tempBookList", null);

			return "borrow-book-confirmation";
		}
	}

	@RequestMapping("/generate-book-borrowing-confirmation")
	public ResponseEntity<InputStreamResource> generateBookBorrowingConfirmation(
			@RequestParam("bookBorrowingInfo") String bookBorrowingInfo, HttpServletRequest request)
			throws FileNotFoundException {

		HttpSession session = request.getSession();

		int bookId;
		StringTokenizer st = new StringTokenizer(bookBorrowingInfo);
		List<Book> bookList = new ArrayList<>();
		User tempUser = userService.getUser(Integer.valueOf(st.nextToken()));

		while (st.hasMoreTokens()) {
			bookId = Integer.parseInt(st.nextToken());
			bookList.add(bookService.getBook(bookId));
		}
		
		Date expectedEndDate = bookService.getExpectedEndDate(tempUser, bookList.get(0));
		String employeeName = session.getAttribute("userLastName") + " " + session.getAttribute("userFirstName");
		File tempFile = pdfService.generateBookBorrowingConfirmation(bookList, tempUser, expectedEndDate, employeeName);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);

	}

	@RequestMapping("/cancel-book-borrowing")
	public String cancelBookBorrowing(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		session.setAttribute("borrowBookSelectedUserId", null);
		session.setAttribute("borrowBookFirstName", null);
		session.setAttribute("borrowBookLastName", null);
		session.setAttribute("borrowBookEmail", null);
		session.setAttribute("borrowBookPesel", null);
		session.setAttribute("borrowBookStartResult", null);
		session.setAttribute("borrowBookStartResult", null);
		session.setAttribute("tempBookList", null);
		session.setAttribute("borrowBookChooseBookStartResult", null);
		session.setAttribute("title", null);
		session.setAttribute("id", null);
		session.setAttribute("author", null);
		session.setAttribute("isUserAbleToBorrow", false);

		return "redirect:/user/main";
	}
}
