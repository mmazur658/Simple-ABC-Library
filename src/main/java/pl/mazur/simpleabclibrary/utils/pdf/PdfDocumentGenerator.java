package pl.mazur.simpleabclibrary.utils.pdf;

import java.io.File;
import java.util.Date;
import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface used to generate PDF files containing confirmations.
 * 
 * @author Marcin
 *
 */
public interface PdfDocumentGenerator {

	/**
	 * Returns the confirmation that the account has been successfully created.
	 * 
	 * @param theUser
	 *            The User containing the User object
	 * @return A File representing the PDF File with confirmation
	 */
	File generateCreatedAccountConfirmation(User theUser);

	/**
	 * Returns the confirmation that the books have been successfully borrowed.
	 * 
	 * @param bookList
	 *            The List&lt;Book&gt; containing the list of borrowed books
	 * @param tempUser
	 *            The User containing the user who borrowed the books
	 * @param expectedEndDate
	 *            The Date containing the expected end date
	 * @param employeeName
	 *            The String containing the name of the employee who performed the
	 *            borrow book operation
	 * @return A File representing the PDF File with confirmation
	 */
	File generateBorrowedBookConfirmation(List<Book> bookList, User tempUser, Date expectedEndDate,
			String employeeName);

	/**
	 * Returns the confirmation that the books have been successfully returned.
	 * 
	 * @param tempUser
	 *            The User containing the user who returned the books
	 * @param employeeName
	 *            The String containing the name of the employee who performed the
	 *            return book operation
	 * @param bookList
	 *            The List&lt;Book&gt; containing the list of returned books
	 * @return A File representing the PDF File with confirmation
	 */
	File generateReturnBookConfirmation(User tempUser, String employeeName, List<Book> bookList);

}
