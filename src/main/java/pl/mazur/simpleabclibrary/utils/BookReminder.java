package pl.mazur.simpleabclibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.UserService;

/**
 * Utility class used to managing reminders.
 * 
 * @author Marcin Mazur
 *
 */
@Component
@EnableScheduling
public class BookReminder {

	private final long ONE_HOUR = (1000 * 60 * 60);
	private final long TWENTY_FOUR_HOURS = (1000 * 60 * 60 * 24);

	/**
	 * The BookServiceinterface
	 */
	private BookService bookService;

	/**
	 * The MessageService interface
	 */
	private MessageService messageService;

	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * Constructs a BookReminder with the BookService, MessageService and
	 * UserService.
	 * 
	 * @param bookService
	 *            The BookServiceinterface
	 * @param messageService
	 *            The MessageService interface
	 * @param userService
	 *            The UserService interface
	 */
	@Autowired
	public BookReminder(BookService bookService, MessageService messageService, UserService userService) {
		this.bookService = bookService;
		this.messageService = messageService;
		this.userService = userService;
	}

	/**
	 * Creates and saves a Message with given parameters.
	 * 
	 * @param theUser
	 *            The User containing the recipient
	 * @param bookTitle
	 *            The String containing the title of the book
	 * @param hourLimit
	 *            The int containing the number of hours
	 * @param expectedEndDate
	 *            The Date containing the expected end date
	 */
	public void createAndSendNewMessage(User theUser, String bookTitle, int hourLimit, Date expectedEndDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Message theMessage = new Message();

		theMessage.setRecipient(theUser);
		theMessage.setRecipientIsActive(true);
		theMessage.setRecipientIsRead(false);
		theMessage.setSender(userService.getUserById(1));
		theMessage.setSenderIsActive(false);
		theMessage.setSenderIsRead(true);
		theMessage.setStartDate(new Date());
		theMessage.setSubject("Przypomnienie o zwrocie książki: " + bookTitle);
		if (hourLimit == 0) {
			theMessage.setText("Posiadasz książkę, która powinna zostać zwrócona do " + sdf.format(expectedEndDate)
					+ ". Prosimy o możliwie najszybszy zwrot książki.");
		} else {
			theMessage.setText("Pozostało mniej niż " + hourLimit + "h na zwrot ksiażki: " + bookTitle
					+ ". Prosimy o terminowy zwrot.");
		}
		messageService.sendMessage(theMessage);
	}

	/**
	 * Returns TRUE if the expected end date plus given number of hours is smaller
	 * then present time and bigger then present time minus 1 hour.
	 * 
	 * @param getExpectedEndDate
	 *            The Date containing the expected end date
	 * @param hourLimit
	 *            The int containing the number of hours
	 * @return A boolean representing the result
	 */
	public boolean isBorrowedBookExpired(Date getExpectedEndDate, int hourLimit) {

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis = getExpectedEndDate.getTime();

		if (hourLimit == 0) {
			boolean isBorrowedBookExpired = (expTimeMillis < currentTimeMillis) ? true : false;
			return isBorrowedBookExpired;
		}

		boolean isBorrowedBookExpired = (expTimeMillis - (ONE_HOUR * hourLimit) < currentTimeMillis
				&& expTimeMillis - (ONE_HOUR * (hourLimit - 1)) > currentTimeMillis) ? true : false;

		return isBorrowedBookExpired;
	}

	/**
	 * Gets the list of all borrowed books and check if they should be returned in
	 * the next 24 hours.
	 */
	@Scheduled(fixedRate = ONE_HOUR)
	public void twentyFourHoursToReturnBookReminder() {

		// Get the list of all borrowed book
		List<BorrowedBook> borrowedBookList = bookService.getListOfAllBorrowedBooks();

		// Check if the expected end date is bigger then (present date + 24 hours)
		for (BorrowedBook borrowedBook : borrowedBookList) {
			if (isBorrowedBookExpired(borrowedBook.getExpectedEndDate(), 24))
				createAndSendNewMessage(borrowedBook.getUser(), borrowedBook.getBook().getTitle(), 24,
						borrowedBook.getExpectedEndDate());

		}
	}

	/**
	 * Gets the list of all borrowed books and check if they should be returned in
	 * the next 6 hours.
	 */
	@Scheduled(fixedRate = ONE_HOUR) // 1 hour
	public void sixHoursToReturnBookReminder() { // 6h

		// Get the list of all borrowed book
		List<BorrowedBook> borrowedBookList = bookService.getListOfAllBorrowedBooks();

		// Check if the expected end date is bigger then (present date + 6 hours)
		for (BorrowedBook borrowedBook : borrowedBookList) {
			if (isBorrowedBookExpired(borrowedBook.getExpectedEndDate(), 6)) {
				createAndSendNewMessage(borrowedBook.getUser(), borrowedBook.getBook().getTitle(), 6,
						borrowedBook.getExpectedEndDate());

			}
		}
	}

	/**
	 * Gets the list of all borrowed books and check if they should be returned
	 * immediately.
	 */
	@Scheduled(fixedRate = TWENTY_FOUR_HOURS)
	public void expiredBookReminder() {

		// Get the list of all borrowed book
		List<BorrowedBook> borrowedBookList = bookService.getListOfAllBorrowedBooks();

		// Check if the expected end date is bigger then present date
		for (BorrowedBook borrowedBook : borrowedBookList) {
			if (isBorrowedBookExpired(borrowedBook.getExpectedEndDate(), 0)) {
				createAndSendNewMessage(borrowedBook.getUser(), borrowedBook.getBook().getTitle(), 0,
						borrowedBook.getExpectedEndDate());
			}
		}
	}
}