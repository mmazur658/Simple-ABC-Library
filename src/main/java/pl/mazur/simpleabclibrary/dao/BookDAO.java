package pl.mazur.simpleabclibrary.dao;

import java.util.Date;
import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BookBorrowing;

public interface BookDAO {

	public List<Book> getAllBooks(int startResult);

	public List<Book> bookSearchResult(String[] searchParameters, int startResult);

	public void saveBook(Book tempBook);

	public Book getBook(int bookId);

	public void setBookActive(Book book);

	public void updateBook(Book book);

	public void deleteBook(Book tempBook);

	public void borrowBook(Book tempBook, User tempUser);

	public List<BookBorrowing> getUserBookBorrowing(int userId);

	public void returnBook(Book book);

	public void closeBoorowingBook(Book book);

	public BookBorrowing getBookBorrowing(int bookBorrowingId);

	public Date getExpectedEndDate(User tempUser, Book book);

	public long getAmountOfSearchResult(String[] searchParameters);

	public long getAmountOfAllBooks();

	public List<BookBorrowing> getAllBookBorrowing();

	public void setBookActive(int reservationId);

}
