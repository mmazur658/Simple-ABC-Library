package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.BookReminder;

class BookReminderTest {

	@Autowired
	private BookService bookService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	BookReminder bookReminder = new BookReminder(bookService, messageService, userService);

	@Test
	void shouldReturnTrueIfBorrowedBookIsExpiredTest1() {

		Calendar theCal = Calendar.getInstance();
		theCal.add(Calendar.DATE, 1);
		theCal.add(Calendar.MINUTE, -10);
		Date theDate = theCal.getTime();
		assertTrue(bookReminder.isBorrowedBookExpired(theDate, 24));
	}

	@Test
	void shouldReturnTrueIfBorrowedBookIsExpiredTest2() {

		Calendar theCal = Calendar.getInstance();
		theCal.add(Calendar.HOUR, 6);
		theCal.add(Calendar.MINUTE, -10);
		Date theDate = theCal.getTime();

		assertTrue(bookReminder.isBorrowedBookExpired(theDate, 6));
	}

	@Test
	void shouldReturnTrueIfBorrowedBookIsExpiredTest3() {

		Calendar theCal = Calendar.getInstance();
		theCal.add(Calendar.HOUR, -3);
		Date theDate = theCal.getTime();

		assertTrue(bookReminder.isBorrowedBookExpired(theDate, 0));
	}

	@Test
	void shouldReturnFalseIfBorrowedBookIsNotExpired() {

		Calendar theCal = Calendar.getInstance();
		theCal.add(Calendar.HOUR, 6);
		Date theDate = theCal.getTime();

		assertFalse(bookReminder.isBorrowedBookExpired(theDate, 0));

	}

}
