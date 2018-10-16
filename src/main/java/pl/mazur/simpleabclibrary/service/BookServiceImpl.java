package pl.mazur.simpleabclibrary.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.Reservation;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private ForbiddenWords forbiddenWords;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public List<Book> getAllBooks(int startResult) {
		return bookDAO.getAllBooks(startResult);
	}

	@Override
	@Transactional
	public List<Book> bookSearchResult(String[] searchParameters, int startResult) {

		String hql = prepareHqlUsingBookSearchParameters(searchParameters, "from Book where ");
		return bookDAO.bookSearchResult(hql, startResult);
	}

	@Override
	@Transactional
	public void saveBook(Book tempBook) {
		tempBook.setDateOfAdded(new Date());
		tempBook.setIsActive(true);
		tempBook.setIsAvailable(true);
		bookDAO.saveBook(tempBook);
	}

	@Override
	@Transactional
	public Book getBook(int bookId) {
		return bookDAO.getBook(bookId);
	}

	@Override
	@Transactional
	public void updateBook(Book book) {

		Book tempBook = bookDAO.getBook(book.getId());
		tempBook.setAuthor(book.getAuthor());
		tempBook.setTitle(book.getTitle());
		tempBook.setIsbn(book.getIsbn());
		tempBook.setLanguage(book.getLanguage());
		tempBook.setPages(book.getPages());
		tempBook.setPublisher(book.getPublisher());

		bookDAO.updateBook(book);
	}

	@Override
	@Transactional
	public void deleteBook(Book tempBook) {
		tempBook.setIsActive(false);
		bookDAO.deleteBook(tempBook);
	}

	@Override
	@Transactional
	public void borrowBook(Book tempBook, User tempUser) {

		BorrowedBook borrowedBook = new BorrowedBook();
		borrowedBook.setBook(tempBook);
		borrowedBook.setUser(tempUser);
		borrowedBook.setStartDate(new Date());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(borrowedBook.getStartDate());
		calendar.add(Calendar.DATE, 14);

		borrowedBook.setExpectedEndDate(calendar.getTime());

		tempBook.setIsAvailable(false);
		bookDAO.updateBook(tempBook);
		bookDAO.borrowBook(borrowedBook);
	}

	@Override
	@Transactional
	public List<BorrowedBook> getUserBorrowedBookList(int userId) {
		return bookDAO.getUserBorrowedBookList(userId);
	}

	@Override
	@Transactional
	public void returnBook(Book book) {
		book.setIsAvailable(true);
		bookDAO.updateBook(book);
	}

	@Override
	@Transactional
	public void closeBorrowedBook(Book book) {
		bookDAO.closeBorrowedBook(book);
	}

	@Override
	@Transactional
	public BorrowedBook getBorrowedBook(int borrowedBookId) {
		return bookDAO.getBorrowedBook(borrowedBookId);
	}

	@Override
	@Transactional
	public Date getExpectedEndDate(User tempUser, Book book) {
		return bookDAO.getExpectedEndDate(tempUser, book);
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] searchParameters) {
		String hql = prepareHqlUsingBookSearchParameters(searchParameters, "select count(*) from Book where ");
		return bookDAO.getAmountOfSearchResult(hql);
	}

	@Override
	@Transactional
	public long getAmountOfAllBooks() {
		return bookDAO.getAmountOfAllBooks();
	}

	@Override
	@Transactional
	public List<BorrowedBook> getAllBorrowedBookList() {
		return bookDAO.getAllBorrowedBookList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAllBorrowedBookToLiest(HttpSession session) {

		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		for (int index = 0; index < userBorrowedBooksList.size(); index++) {
			tempReturnedBookList.add(userBorrowedBooksList.get(index).getBook());
		}
		userBorrowedBooksList.clear();

		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void addReturnedBookToList(HttpSession session, int bookId) {

		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
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

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void deleteReturnedBookFromList(HttpSession session, int bookId) {

		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		for (int index = 0; index < tempReturnedBookList.size(); index++) {
			if (tempReturnedBookList.get(index).getId() == bookId) {
				tempReturnedBookList.remove(index);
				userBorrowedBooksList.add(bookDAO.getBorrowedBook(bookId));
				break;
			}
		}
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String returnBooks(HttpSession session) {

		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		StringBuilder sb = new StringBuilder();
		sb.append((int) session.getAttribute("selectedUserId"));
		sb.append(" ");

		for (Book book : tempReturnedBookList) {
			bookDAO.closeBorrowedBook(book);
			book.setIsAvailable(true);
			bookDAO.updateBook(book);
			sb.append(book.getId());
			sb.append(" ");
		}

		return sb.toString();
	}

	@Override
	public void cancelBookReturning(HttpSession session) {

		session.setAttribute("returnBookSelectedUserId", null);
		session.setAttribute("returnBookFirstName", null);
		session.setAttribute("returnBookLastName", null);
		session.setAttribute("returnBookEmail", null);
		session.setAttribute("returnBookPesel", null);
		session.setAttribute("returnBookStartResult", null);
		session.setAttribute("tempBookList", null);
		session.setAttribute("selectedUserId", null);
		session.setAttribute("userBorrowedBooksList", null);
		session.setAttribute("tempReturnedBookList", null);

	}

	@Override
	public void clearBorrowedBookSearchParameters(HttpSession session) {
		session.setAttribute("borrowBookChooseBookStartResult", null);
		session.setAttribute("title", null);
		session.setAttribute("id", null);
		session.setAttribute("author", null);

	}

	@Override
	public void cancelBorrowedBook(HttpSession session) {
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

	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteBookFromList(HttpSession session, int bookId) {

		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		for (int index = 0; index < tempBookList.size(); index++) {
			if (tempBookList.get(index).getId() == bookId) {
				tempBookList.remove(index);
				break;
			}
		}
	}

	@Override
	public String[] prepareTableToSearch(HttpSession session, String searchType, String title, String bookId,
			String author, String isbn, String publisher) {

		if (title != null)
			session.setAttribute(searchType + "SeachParamTitle", title);
		if (bookId != null)
			session.setAttribute(searchType + "SeachParamId", bookId);
		if (author != null)
			session.setAttribute(searchType + "SeachParamAuthor", author);
		if (isbn != null)
			session.setAttribute(searchType + "SeachParamIsbn", isbn);
		if (publisher != null)
			session.setAttribute(searchType + "SeachParamPublisher", publisher);

		if ((title == null) && !(session.getAttribute(searchType + "SeachParamTitle") == null))
			title = (String) session.getAttribute(searchType + "SeachParamTitle");
		if ((bookId == null) && !(session.getAttribute(searchType + "SeachParamId") == null))
			bookId = (String) session.getAttribute(searchType + "SeachParamId");
		if ((author == null) && !(session.getAttribute(searchType + "SeachParamAuthor") == null))
			author = (String) session.getAttribute(searchType + "SeachParamAuthor");
		if ((publisher == null) && !(session.getAttribute(searchType + "SeachParamPublisher") == null))
			publisher = (String) session.getAttribute(searchType + "SeachParamPublisher");
		if ((isbn == null) && !(session.getAttribute(searchType + "SeachParamIsbn") == null))
			isbn = (String) session.getAttribute(searchType + "SeachParamIsbn");

		String[] searchBookParameters = { "", "", "", "", "", "" };
		searchBookParameters[0] = (title == null) ? "" : title.trim();
		searchBookParameters[1] = (author == null) ? "" : author.trim();
		searchBookParameters[2] = (publisher == null) ? "" : publisher.trim();
		searchBookParameters[3] = (isbn == null) ? "" : isbn.trim();
		searchBookParameters[4] = "";
		searchBookParameters[5] = (bookId == null) ? "" : bookId.trim();

		for (int i = 0; i < searchBookParameters.length; i++) {
			if (forbiddenWords.findForbiddenWords(searchBookParameters[i])) {
				searchBookParameters[i] = "";
			}
		}

		return searchBookParameters;
	}

	@Override
	public long generateShowLessLinkValue(Integer startResult) {
		if ((startResult - 10) < 0) {
			return 0;
		} else {
			return startResult - 10;
		}
	}

	@Override
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue) {
		if ((startResult + 10) > amountOfResults) {
			return "Wyniki od " + (startResult + 1) + " do " + amountOfResults;
		} else {
			return "Wyniki od " + (startResult + 1) + " do " + showMoreLinkValue;
		}
	}

	@Override
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults) {
		if ((startResult + 10) > amountOfResults) {
			return startResult;
		} else {
			return startResult + 10;
		}
	}

	@Override
	public boolean hasTableAnyParameters(String[] searchBookParameters) {

		boolean hasAnyParameters = false;
		for (int i = 0; i < searchBookParameters.length; i++) {
			if (searchBookParameters[i] != "")
				hasAnyParameters = true;
		}
		return hasAnyParameters;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String addBookToList(HttpSession session, int bookId) {

		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		Book theBook = bookDAO.getBook(bookId);
		boolean isAllreadyOnTheList = false;

		for (Book tempBook : tempBookList) {
			if (tempBook.getId() == theBook.getId())
				isAllreadyOnTheList = true;
		}

		if (isAllreadyOnTheList)
			return "Ksi¹¿ka ju¿ znajduje siê na liœcie. ";
		else {
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return "Ksi¹¿ka zosta³a dodana do listy. ";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String addReservedBookToList(HttpSession session, int reservationId) {

		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		Reservation reservation = reservationService.getReservation(reservationId);
		Book theBook = reservation.getBook();

		boolean isAllreadyOnTheList = false;
		for (Book tempBook : tempBookList) {
			if (tempBook.getId() == theBook.getId())
				isAllreadyOnTheList = true;
		}

		if (isAllreadyOnTheList) {
			return "Ksi¹¿ka ju¿ znajduje siê na liœcie";
		} else {
			reservationService.deleteReservationInOrderToCreateBorrowedBook(reservation);
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return "Ksi¹¿ka zosta³a dodana do listy";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String borrowBooks(HttpSession session) {

		User tempUser = userService.getUser(Integer.valueOf((String) session.getAttribute("selectedUserId")));
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");
		StringBuilder sb = new StringBuilder();

		sb.append(tempUser.getId());
		sb.append(" ");

		for (Book tempBook : tempBookList) {
			borrowBook(tempBook, tempUser);
			sb.append(tempBook.getId());
			sb.append(" ");
		}

		return sb.toString();
	}

	@Override
	public void clearBookSearchParameters(HttpSession session) {

		session.setAttribute("bookSeachParamTitle", null);
		session.setAttribute("bookSeachParamId", null);
		session.setAttribute("bookSeachParamAuthor", null);
		session.setAttribute("bookSeachParamPublisher", null);
		session.setAttribute("bookSeachParamIsbn", null);
		session.setAttribute("bookSeachParamIsAvailable", null);

	}

	@Override
	public String prepareHqlUsingBookSearchParameters(String[] searchBookParameters, String searchType) {
		// 0 - title, 1 - author, 2 - publisher, 3 - isbn, 4- isAvailable
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append(searchType);
		if (!searchBookParameters[0].equals("")) {
			sb.append("title like '%" + searchBookParameters[0] + "%'");
			isContent = true;
		}
		if (!searchBookParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("author like '%" + searchBookParameters[1] + "%'");
			} else {
				sb.append("author like '%" + searchBookParameters[1] + "%'");
				isContent = true;
			}
		}
		if (!searchBookParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("publisher like '%" + searchBookParameters[2] + "%'");
			} else {
				sb.append("publisher like '%" + searchBookParameters[2] + "%'");
				isContent = true;
			}
		}
		if (!searchBookParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isbn like '%" + searchBookParameters[3] + "%'");
			} else {
				sb.append("isbn like '%" + searchBookParameters[3] + "%'");
				isContent = true;
			}
		}
		if (searchBookParameters[4].equals("true")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isAvailable=true");
			} else {
				sb.append("isAvailable=true");
				isContent = true;
			}
		}
		if (!searchBookParameters[5].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("id like '%" + searchBookParameters[5] + "%'");
			} else {
				sb.append("id like '%" + searchBookParameters[5] + "%'");
				isContent = true;
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");

		return sb.toString();
	}
}
