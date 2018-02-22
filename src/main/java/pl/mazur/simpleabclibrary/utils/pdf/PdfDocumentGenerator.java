package pl.mazur.simpleabclibrary.utils.pdf;

import java.io.File;
import java.util.Date;
import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;

public interface PdfDocumentGenerator {

	File generateCreatedAccountConfirmation(User theUser);

	File generateBookBorrowingConfirmation(List<Book> bookList, User tempUser, Date expectedEndDate,
			String employeeName);

	File generateReturnBookConfirmation(User tempUser, String employeeName, List<Book> bookList);

}
