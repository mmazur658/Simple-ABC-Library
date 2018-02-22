package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.dao.ReservationDAO;
import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BookBorrowing;

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
		return bookDAO.bookSearchResult(searchParameters, startResult);
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
		bookDAO.updateBook(book);
	}

	@Override
	@Transactional
	public void deleteBook(Book tempBook) {
		bookDAO.deleteBook(tempBook);
	}

	@Override
	@Transactional
	public void borrowBook(Book tempBook, User tempUser) {
		bookDAO.borrowBook(tempBook, tempUser);
	}

	@Override
	@Transactional
	public List<BookBorrowing> getUserBookBorrowing(int userId) {
		return bookDAO.getUserBookBorrowing(userId);
	}

	@Override
	@Transactional
	public void returnBook(Book book) {
		bookDAO.returnBook(book);
	}

	@Override
	@Transactional
	public void closeBookBorrowing(Book book) {
		bookDAO.closeBoorowingBook(book);
	}

	@Override
	@Transactional
	public BookBorrowing getBookBorrowing(int bookBorrowingId) {
		return bookDAO.getBookBorrowing(bookBorrowingId);
	}

	@Override
	@Transactional
	public Date getExpectedEndDate(User tempUser, Book book) {
		return bookDAO.getExpectedEndDate(tempUser, book);
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] searchParameters) {
		return bookDAO.getAmountOfSearchResult(searchParameters);
	}

	@Override
	@Transactional
	public long getAmountOfAllBooks() {
		return bookDAO.getAmountOfAllBooks();
	}

	@Override
	@Transactional
	public List<BookBorrowing> getAllBookBorrowing() {
		return bookDAO.getAllBookBorrowing();
	}

}
