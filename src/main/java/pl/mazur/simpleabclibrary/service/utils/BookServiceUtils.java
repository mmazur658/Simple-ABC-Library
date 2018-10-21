package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.User;

public interface BookServiceUtils {

	BorrowedBook createBorrowedBook(Book tempBook, User tempUser);

	void prepareBookToUpdate(Book tempBook, Book book);

	void prepareBookToSave(Book tempBook);

}
