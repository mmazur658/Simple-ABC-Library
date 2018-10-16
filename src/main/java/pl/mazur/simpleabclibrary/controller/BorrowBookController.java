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
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";
		
		String[] userSearchParameters = userService.prepareTableToSearch(session, "borrowBook", borrowBookSelectedUserId,
				borrowBookFirstName, borrowBookLastName, borrowBookEmail, borrowBookPesel);
		
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
		long amountOfResults = hasAnyParameters 
				? userService.getAmountOfSearchResult(userSearchParameters)
				: userService.getAmountOfAllUsers();

		long showMoreLinkValue=userService.generateShowMoreLinkValue(borrowBookStartResult,amountOfResults);
		String resultRange = userService.generateResultRange(borrowBookStartResult,amountOfResults,showMoreLinkValue);
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
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
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
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
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

		if (!(title == null))
			session.setAttribute("borrowBookSeachParamTitle", title);
		if (!(bookId == null))
			session.setAttribute("borrowBookSeachParamId", bookId);
		if (!(author == null))
			session.setAttribute("borrowBookSeachParamAuthor", author);
		if (!(isbn == null))
			session.setAttribute("borrowBookSeachParamIsbn", isbn);
		if (!(publisher == null))
			session.setAttribute("borrowBookSeachParamPublisher", publisher);

		if ((title == null) && !(session.getAttribute("title") == null))
			title = (String) session.getAttribute("title");
		if ((bookId == null) && !(session.getAttribute("id") == null))
			bookId = (String) session.getAttribute("id");
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
		searchParameters[5] = (bookId == null) ? "" : bookId.trim();

		for (String word : searchParameters) {
			if (forbiddenWords.findForbiddenWords(word))
				return "redirect:/error";
		}

		List<Book> booksList;
		long amountOfResults;

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
		theModel.addAttribute("errorMessage", errorMessage);
		return "borrow-book-choose-books";
	}

	@RequestMapping("/clearBookSearchParameters")
	public String clearBookSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.clearBookSearchParameters(session);

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	@RequestMapping("/addBookToList")
	public String addBookToList(@RequestParam("bookId") int bookId,
			@RequestParam("isAbleToBorrow") boolean isAbleToBorrow, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (isAbleToBorrow) {
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
		} else {
			redirectAttributes.addAttribute("errorMessage", "Nie mo¿na wypo¿yczyæ wiêcej ksi¹¿ek");
			return "redirect:/borrow-book/borrow-book-choose-books";
		}

	}

	@RequestMapping("/addReservedBookToList")
	public String addReservedBookToList(@RequestParam("reservationId") int reservationId,
			@RequestParam("isAbleToBorrow") boolean isAbleToBorrow, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";
		
		if(isAbleToBorrow) {
			List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
			Reservation reservation = reservationService.getReservation(reservationId);
			Book theBook = reservation.getBook();
			boolean isAllreadyOnTheList = false;

			for (Book tempBook : tempBookList) {
				if (tempBook.getId() == theBook.getId())
					isAllreadyOnTheList = true;
			}

			if (!isAllreadyOnTheList) {
				reservationService.deleteReservationInOrderToCreateBorrowedBook(reservation);
				tempBookList.add(theBook);
				session.setAttribute("tempBookList", tempBookList);
				redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a dodana do listy");
				return "redirect:/borrow-book/borrow-book-choose-books";
			} else {
				redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka ju¿ znajduje siê na liœcie. ");
				return "redirect:/borrow-book/borrow-book-choose-books";
			}
		} else {
			redirectAttributes.addAttribute("errorMessage", "Nie mo¿na wypo¿yczyæ wiêcej ksi¹¿ek");
			return "redirect:/borrow-book/borrow-book-choose-books";
		}
		

	}

	@RequestMapping("/deleteBookFromList")
	public String deleteBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.deleteBookFromList(session, bookId);
		redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a usuniêta z listy");

		return "redirect:/borrow-book/borlrow-book-choose-books";
	}

	@RequestMapping("/borrow-books")
	public String borrowBooks(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel) {

		HttpSession session = request.getSession();
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
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
			String borrowedBookInfo = sb.toString();

			theModel.addAttribute("borrowedBookInfo", borrowedBookInfo);
			session.setAttribute("isUserAbleToBorrow", false);
			session.setAttribute("tempBookList", null);

			return "borrow-book-confirmation";
		}
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
		if(!accessLevelControl.isEmployee((LoggedInUser)session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		bookService.cancelBorrowedBook(session);

		return "redirect:/user/main";
	}
}
