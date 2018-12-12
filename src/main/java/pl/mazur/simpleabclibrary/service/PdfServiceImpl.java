package pl.mazur.simpleabclibrary.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BadElementException;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.pdf.BookLabelGenerator;
import pl.mazur.simpleabclibrary.utils.pdf.PdfDocumentGenerator;

/**
 * Service class for creating and managing PDF files.
 * 
 * @author Marcin Mazur
 *
 */
@Service
public class PdfServiceImpl implements PdfService {

	/**
	 * The BookLabelGenerator interface
	 */
	private BookLabelGenerator bookLabelGenerator;

	/**
	 * The PdfDocumentGenerator interface
	 */
	private PdfDocumentGenerator pdfDocumentGenerator;

	/**
	 * Constructs a PdfServiceImpl with the BookLabelGenerator and
	 * PdfDocumentGenerator.
	 * 
	 * @param bookLabelGenerator
	 *            The BookLabelGenerator interface
	 * @param pdfDocumentGenerator
	 *            The PdfDocumentGenerator interface
	 */
	@Autowired
	public PdfServiceImpl(BookLabelGenerator bookLabelGenerator, PdfDocumentGenerator pdfDocumentGenerator) {
		this.bookLabelGenerator = bookLabelGenerator;
		this.pdfDocumentGenerator = pdfDocumentGenerator;
	}

	@Override
	public File getBookLabel(Book tempBook, String userName) throws BadElementException, MalformedURLException, IOException {
		return bookLabelGenerator.generateBookLabel(tempBook, userName);
	}

	@Override
	public File generateConfirmationForNewAccount(User theUser) {
		return pdfDocumentGenerator.generateCreatedAccountConfirmation(theUser);
	}

	@Override
	public File generateConfirmationForBorrowedBook(List<Book> bookList, User tempUser, Date expectedEndDate,
			String employeeName) {
		return pdfDocumentGenerator.generateBorrowedBookConfirmation(bookList, tempUser, expectedEndDate, employeeName);
	}

	@Override
	public File generateConfirmationForReturnBook(User tempUser, String employeeName, List<Book> bookList) {
		return pdfDocumentGenerator.generateReturnBookConfirmation(tempUser, employeeName, bookList);
	}

}
