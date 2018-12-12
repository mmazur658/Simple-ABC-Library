package pl.mazur.simpleabclibrary.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BadElementException;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface for creating and managing PDF files.
 * 
 * @author Marcin Mazur
 *
 */
public interface PdfService {

	/**
	 * Generates and returns the label of the book with given parameters.
	 * 
	 * @param tempBook
	 *            The Book containing the book to generate a label
	 * @param userName
	 *            The String containing the name of the user who generated the label
	 * @return A File representing the PDF File with label
	 * @throws IOException
	 *             A IOException is thrown when the file can't be created
	 * @throws MalformedURLException
	 *             A MalformedURLException is thrown when URL is incorrect
	 * @throws BadElementException
	 *             A BadElementException is thrown when created element has
	 *             incorrect form.
	 */
	File getBookLabel(Book tempBook, String userName) throws BadElementException, MalformedURLException, IOException;

	/**
	 * Generates and returns the confirmation after creating a new account.
	 * 
	 * @param theUser
	 *            The User containing the user
	 * @return A File representing the PDF File with confirmation
	 */
	File generateConfirmationForNewAccount(User theUser);

	/**
	 * Generates and returns the confirmation after borrowing the books.
	 * 
	 * @param bookList
	 *            The List&lt;Book&gt; containing the list of the borrowed books
	 * @param tempUser
	 *            The User containing the user who borrowed the books
	 * @param expectedEndDate
	 *            The Date containing the expected end date
	 * @param employeeName
	 *            The String containing the name of the employee
	 * @return A File representing the PDF File with confirmation
	 */
	File generateConfirmationForBorrowedBook(List<Book> bookList, User tempUser, Date expectedEndDate,
			String employeeName);

	/**
	 * Generates and returns the confirmation after returning the books.
	 * 
	 * @param tempUser
	 *            The User containing the user who returned the books
	 * @param employeeName
	 *            The String containing the name of the employee
	 * @param bookList
	 *            The List&lt;Book&gt; containing the list of the returned books
	 * @return A File representing the PDF File with confirmation
	 */
	File generateConfirmationForReturnBook(User tempUser, String employeeName, List<Book> bookList);

}
