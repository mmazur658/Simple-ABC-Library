package pl.mazur.simpleabclibrary.service.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.BookServiceUtilsImpl;

class BookServiceUtilsImplTest {

	private BookServiceUtilsImpl bookServiceUtilsImpl = new BookServiceUtilsImpl();
	private Book tempBook;
	private Book expectedBook;
	private User tempUser;
	private BorrowedBook expectedBorrowedBook;
	private BorrowedBook borrowedBook;

	@Test
	void shouldCreateBorrowedBook() {
		tempBook = new Book();
		tempBook.setTitle("Macbeth");

		tempUser = new User();
		tempUser.setFirstName("Marcin");

		expectedBorrowedBook = new BorrowedBook();
		expectedBorrowedBook.setBook(tempBook);
		expectedBorrowedBook.setUser(tempUser);

		borrowedBook = bookServiceUtilsImpl.createBorrowedBook(tempBook, tempUser);

		assertFalse(tempBook.getIsActive());
		assertNotNull(borrowedBook.getStartDate());
		assertNotNull(borrowedBook.getExpectedEndDate());
		assertEquals(expectedBorrowedBook.getBook().getTitle(), borrowedBook.getBook().getTitle());
		assertEquals(expectedBorrowedBook.getUser().getFirstName(), borrowedBook.getUser().getFirstName());

	}

	@Test
	void shouldPrepareBookToUpdate() {
		expectedBook = new Book();
		expectedBook.setAuthor("Jo Nesbo");
		expectedBook.setPages(295);
		expectedBook.setLanguage("PL");
		expectedBook.setTitle("Macbeth");
		expectedBook.setIsbn("56325689654");
		expectedBook.setPublisher("Wydawnictwo Dolnoœl¹skie");

		tempBook = new Book();
		bookServiceUtilsImpl.prepareBookToUpdate(tempBook, expectedBook);

		assertEquals(expectedBook.getAuthor(), tempBook.getAuthor());
		assertEquals(expectedBook.getPages(), tempBook.getPages());
		assertEquals(expectedBook.getLanguage(), tempBook.getLanguage());
		assertEquals(expectedBook.getTitle(), tempBook.getTitle());
		assertEquals(expectedBook.getIsbn(), tempBook.getIsbn());
		assertEquals(expectedBook.getPublisher(), tempBook.getPublisher());
	}

	@Test
	void shoudlPrepareBookToSave() {

		expectedBook = new Book();
		expectedBook.setIsActive(true);
		expectedBook.setIsAvailable(true);

		tempBook = new Book();
		bookServiceUtilsImpl.prepareBookToSave(tempBook);

		assertNotNull(tempBook.getDateOfAdded());
		assertEquals(expectedBook.getIsAvailable(), tempBook.getIsAvailable());
		assertEquals(expectedBook.getIsActive(), tempBook.getIsActive());

	}

}
