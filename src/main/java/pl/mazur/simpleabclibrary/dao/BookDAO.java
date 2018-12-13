package pl.mazur.simpleabclibrary.dao;

import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;

/**
 * Interface for performing database operations on Message objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface BookDAO {

	/**
	 * Returns the list of the Book objects for given HQL and startResult. Returns
	 * all books if HQL is null or blank. The startResult specifies the range of the
	 * results.
	 * 
	 * @param startResult
	 *            The int containing the value that specifies the range of the
	 *            results.
	 * @param hql
	 *            The String containing the HQL Statement
	 * 
	 * @return A List&lt;Book&gt; representing the list of the Book objects.
	 */
	public List<Book> getListOfBooks(String hql, int startResult);

	/**
	 * Saves a Book object in the database
	 * 
	 * @param book
	 *            The Book object to be saved
	 */
	public void saveBook(Book book);

	/**
	 * Returns the Book object with given id.
	 * 
	 * @param bookId
	 *            The int containing the id of the Book
	 * @return A Book representing the Book with given id
	 */
	public Book getBookById(int bookId);

	/**
	 * Updates the Book object.
	 * 
	 * @param book
	 *            The Book containing the Book to be updated
	 */
	public void updateBook(Book book);

	/**
	 * Saves the BorrowedBook object in the database.
	 * 
	 * @param borrowedBook
	 *            The BorrowedBook containing the BorrowedBook object to be saved
	 */
	public void saveBorrowedBook(BorrowedBook borrowedBook);

	/**
	 * Returns the list of BorrowedBook objects for given userId
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @return The List of BorrowedBook objects
	 */
	public List<BorrowedBook> getListOfBorrowedBookByUserId(int userId);

	/**
	 * Returns the BorrowedBook object with given id.
	 * 
	 * @param borrowedBookId
	 *            The int containing the id of the BorrowedBook
	 * @return A BorrowedBook representing the BorrowedBook with given id
	 */
	public BorrowedBook getBorrowedBookById(int borrowedBookId);

	/**
	 * Returns the BorrowedBook object for given id of the book
	 * 
	 * @param bookId
	 *            The int containing the id of the Book
	 * @return A BorrowedBook representing the BorrowedBook associated with given
	 *         book id
	 */
	public BorrowedBook getBorrowedBookByBookId(int bookId);

	/**
	 * Returns BorrowedBook object for given Book id and User id
	 * 
	 * @param bookId
	 *            The int containing the id of the Book
	 * @param userId
	 *            The int containing the id of the User
	 * @return A BorrowedBook representing the BorrowedBook with given ids
	 */
	public BorrowedBook getBorrowedBookByBookIdAndUserId(int bookId, int userId);

	/**
	 * Returns the list of all BorrowedBook objects
	 * 
	 * @return A List&lt;BorrowedBook&gt; representing the list of BorrowedBook
	 *         objects
	 * 
	 */
	public List<BorrowedBook> getListOfAllBorrowedBook();

	/**
	 * Returns the number of books for given HQL statement
	 * 
	 * @param hql
	 *            The String containing the HQL to be executed
	 * @return A long representing the number of books
	 */
	public long getNumberOfBookForGivenHql(String hql);

}
