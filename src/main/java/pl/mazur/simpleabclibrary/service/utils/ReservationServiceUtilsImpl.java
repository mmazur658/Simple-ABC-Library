package pl.mazur.simpleabclibrary.service.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Utility class used to perform operations for reservation and message service classes.
 * 
 * @author Marcin Mazur
 *
 */
@Component
public class ReservationServiceUtilsImpl implements ReservationServiceUtils {
	
	private final int RESERVATION_DAYS_LIMIT = 2;
	private final int RESERVATION_CONTINUATION = 1;
	private final String BASIC_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

	@Override
	public Reservation createReservation(Book tempBook, User theUser) {

		Reservation reservation = new Reservation();
		reservation.setBook(tempBook);
		reservation.setUser(theUser);
		reservation.setIsActive(true);
		reservation.setStartDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getStartDate());
		calendar.add(Calendar.DATE, RESERVATION_DAYS_LIMIT);
		reservation.setEndDate(calendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat(BASIC_DATE_FORMAT);
		String date = sdf.format(calendar.getTime());
		reservation.setStatus("Rezerwacja ważna do " + date);
		tempBook.setIsAvailable(false);

		return reservation;
	}

	@Override
	public Message prepareReservationToDeleteAndCreateNewMessage(Reservation reservation, User adminUser) {

		reservation.setStatus("Rezerwacja usunięta przez pracownika biblioteki.");
		reservation.setIsActive(false);

		Message message = new Message();
		message.setRecipient(reservation.getUser());
		message.setRecipientIsActive(true);
		message.setRecipientIsRead(false);
		message.setSender(adminUser);
		message.setSenderIsActive(false);
		message.setSenderIsRead(true);
		message.setStartDate(new Date());
		message.setSubject(
				"Rezerwacja książki " + reservation.getBook().getTitle() + " została usunięta przez pracownika");
		message.setText("Z przykrością informujemy, że twoja rezerwacja książki " + reservation.getBook().getTitle()
				+ " została usunięta przez pracownika biblioteki.");

		return message;

	}

	@Override
	public void increaseExpirationDate(Reservation reservation) {
		SimpleDateFormat sdf = new SimpleDateFormat(BASIC_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getEndDate());
		calendar.add(Calendar.DATE, RESERVATION_CONTINUATION); 
		reservation.setEndDate(calendar.getTime());
		reservation.setStatus("Rezerwacja ważna do " + sdf.format(reservation.getEndDate()));
	}

}
