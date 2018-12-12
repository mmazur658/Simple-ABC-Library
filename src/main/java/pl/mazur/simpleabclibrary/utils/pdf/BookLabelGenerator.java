package pl.mazur.simpleabclibrary.utils.pdf;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BadElementException;

import pl.mazur.simpleabclibrary.entity.Book;

/**
 * Interface used to generate book labels.
 * 
 * @author Marcin Mazur
 *
 */
public interface BookLabelGenerator {

	/**
	 * Returns a book label as a PDF File for the given book and user's name.
	 * 
	 * @param theBook
	 *            The Book containing the book, which the label will be generated
	 *            for
	 * @param userName
	 *            The User containing the user who generate the label
	 * @return A File representing the PDF File with generated label
	 * @throws IOException
	 *             A IOException is thrown when the file can't be created
	 * @throws MalformedURLException
	 *             A MalformedURLException is thrown when URL is incorrect
	 * @throws BadElementException
	 *             A BadElementException is thrown when created element has
	 *             incorrect form.
	 */
	public File generateBookLabel(Book theBook, String userName)
			throws BadElementException, MalformedURLException, IOException;

}