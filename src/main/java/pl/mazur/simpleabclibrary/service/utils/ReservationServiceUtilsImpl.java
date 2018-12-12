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

	@Override
	public Reservation createReservation(Book tempBook, User theUser) {

		Reservation reservation = new Reservation();
		reservation.setBook(tempBook);
		reservation.setUser(theUser);
		reservation.setIsActive(true);
		reservation.setStartDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getStartDate());
		calendar.add(Calendar.DATE, 2);
		reservation.setEndDate(calendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String date = sdf.format(calendar.getTime());
		reservation.setStatus("Rezerwacja wa¿na do " + date);
		tempBook.setIsAvailable(false);

		return reservation;
	}

	@Override
	public Message prepareReservationToDeleteAndCreateNewMessage(Reservation reservation, User adminUser) {

		reservation.setStatus("Rezerwacja usuniêta przez pracownika biblioteki.");
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
				"Rzerwacja ksi¹¿ki " + reservation.getBook().getTitle() + " zosta³a usuniêta przez pracownika");
		message.setText("Z przykroœci¹ informujemy, ¿e twoja rezerwacja ksi¹¿ki " + reservation.getBook().getTitle()
				+ " zosta³a usuniêta przez pracownika biblioteki.");

		return message;

	}

	@Override
	public void increaseExpirationDate(Reservation reservation) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getEndDate());
		calendar.add(Calendar.DATE, 1); // 24h
		reservation.setEndDate(calendar.getTime());
		reservation.setStatus("Rezerwacja wa¿na do " + sdf.format(reservation.getEndDate()));
	}

}
