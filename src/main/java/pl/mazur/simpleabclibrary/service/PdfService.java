package pl.mazur.simpleabclibrary.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;

public interface PdfService {

	File getBookLabel(Book tempBook, String userName);

	File generateCreatedAccountConfirmation(User theUser);

	File generateBorrowedBookConfirmation(List<Book> bookList, User tempUser, Date expectedEndDate,
			String employeeName);

	File generateReturnBookConfirmation(User tempUser, String employeeName, List<Book> bookList);

}
