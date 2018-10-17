package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

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

	public void addAllBorrowedBookToLiest(HttpSession session);

	public void addReturnedBookToList(HttpSession session, int bookId);

	public void deleteReturnedBookFromList(HttpSession session, int bookId);

	public String returnBooks(HttpSession session);

	public void deleteBookFromList(HttpSession session, int bookId);

	public String addBookToList(HttpSession session, int bookId);

	public String addReservedBookToList(HttpSession session, int reservationId);

	public String borrowBooks(HttpSession session);


}
