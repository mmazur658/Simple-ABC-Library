package pl.mazur.simpleabclibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.BookBorrowing;
import pl.mazur.simpleabclibrary.entity.Message;
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

	@Scheduled(fixedRate = 1000 * 60 * 60) // 1 hour
	public void twentyFourHoursToReturnBookReminder() {// 24h

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis;
		Message message;
		List<BookBorrowing> bookBorrowingList = bookService.getAllBookBorrowing();

		for (BookBorrowing bookBorrowing : bookBorrowingList) {
			expTimeMillis = bookBorrowing.getExpectedEndDate().getTime();

			if (expTimeMillis - (1000 * 60 * 60 * 24) < currentTimeMillis
					&& expTimeMillis - (1000 * 60 * 60 * 23) > currentTimeMillis) {

				message = new Message();
				message.setRecipient(bookBorrowing.getUser());
				message.setRecipientIsActive(true);
				message.setRecipientIsRead(false);
				message.setSender(userService.getUser(1));
				message.setSenderIsActive(false);
				message.setSenderIsRead(true);
				message.setStartDate(new Date());
				message.setSubject("Przypomnienie o zwrocie ksi¹¿ki: " + bookBorrowing.getBook().getTitle());
				message.setText("Pozosta³o mniej ni¿ 24h na zwrot ksi¹¿ki: " + bookBorrowing.getBook().getTitle()
						+ ". Prosimy o terminowy zwrot.");
				messageService.sendMessage(message);
			}
		}
	}

	@Scheduled(fixedRate = 1000 * 60 * 60) // 1 hour
	public void sixHoursToReturnBookReminder() { // 6h

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis;
		Message message;
		List<BookBorrowing> bookBorrowingList = bookService.getAllBookBorrowing();

		for (BookBorrowing bookBorrowing : bookBorrowingList) {
			expTimeMillis = bookBorrowing.getExpectedEndDate().getTime();

			if (expTimeMillis - (1000 * 60 * 60 * 6) < currentTimeMillis
					&& expTimeMillis - (1000 * 60 * 60 * 5) > currentTimeMillis) {

				message = new Message();
				message.setRecipient(bookBorrowing.getUser());
				message.setRecipientIsActive(true);
				message.setRecipientIsRead(false);
				message.setSender(userService.getUser(1));
				message.setSenderIsActive(false);
				message.setSenderIsRead(true);
				message.setStartDate(new Date());
				message.setSubject("Przypomnienie o zwrocie ksi¹¿ki: " + bookBorrowing.getBook().getTitle());
				message.setText("Pozosta³o mniej ni¿ 6h na zwrot ksi¹¿ki: " + bookBorrowing.getBook().getTitle()
						+ ". Prosimy o terminowy zwrot.");
				messageService.sendMessage(message);

			}
		}
	}

	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	public void expiredBookReminder() {

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis;
		Message message;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<BookBorrowing> bookBorrowingList = bookService.getAllBookBorrowing();

		for (BookBorrowing bookBorrowing : bookBorrowingList) {
			expTimeMillis = bookBorrowing.getExpectedEndDate().getTime();

			if (expTimeMillis < currentTimeMillis) {

				message = new Message();
				message.setRecipient(bookBorrowing.getUser());
				message.setRecipientIsActive(true);
				message.setRecipientIsRead(false);
				message.setSender(userService.getUser(1));
				message.setSenderIsActive(false);
				message.setSenderIsRead(true);
				message.setStartDate(new Date());
				message.setSubject("Przypomnienie o zwrocie ksi¹¿ki: " + bookBorrowing.getBook().getTitle());
				message.setText("Posiadasz ksi¹¿kê, która powinna zostaæ zwrócona do "
						+ sdf.format(bookBorrowing.getExpectedEndDate())
						+ ". Prosimy o mo¿liwie najszybszy zwrot ksi¹¿ki.");
				messageService.sendMessage(message);
			}
		}
	}
}
