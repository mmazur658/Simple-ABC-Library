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
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.Reservation;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookDAO bookDAO;

	@Autowired
	ReservationService reservationService;

	@Autowired
	UserService userService;

	@Autowired
	SearchEngineUtils searchEngineUtils;

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
			return "Ksi��ka ju� znajduje si� na li�cie. ";
		else {
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return "Ksi��ka zosta�a dodana do listy. ";
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
			return "Ksi��ka ju� znajduje si� na li�cie";
		} else {
			reservationService.deleteReservationInOrderToCreateBorrowedBook(reservation);
			tempBookList.add(theBook);
			session.setAttribute("tempBookList", tempBookList);
			return "Ksi��ka zosta�a dodana do listy";
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
