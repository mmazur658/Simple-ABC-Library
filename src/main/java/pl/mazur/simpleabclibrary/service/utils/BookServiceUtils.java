package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface used to perform operations for book service classes.
 * 
 * @author Marcin Mazur
 *
 */
public interface BookServiceUtils {

	/**
	 * Creates and returns the BorrowedBook object with given parameters.
	 * 
	 * @param tempBook
	 *            The Book containing the book associated with the BorrowedBook
	 * @param tempUser
	 *            The User containing the user associated with the BorrowedBook
	 * @return A BorrowedBook representing the BorrowedBook with given parameters
	 */
	BorrowedBook createBorrowedBook(Book tempBook, User tempUser);

	/**
	 * Updates the book with the given parameters
	 * 
	 * @param tempBook
	 *            The Book to be updated
	 * @param book
	 *            The Book containing the information using to update
	 */
	void prepareBookToUpdate(Book tempBook, Book book);

	/**
	 * Sets the new date of added and isActive and isAvailable status to TRUE
	 * 
	 * @param tempBook
	 *            The Book containing the book to be updated
	 */
	void prepareBookToSave(Book tempBook);

}
