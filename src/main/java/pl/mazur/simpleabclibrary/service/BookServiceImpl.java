package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.BookServiceUtils;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Service
@PropertySource("classpath:messages.properties")
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private Environment env;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserService userService;

	@Autowired
	private SearchEngineUtils searchEngineUtils;

	@Autowired
	private BookServiceUtils bookServiceUtils;

	@Override
	@Transactional
	public List<Book> getAllBooks(int startResult) {
		return bookDAO.getAllBooks(startResult);
	}

	@Override
	@Transactional
	public List<Book> bookSearchResult(String[] searchParameters, int startResult) {

		String searchType = "from Book where ";
		String[] fieldsName = { "title", "author", "publisher", "isbn", "id" };
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(searchParameters, searchType, fieldsName);

		return bookDAO.bookSearchResult(hql, startResult);
	}

	@Override
	@Transactional
	public void saveBook(Book tempBook) {

		bookServiceUtils.prepareBookToSave(tempBook);
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
		Book tempBook = getBook(book.getId());
		bookServiceUtils.prepareBookToUpdate(tempBook, book);

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

		BorrowedBook borrowedBook = bookServiceUtils.createBorrowedBook(tempBook, tempUser);
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

		String searchType = "select count(*) from Book where ";
		String[] fieldsName = { "title", "author", "publisher", "isbn", "id" };
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(searchParameters, searchType, fieldsName);

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
			book.setIsAvailable(true);
			returnBook(book);
			closeBorrowedBook(book);
			sb.append(book.getId());
			sb.append(" ");
		}
		return sb.toString();
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
			return env.getProperty("service.BookServiceImpl.addBookToList.error.1");
		else {
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return env.getProperty("service.BookServiceImpl.addBookToList.success.1");
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
			return env.getProperty("service.BookServiceImpl.addReservedBookToList.error.1");
		} else {
			reservationService.deleteReservationInOrderToCreateBorrowedBook(reservation);
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return env.getProperty("service.BookServiceImpl.addReservedBookToList.success.1");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
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

}
