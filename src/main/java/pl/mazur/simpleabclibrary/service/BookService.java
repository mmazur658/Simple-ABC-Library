package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface for managing Book and BorrowedBook objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface BookService {

	/**
	 * Returns the list of the books.
	 * 
	 * @param startResult
	 *            An int containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;Book&gt; representing the list of all books
	 */
	public List<Book> getListOfAllBooks(int startResult);

	/**
	 * Returns the list of the books for given search parameters.
	 * 
	 * @param searchParameters
	 *            The String[] containing the search parameters
	 * @param startResult
	 *            An int containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;Book&gt; representing the list of books
	 */
	public List<Book> getListOfBooksForGivenSearchParams(String[] searchParameters, int startResult);

	/**
	 * Saves a Book in the database.
	 * 
	 * @param book
	 *            The Book to be saved
	 */
	public void saveBook(Book book);

	/**
	 * Returns a Book with given id
	 * 
	 * @param bookId
	 *            The int containing the id of the book
	 * @return A Book representing the book with given id
	 */
	public Book getBookById(int bookId);

	/**
	 * Updates the book
	 * 
	 * @param book
	 *            The Book to be updated
	 */
	public void updateBook(Book book);

	/**
	 * Deletes the book
	 * 
	 * @param book
	 *            The Book to be deleted
	 */
	public void deleteBook(Book book);

	/**
	 * Saves a BorrowBook object with the given Book and User.
	 * 
	 * @param book
	 *            The Book containing the book to be borrowed
	 * @param user
	 *            The User containing the user who borrows the books
	 */
	public void borrowBook(Book book, User user);

	/**
	 * Returns the list of BorrowedBook object for given id of the user.
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @return A List&lt;BorrowedBook&gt; representing the list of BorrowedBook
	 *         object for given id of the user.
	 */
	public List<BorrowedBook> getListOfBorrowedBooksByUserId(int userId);

	/**
	 * Changes the isAvailable status of the Book to TRUE.
	 * 
	 * @param book
	 *            The Book containing the book which status will be changed
	 */
	public void returnBook(Book book);

	/**
	 * Sets the stopDate of the BorrowedBook object with given book.
	 * 
	 * @param book
	 *            The Book containing the book which stopDate will be set
	 */
	public void closeBorrowedBook(Book book);

	/**
	 * Returns the BorrowedBook with given id.
	 * 
	 * @param borrowedBookId
	 *            The int containing the id of the BorrowedBook
	 * @return A BorrowedBook representing the BorrowedBook with given id
	 */
	public BorrowedBook getBorrowedBookById(int borrowedBookId);

	/**
	 * Returns the expected end date of the BorrowedBook for given parameters.
	 * 
	 * @param user
	 *            The User containing the user associated with the BorrowedBook
	 * @param book
	 *            The Book containing the book associated with the BorrowedBook
	 * @return A Date representing the expected end date of the BorrowedBook
	 */
	public Date getExpectedEndDate(User user, Book book);

	/**
	 * Returns the list of all BorrowedBook objects.
	 * 
	 * @return A List&lt;BorrowedBook&gt; representing the list of all BorrowedBook
	 *         objects.
	 */
	public List<BorrowedBook> getListOfAllBorrowedBooks();

	/**
	 * Adds all borrowed books to the list of books to return. <br>
	 * <br>
	 * The session contains the temporary list of the books to return and the list
	 * of borrowed books.
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * 
	 */
	public void addAllBorrowedBookToList(HttpSession session);

	/**
	 * Adds the book with the given id to the list of books to return. <br>
	 * <br>
	 * The session contains the temporary list of the books to return.
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @param bookId
	 *            The int containing the id of the book
	 */
	public void addReturnedBookToList(HttpSession session, int bookId);

	/**
	 * Removes the book with given id from the list of books to return. <br>
	 * <br>
	 * The session contains the temporary list of the books to return
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @param bookId
	 *            The int containing the id of the book
	 */
	public void deleteReturnedBookFromList(HttpSession session, int bookId);

	/**
	 * Changes the isAvailable status of the Book to TRUE. <br>
	 * Sets the stopDate of the BorrowedBook.<br>
	 * Returns a message as a String.<br>
	 * <br>
	 * The session contains the list of the books to return.
	 *
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @return A String representing the message
	 */
	public String returnBooks(HttpSession session);

	/**
	 * Deletes the book with given id form the list of books to borrow. <br>
	 * <br>
	 * The session contains the temporary list of books to borrow.
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @param bookId
	 *            The int containing the id of the book
	 */
	public void deleteBookFromList(HttpSession session, int bookId);

	/**
	 * Adds the book with given id to the list of books to borrow.<br>
	 * Returns the message as a String. <br>
	 * <br>
	 * The session contains the temporary list of the books to borrow.
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @param bookId
	 *            The int containing the id of the book
	 * @param locale
	 *            The Locale containing the user`s locale
	 * @return A String representing the message
	 */
	public String addBookToList(HttpSession session, int bookId, Locale locale);

	/**
	 * Adds the book with given id form the list of reservations to the list of
	 * books to borrow.<br>
	 * Returns the message as a String. <br>
	 * <br>
	 * The session contains the temporary list of the books to return.
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @param reservationId
	 *            The int containing the id of the reservation
	 * @param locale
	 *            The Locale containing the user`s locale
	 * @return A String representing the message
	 */
	public String addReservedBookToList(HttpSession session, int reservationId, Locale locale);

	/**
	 * Returns the confirmation message as a String. <br>
	 * <Br>
	 * The session contains the temporary list of the books using to create a
	 * message
	 * 
	 * @param session
	 *            The HttpSession containing the user`s session
	 * @return A String representing the message
	 */
	public String borrowBooks(HttpSession session);

	/**
	 * Returns the number of books for given search parameters
	 * 
	 * @param searchBookParameters
	 *            The String[] containing the search parameters
	 * @return A long representing the number of books
	 */
	public long getNumberOfBooksForGivenSearchParams(String[] searchBookParameters);

	/**
	 * Returns the number of all books.
	 * 
	 * @return A long representing the number of all books
	 */
	public long getNumberOfAllBooks();
}
