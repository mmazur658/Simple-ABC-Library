package pl.mazur.simpleabclibrary.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

}
