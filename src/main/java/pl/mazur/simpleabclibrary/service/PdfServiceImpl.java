package pl.mazur.simpleabclibrary.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.pdf.BookLabelGenerator;
import pl.mazur.simpleabclibrary.utils.pdf.PdfDocumentGenerator;

@Service
public class PdfServiceImpl implements PdfService {

	@Autowired
	private BookLabelGenerator bookLabelGenerator;

	@Autowired
	private PdfDocumentGenerator pdfDocumentGenerator;

	@Override
	public File getBookLabel(Book tempBook, String userName) {
		return bookLabelGenerator.generateBookLabel(tempBook, userName);
	}

	@Override
	public File generateCreatedAccountConfirmation(User theUser) {
		return pdfDocumentGenerator.generateCreatedAccountConfirmation(theUser);
	}

	@Override
	public File generateBorrowedBookConfirmation(List<Book> bookList, User tempUser, Date expectedEndDate,
			String employeeName) {
		return pdfDocumentGenerator.generateBorrowedBookConfirmation(bookList, tempUser, expectedEndDate,
				employeeName);
	}

	@Override
	public File generateReturnBookConfirmation(User tempUser, String employeeName, List<Book> bookList) {
		return pdfDocumentGenerator.generateReturnBookConfirmation(tempUser, employeeName, bookList);
	}

}
