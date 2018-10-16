package pl.mazur.simpleabclibrary.dao;

import java.util.Date;
import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;

public interface BookDAO {

	public List<Book> getAllBooks(int startResult);

	public List<Book> bookSearchResult(String hql, int startResult);

	public void saveBook(Book tempBook);

	public Book getBook(int bookId);

	public void setBookActive(Book book);

	public void updateBook(Book book);

	public void deleteBook(Book tempBook);

	public void borrowBook(BorrowedBook borrowedBook);

	public List<BorrowedBook> getUserBorrowedBookList(int userId);

	public void closeBorrowedBook(Book book);

	public BorrowedBook getBorrowedBook(int borrowedBookId);

	public Date getExpectedEndDate(User tempUser, Book book);

	public long getAmountOfSearchResult(String hql);

	public long getAmountOfAllBooks();

	public List<BorrowedBook> getAllBorrowedBookList();

	public void setBookActive(int reservationId);

}
