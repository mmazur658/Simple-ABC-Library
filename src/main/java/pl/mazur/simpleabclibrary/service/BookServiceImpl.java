package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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

/**
 * Service class for managing Book and BorrowedBook objects.
 * 
 * @author Marcin Mazur
 *
 */
@Service
@PropertySource("classpath:systemMessages.properties")
public class BookServiceImpl implements BookService {

	/**
	 * The array containing the names of book fields
	 */
	private final String[] NAMES_OF_BOOK_FIELDS = { "title", "author", "publisher", "isbn", "id" };

	/**
	 * The BookDAO interface
	 */
	private BookDAO bookDAO;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The ReservationService interface
	 */
	private ReservationService reservationService;

	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * The BookServiceUtils interface
	 */
	private BookServiceUtils bookServiceUtils;

	/**
	 * Constructs a BookServiceImpl with the BookDAO, Environment,
	 * ReservationService, UserService, SearchEngineUtils and BookServiceUtils.
	 * 
	 * @param bookDAO
	 *            The BookDAO interface
	 * @param env
	 *            The Environment interface
	 * @param reservationService
	 *            The ReservationService interface
	 * @param userService
	 *            The UserService interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 * @param bookServiceUtils
	 *            The BookServiceUtils interface
	 */
	@Autowired
	public BookServiceImpl(BookDAO bookDAO, Environment env, ReservationService reservationService,
			UserService userService, SearchEngineUtils searchEngineUtils, BookServiceUtils bookServiceUtils) {

		this.bookDAO = bookDAO;
		this.env = env;
		this.reservationService = reservationService;
		this.userService = userService;
		this.searchEngineUtils = searchEngineUtils;
		this.bookServiceUtils = bookServiceUtils;

	}

	@Override
	@Transactional
	public List<Book> getListOfAllBooks(int startResult) {
		return bookDAO.getListOfBooks(null, startResult);
	}

	@Override
	@Transactional
	public List<Book> getListOfBooksForGivenSearchParams(String[] searchParameters, int startResult) {

		String searchType = "from Book where ";
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(searchParameters, searchType,
				NAMES_OF_BOOK_FIELDS);

		return bookDAO.getListOfBooks(hql, startResult);
	}

	@Override
	@Transactional
	public void saveBook(Book tempBook) {

		bookServiceUtils.prepareBookToSave(tempBook);
		bookDAO.saveBook(tempBook);
	}

	@Override
	@Transactional
	public Book getBookById(int bookId) {
		return bookDAO.getBookById(bookId);
	}

	@Override
	@Transactional
	public void updateBook(Book book) {
		Book tempBook = getBookById(book.getId());
		bookServiceUtils.prepareBookToUpdate(tempBook, book);

	}

	@Override
	@Transactional
	public void deleteBook(Book tempBook) {
		tempBook.setIsActive(false);
		bookDAO.updateBook(tempBook);
	}

	@Override
	@Transactional
	public void borrowBook(Book tempBook, User tempUser) {

		BorrowedBook borrowedBook = bookServiceUtils.createBorrowedBook(tempBook, tempUser);
		bookDAO.updateBook(tempBook);
		bookDAO.saveBorrowedBook(borrowedBook);
	}

	@Override
	@Transactional
	public List<BorrowedBook> getListOfBorrowedBooksByUserId(int userId) {
		return bookDAO.getListOfBorrowedBookByUserId(userId);
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

		BorrowedBook borrowedBook = bookDAO.getBorrowedBookByBookId(book.getId());
		borrowedBook.setStopDate(new Date());

	}

	@Override
	@Transactional
	public BorrowedBook getBorrowedBookById(int borrowedBookId) {
		return bookDAO.getBorrowedBookById(borrowedBookId);
	}

	@Override
	@Transactional
	public Date getExpectedEndDate(User user, Book book) {

		BorrowedBook borrowedBook = bookDAO.getBorrowedBookByBookIdAndUserId(book.getId(), user.getId());
		return borrowedBook.getExpectedEndDate();
	}

	@Override
	@Transactional
	public List<BorrowedBook> getListOfAllBorrowedBooks() {
		return bookDAO.getListOfAllBorrowedBook();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAllBorrowedBookToList(HttpSession session) {

		// Get the lists from the session
		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		// Add all books from userBorrowedBooksList to tempReturnedBookList, then clear
		// userBorrowedBooksList
		for (int index = 0; index < userBorrowedBooksList.size(); index++) {
			tempReturnedBookList.add(userBorrowedBooksList.get(index).getBook());
		}
		userBorrowedBooksList.clear();

		// Set the new lists as a session attribute
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void addReturnedBookToList(HttpSession session, int bookId) {

		// Get the lists from the session
		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		// Add a book to tempReturnedBookList and remove it form userBorrowedBooksList.
		for (int index = 0; index < userBorrowedBooksList.size(); index++) {
			if (userBorrowedBooksList.get(index).getBook().getId() == bookId) {
				tempReturnedBookList.add(userBorrowedBooksList.get(index).getBook());
				userBorrowedBooksList.remove(index);
				break;
			}
		}

		// Set the new lists as a session attribute
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void deleteReturnedBookFromList(HttpSession session, int bookId) {

		// Get the lists from the session
		List<BorrowedBook> userBorrowedBooksList = (List<BorrowedBook>) session.getAttribute("userBorrowedBooksList");
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		// Find the book with given id and remove it from the tempReturnedBookList
		for (int index = 0; index < tempReturnedBookList.size(); index++) {
			if (tempReturnedBookList.get(index).getId() == bookId) {
				tempReturnedBookList.remove(index);
				userBorrowedBooksList.add(bookDAO.getBorrowedBookById(bookId));
				break;
			}
		}

		// Set the new lists as a session attribute
		session.setAttribute("tempReturnedBookList", tempReturnedBookList);
		session.setAttribute("userBorrowedBooksList", userBorrowedBooksList);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String returnBooks(HttpSession session) {

		// Get the list from the session
		List<Book> tempReturnedBookList = (List<Book>) session.getAttribute("tempReturnedBookList");

		// Build the message
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

		// Get the list from the session
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		// Find the book with given id and remove it from the tempBookList
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
	public String addBookToList(HttpSession session, int bookId, Locale locale) {

		// Get the list from the session
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		// Get the Book with given id and check if the book is already on the list
		Book theBook = bookDAO.getBookById(bookId);
		boolean isAllreadyOnTheList = false;

		for (Book tempBook : tempBookList) {
			if (tempBook.getId() == theBook.getId())
				isAllreadyOnTheList = true;
		}

		// Return a message informing that the book is already on the list.
		if (isAllreadyOnTheList)
			return env.getProperty(locale.getLanguage() + ".service.BookServiceImpl.addBookToList.error.1");

		// Add book to the list, set new lists as a session attribute and return a
		// message informing that the book has been added to the list.
		else {
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return env.getProperty(locale.getLanguage() + ".service.BookServiceImpl.addBookToList.success.1");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String addReservedBookToList(HttpSession session, int reservationId, Locale locale) {

		// Get the list from the session
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		// Get the Reservation
		Reservation reservation = reservationService.getReservationById(reservationId);

		// Get the Book with given id and check if the book is already on the list
		Book theBook = reservation.getBook();
		boolean isAllreadyOnTheList = false;
		for (Book tempBook : tempBookList) {
			if (tempBook.getId() == theBook.getId())
				isAllreadyOnTheList = true;
		}
		// Return a message informing that the book is already on the list
		if (isAllreadyOnTheList) {
			return env.getProperty(locale.getLanguage() + ".service.BookServiceImpl.addReservedBookToList.error.1");

			// Delete the Reservation, add book to the list, set new lists as a session
			// attribute and return a message informing that the book has been added to the
			// list.
		} else {
			reservationService.deleteReservationInOrderToCreateBorrowedBook(reservation);
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return env.getProperty(locale.getLanguage() + ".service.BookServiceImpl.addReservedBookToList.success.1");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String borrowBooks(HttpSession session) {

		// Get the list from the session
		List<Book> tempBookList = (List<Book>) session.getAttribute("tempBookList");

		// Get the user with given id and build a message
		User tempUser = userService.getUserById(Integer.valueOf((String) session.getAttribute("selectedUserId")));
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
	@Transactional
	public long getNumberOfBooksForGivenSearchParams(String[] searchBookParameters) {

		String searchType = "SELECT COUNT(*) FROM Book WHERE ";
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(searchBookParameters, searchType,
				NAMES_OF_BOOK_FIELDS);

		return bookDAO.getNumberOfBookForGivenHql(hql);
	}

	@Override
	@Transactional
	public long getNumberOfAllBooks() {

		String hql = "SELECT COUNT(*) FROM Book";

		return bookDAO.getNumberOfBookForGivenHql(hql);
	}

}
