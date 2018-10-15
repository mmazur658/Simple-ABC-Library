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

@Component
@EnableScheduling
public class BookReminder {

	@Autowired
	BookService bookService;

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	public void createAndSendNewMessage(User theUser, String bookTitle, int hourLimit, Date expectedEndDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Message theMessage = new Message();

		theMessage.setRecipient(theUser);
		theMessage.setRecipientIsActive(true);
		theMessage.setRecipientIsRead(false);
		theMessage.setSender(userService.getUser(1));
		theMessage.setSenderIsActive(false);
		theMessage.setSenderIsRead(true);
		theMessage.setStartDate(new Date());
		theMessage.setSubject("Przypomnienie o zwrocie ksi¹¿ki: " + bookTitle);
		if (hourLimit == 0) {
			theMessage.setText("Posiadasz ksi¹¿kê, która powinna zostaæ zwrócona do " + sdf.format(expectedEndDate)
					+ ". Prosimy o mo¿liwie najszybszy zwrot ksi¹¿ki.");
		} else {
			theMessage.setText("Pozosta³o mniej ni¿ " + hourLimit + "h na zwrot ksi¹¿ki: " + bookTitle
					+ ". Prosimy o terminowy zwrot.");
		}
		messageService.sendMessage(theMessage);
	}

	public boolean isBorrowedBookExpired(Date getExpectedEndDate, int hourLimit) {

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis = getExpectedEndDate.getTime();

		if (hourLimit == 0) {
			if (expTimeMillis < currentTimeMillis)
				return true;
			else
				return false;
		}

		if (expTimeMillis - (1000 * 60 * 60 * hourLimit) < currentTimeMillis
				&& expTimeMillis - (1000 * 60 * 60 * (hourLimit - 1)) > currentTimeMillis)
			return true;
		else
			return false;
	}

	@Scheduled(fixedRate = 1000 * 60 * 60) // 1 hour
	public void twentyFourHoursToReturnBookReminder() {// 24h

		List<BorrowedBook> borrowedBookList = bookService.getAllBorrowedBookList();

		for (BorrowedBook borrowedBook : borrowedBookList) {

			if (isBorrowedBookExpired(borrowedBook.getExpectedEndDate(), 24))
				createAndSendNewMessage(borrowedBook.getUser(), borrowedBook.getBook().getTitle(), 24,
						borrowedBook.getExpectedEndDate());

		}
	}

	@Scheduled(fixedRate = 1000 * 60 * 60) // 1 hour
	public void sixHoursToReturnBookReminder() { // 6h

		List<BorrowedBook> borrowedBookList = bookService.getAllBorrowedBookList();

		for (BorrowedBook borrowedBook : borrowedBookList) {

			if (isBorrowedBookExpired(borrowedBook.getExpectedEndDate(), 6)) {
				createAndSendNewMessage(borrowedBook.getUser(), borrowedBook.getBook().getTitle(), 6,
						borrowedBook.getExpectedEndDate());

			}
		}
	}

	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	public void expiredBookReminder() {

		List<BorrowedBook> borrowedBookList = bookService.getAllBorrowedBookList();

		for (BorrowedBook borrowedBook : borrowedBookList) {
			if (isBorrowedBookExpired(borrowedBook.getExpectedEndDate(), 0)) {
				createAndSendNewMessage(borrowedBook.getUser(), borrowedBook.getBook().getTitle(), 0,
						borrowedBook.getExpectedEndDate());
			}
		}
	}
}