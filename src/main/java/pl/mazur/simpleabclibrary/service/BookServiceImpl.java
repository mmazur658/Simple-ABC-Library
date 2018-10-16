package pl.mazur.simpleabclibrary.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.dao.ReservationDAO;
import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookDAO bookDAO;

	@Autowired
	ReservationDAO reservationDAO;

	@Override
	@Transactional
	public List<Book> getAllBooks(int startResult) {
		return bookDAO.getAllBooks(startResult);
	}

	@Override
	@Transactional
	public List<Book> bookSearchResult(String[] searchParameters, int startResult) {

		// 0 - title, 1 - author, 2 - publisher, 3 - isbn, 5-id
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append("from Book where ");

		if (!searchParameters[0].equals("")) {
			sb.append("title like '%" + searchParameters[0] + "%'");
			isContent = true;
		}

		if (!searchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("author like '%" + searchParameters[1] + "%'");
			} else {
				sb.append("author like '%" + searchParameters[1] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("publisher like '%" + searchParameters[2] + "%'");
			} else {
				sb.append("publisher like '%" + searchParameters[2] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isbn like '%" + searchParameters[3] + "%'");
			} else {
				sb.append("isbn like '%" + searchParameters[3] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[5].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("id like '%" + searchParameters[5] + "%'");
			} else {
				sb.append("id like '%" + searchParameters[5] + "%'");
				isContent = true;
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");

		String hql = sb.toString();

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

		// 0 - title, 1 - author, 2 - publisher, 3 - isbn, 4- isAvailable
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append("select count(*) from Book where ");

		if (!searchParameters[0].equals("")) {
			sb.append("title like '%" + searchParameters[0] + "%'");
			isContent = true;
		}
		if (!searchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("author like '%" + searchParameters[1] + "%'");
			} else {
				sb.append("author like '%" + searchParameters[1] + "%'");
				isContent = true;
			}
		}
		if (!searchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("publisher like '%" + searchParameters[2] + "%'");
			} else {
				sb.append("publisher like '%" + searchParameters[2] + "%'");
				isContent = true;
			}
		}
		if (!searchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isbn like '%" + searchParameters[3] + "%'");
			} else {
				sb.append("isbn like '%" + searchParameters[3] + "%'");
				isContent = true;
			}
		}
		if (searchParameters[4].equals("true")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isAvailable=true");
			} else {
				sb.append("isAvailable=true");
				isContent = true;
			}
		}
		if (!searchParameters[5].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("id like '%" + searchParameters[5] + "%'");
			} else {
				sb.append("id like '%" + searchParameters[5] + "%'");
				isContent = true;
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");
		String hql = sb.toString();

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
	public void clearBookSearchParameters(HttpSession session) {
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
}
