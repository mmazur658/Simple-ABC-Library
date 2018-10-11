package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;

public interface BookService {

	public List<Book> getAllBooks(int startResult);

	public List<Book> bookSearchResult(String[] searchParameters, int startResult);

	public void saveBook(Book tempBook);

	public Book getBook(int bookId);

	public void updateBook(Book book);

	public void deleteBook(Book tempBook);

	public void borrowBook(Book tempBook, User tempUser);

	public List<BorrowedBook> getUserBorrowedBookList(int userId);

	public void returnBook(Book book);

	public void closeBorrowedBook(Book book);

	public BorrowedBook getBorrowedBook(int borrowedBookId);

	public Date getExpectedEndDate(User tempUser, Book book);

	public long getAmountOfSearchResult(String[] searchParameters);

	public long getAmountOfAllBooks();

	public List<BorrowedBook> getAllBorrowedBookList();

}
