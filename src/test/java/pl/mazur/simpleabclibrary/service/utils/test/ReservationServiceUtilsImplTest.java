package pl.mazur.simpleabclibrary.service.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.ReservationServiceUtilsImpl;

class ReservationServiceUtilsImplTest {

	private ReservationServiceUtilsImpl reservationServiceUtilsImpl = new ReservationServiceUtilsImpl();
	private Book tempBook;
	private User tempUser;

	@Test
	void shoudlCreateReservation() {
		tempBook = new Book();
		tempBook.setTitle("Mackbeth");

		tempUser = new User();
		tempUser.setFirstName("Marcin");

		Reservation expectedReservation = new Reservation();
		expectedReservation.setBook(tempBook);
		expectedReservation.setUser(tempUser);
		expectedReservation.setIsActive(true);

		Reservation reservation = reservationServiceUtilsImpl.createReservation(tempBook, tempUser);

		assertEquals(expectedReservation.getBook().getTitle(), reservation.getBook().getTitle());
		assertEquals(expectedReservation.getUser().getFirstName(), reservation.getUser().getFirstName());
		assertFalse(tempBook.getIsAvailable());
		assertNotNull(reservation.getStartDate());
		assertNotNull(reservation.getStatus());
		assertTrue(reservation.getIsActive());

	}

	@Test
	void shouldDeleteReservationAndSendMessage() {

		User tempUser = new User();
		tempUser.setEmail("mmazur@op.pl");

		User adminUser = new User();
		tempUser.setEmail("mmazur2@op.pl");

		Book tempBook = new Book();
		tempBook.setTitle("Macbeth");

		Reservation reservation = new Reservation();
		reservation.setUser(tempUser);
		reservation.setBook(tempBook);

		Message expectedMessage = new Message();
		expectedMessage.setRecipient(reservation.getUser());
		expectedMessage.setRecipientIsActive(true);
		expectedMessage.setRecipientIsRead(false);
		expectedMessage.setSender(adminUser);
		expectedMessage.setSenderIsActive(false);
		expectedMessage.setSenderIsRead(true);

		Message message = reservationServiceUtilsImpl.prepareReservationToDeleteAndCreateNewMessage(reservation,
				adminUser);

		assertEquals(expectedMessage.getRecipient().getEmail(), message.getRecipient().getEmail());
		assertEquals(expectedMessage.getRecipientIsRead(), message.getRecipientIsRead());
		assertEquals(expectedMessage.getSender().getEmail(), message.getSender().getEmail());
		assertEquals(expectedMessage.getSenderIsActive(), message.getSenderIsActive());
		assertEquals(expectedMessage.getSenderIsRead(), message.getSenderIsRead());
		assertNotNull(message.getText());
		assertNotNull(message.getSubject());
		assertNotNull(message.getStartDate());
		assertNotNull(reservation.getStatus());
		assertNotNull(reservation.getStatus());
		assertFalse(reservation.getIsActive());
	}

	@Test
	void shouldIncreaseExpirationDate() {

		Reservation expectedReservation = new Reservation();
		expectedReservation.setEndDate(new Date());

		Reservation reservation = new Reservation();
		reservation.setEndDate(expectedReservation.getEndDate());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expectedReservation.getEndDate());
		calendar.add(Calendar.DATE, 1); // 24h
		expectedReservation.setEndDate(calendar.getTime());

		reservationServiceUtilsImpl.increaseExpirationDate(reservation);

		assertEquals(expectedReservation.getEndDate(), reservation.getEndDate());

	}

}
