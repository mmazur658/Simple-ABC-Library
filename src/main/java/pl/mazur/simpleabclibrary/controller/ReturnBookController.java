package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import pl.mazur.simpleabclibrary.entity.BookBorrowing;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.LoginAndAccessLevelCheck;

@Controller
@Scope("session")
@RequestMapping("/return-book")
public class ReturnBookController {

	@Autowired
	BookService bookService;

	@Autowired
	UserService userService;

	@Autowired
	PdfService pdfService;

	@Autowired
	LoginAndAccessLevelCheck loginAndAccessLevelCheck;
	
	@Autowired
	ForbiddenWords forbiddenWords;

	@RequestMapping("/return-book-choose-user")
	public String borrowBook(
			@RequestParam(required = false, name = "returnBookSelectedUserId") String returnBookSelectedUserId,
			@RequestParam(required = false, name = "returnBookFirstName") String returnBookFirstName,
			@RequestParam(required = false, name = "returnBookLastName") String returnBookLastName,
			@RequestParam(required = false, name = "returnBookEmail") String returnBookEmail,
			@RequestParam(required = false, name = "returnBookPesel") String returnBookPesel,
			@RequestParam(required = false, name = "returnBookStartResult") Integer returnBookStartResult,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		if (!(returnBookSelectedUserId == null))
			session.setAttribute("returnBookSelectedUserId", returnBookSelectedUserId);
		if (!(returnBookFirstName == null))
			session.setAttribute("returnBookFirstName", returnBookFirstName);
		if (!(returnBookLastName == null))
			session.setAttribute("returnBookLastName", returnBookLastName);
		if (!(returnBookEmail == null))
			session.setAttribute("returnBookEmail", returnBookEmail);
		if (!(returnBookPesel == null))
			session.setAttribute("returnBookPesel", returnBookPesel);

		if ((returnBookSelectedUserId == null) && !(session.getAttribute("returnBookSelectedUserId") == null))
			returnBookSelectedUserId = String.valueOf(session.getAttribute("returnBookSelectedUserId"));
		if ((returnBookFirstName == null) && !(session.getAttribute("returnBookFirstName") == null))
			returnBookFirstName = String.valueOf(session.getAttribute("returnBookFirstName"));
		if ((returnBookLastName == null) && !(session.getAttribute("returnBookLastName") == null))
			returnBookLastName = String.valueOf(session.getAttribute("returnBookLastName"));
		if ((returnBookEmail == null) && !(session.getAttribute("returnBookEmail") == null))
			returnBookEmail = String.valueOf(session.getAttribute("returnBookEmail"));
		if ((returnBookPesel == null) && !(session.getAttribute("returnBookPesel") == null))
			returnBookPesel = String.valueOf(session.getAttribute("returnBookPesel"));

		String[] userSearchParameters = { "", "", "", "", "", "", "", "" };
		userSearchParameters[0] = (returnBookSelectedUserId == null) ? "" : returnBookSelectedUserId.trim();
		userSearchParameters[1] = (returnBookFirstName == null) ? "" : returnBookFirstName.trim();
		userSearchParameters[2] = (returnBookLastName == null) ? "" : returnBookLastName.trim();
		userSearchParameters[3] = (returnBookEmail == null) ? "" : returnBookEmail.trim();
		userSearchParameters[4] = (returnBookPesel == null) ? "" : returnBookPesel.trim();
		
		for (String word:userSearchParameters) {
			if(forbiddenWords.findForbiddenWords(word)) {
				return "redirect:/error";
			}
		}


		if (returnBookStartResult == null)
			returnBookStartResult = (Integer) session.getAttribute("returnBookStartResult");
		if (returnBookStartResult == null)
			returnBookStartResult = 0;

		List<User> usersList;
		long amountOfResults;

		if (!userSearchParameters[0].equals("") || !userSearchParameters[1].equals("")
				|| !userSearchParameters[2].equals("") || !userSearchParameters[3].equals("")
				|| !userSearchParameters[4].equals("")) {
			usersList = userService.getUserSearchResult(userSearchParameters, returnBookStartResult);
			amountOfResults = userService.getAmountOfSearchResult(userSearchParameters);
		} else {
			usersList = userService.getAllUsers(returnBookStartResult);
			amountOfResults = userService.getAmountOfAllBooks();
		}

		String resultRange;
		long showMoreLinkValue = 0;
		if ((returnBookStartResult + 10) > amountOfResults) {
			showMoreLinkValue = returnBookStartResult;
			resultRange = "Wyniki od " + (returnBookStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = returnBookStartResult + 10;
			resultRange = "Wyniki od " + (returnBookStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((returnBookStartResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = returnBookStartResult - 10;
		}

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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";
		
		session.setAttribute("returnBookStartResult", null);
		session.setAttribute("returnBookSelectedUserId", null);
		session.setAttribute("returnBookFirstName", null);
		session.setAttribute("returnBookLastName", null);
		session.setAttribute("returnBookEmail", null);
		session.setAttribute("returnBookPesel", null);

		return "redirect:/borrow-book/borrow-book-choose-books";
	}

	@RequestMapping("/prepareForReturn")
	public String returnBookPreparation(@RequestParam(required = false, name = "userId") int selectedUserId,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<BookBorrowing> userBorrowedBooksList = bookService.getUserBookBorrowing(selectedUserId);
		List<Book> tempReturnedBookList = new ArrayList<>();
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);
		session.setAttribute("selectedUserId", selectedUserId);

		return "redirect:/return-book/return-book-choose-books";

	}

	@RequestMapping("/return-book-choose-books")
	public String returnBookChooseBooks(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "systemMessage") String systemMessage) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		int theUserId = (int) session.getAttribute("selectedUserId");
		User theUser = userService.getUser(theUserId);
		List<BookBorrowing> userBorrowedBooksList = (List<BookBorrowing>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("tempReturnedBookList", tempReturnedBookList);
		theModel.addAttribute("userBorrowedBooksList", userBorrowedBooksList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "return-book-choose-books";
	}

	@RequestMapping("/deleteReturnedBookFromList")
	public String deleteReturnedBookFromList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<BookBorrowing> userBorrowedBooksList = (List<BookBorrowing>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		for (int index = 0; index < tempReturnedBookList.size(); index++) {
			if (tempReturnedBookList.get(index).getId() == bookId) {
				tempReturnedBookList.remove(index);
				userBorrowedBooksList.add(bookService.getBookBorrowing(bookId));
				break;
			}
		}
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);
		redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a usuniêta z listy");

		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/addReturnedBookToList")
	public String addReturnedBookToList(@RequestParam("bookId") int bookId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<BookBorrowing> userBorrowedBooksList = (List<BookBorrowing>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		for (int index = 0; index < userBorrowedBooksList.size(); index++) {
			if (userBorrowedBooksList.get(index).getBook().getId() == bookId) {
				tempReturnedBookList.add(userBorrowedBooksList.get(index).getBook());
				userBorrowedBooksList.remove(index);
				break;
			}
		}
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);

		redirectAttributes.addAttribute("systemMessage", "Ksi¹¿ka zosta³a dodana do listy");
		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/addAllBorrowedBookToList")
	public String addAllBorrowedBookToList(HttpServletRequest request, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<BookBorrowing> userBorrowedBooksList = (List<BookBorrowing>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		for (int index = 0; index < userBorrowedBooksList.size(); index++) {
			tempReturnedBookList.add(userBorrowedBooksList.get(index).getBook());
		}
		userBorrowedBooksList.clear();

		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);

		redirectAttributes.addAttribute("systemMessage", "Wszystkie ksi¹¿ki zosta³y dodane do listy");

		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/return-book")
	public String returnBook(HttpServletRequest request, RedirectAttributes redirectAttributes, Model theModel) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		if (tempReturnedBookList.size() < 1) {
			redirectAttributes.addAttribute("systemMessage", "Lista ksi¹¿ek do zwrotu jest pusta!!");
			return "redirect:/return-book/return-book-choose-books";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append((int) session.getAttribute("selectedUserId"));
			sb.append(" ");

			for (Book book : tempReturnedBookList) {
				bookService.closeBookBorrowing(book);
				bookService.returnBook(book);
				sb.append(book.getId());
				sb.append(" ");
			}

			String returnedBookInfo = sb.toString();

			theModel.addAttribute("returnedBookInfo", returnedBookInfo);
			session.setAttribute("selectedUserId", null);
			session.setAttribute("tempReturnedBookList", null);
			session.setAttribute("userBorrowedBooksList", null);

			return "return-book-confirmation";
		}

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
}
