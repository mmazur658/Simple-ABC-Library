package pl.mazur.simpleabclibrary.service.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.User;

@Component
public class BookServiceUtilsImpl implements BookServiceUtils {

	@Override
	public BorrowedBook createBorrowedBook(Book tempBook, User tempUser) {

		BorrowedBook borrowedBook = new BorrowedBook();
		borrowedBook.setBook(tempBook);
		borrowedBook.setUser(tempUser);
		borrowedBook.setStartDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(borrowedBook.getStartDate());
		calendar.add(Calendar.DATE, 14);
		borrowedBook.setExpectedEndDate(calendar.getTime());
		tempBook.setIsAvailable(false);

		return borrowedBook;
	}

	@Override
	public void prepareBookToUpdate(Book tempBook, Book book) {
		tempBook.setAuthor(book.getAuthor());
		tempBook.setTitle(book.getTitle());
		tempBook.setIsbn(book.getIsbn());
		tempBook.setLanguage(book.getLanguage());
		tempBook.setPages(book.getPages());
		tempBook.setPublisher(book.getPublisher());

	}

	@Override
	public void prepareBookToSave(Book tempBook) {
		tempBook.setDateOfAdded(new Date());
		tempBook.setIsActive(true);
		tempBook.setIsAvailable(true);

	}

}
