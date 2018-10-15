package pl.mazur.simpleabclibrary.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;

@Component
@EnableScheduling
public class ReservationControl {

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	@Autowired
	ReservationService reservationService;

	public Message createNewMessage(User theUser, String bookTitle) {

		Message theMessage = new Message();
		theMessage.setRecipient(theUser);
		theMessage.setRecipientIsActive(true);
		theMessage.setRecipientIsRead(false);
		theMessage.setSender(userService.getUser(1));
		theMessage.setSenderIsActive(false);
		theMessage.setSenderIsRead(true);
		theMessage.setStartDate(new Date());
		theMessage.setSubject("Rzerwacja ksi¹¿ki " + bookTitle.trim() + " zosta³a usuniêta");
		theMessage.setText("Z przykroœci¹ informuajemy, ¿e twoja rezerwacja na ksi¹¿kê " + bookTitle.trim()
				+ " zosta³a usuniêta ze wzglêdu na przekroczony termin odbioru");
		return theMessage;

	}

	public boolean isReservationExpired(Date reservationStartDate) {

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis = reservationStartDate.getTime() + (1000 * 60 * 60 * 48);
		if (currentTimeMillis > expTimeMillis)
			return true;
		else
			return false;
	}

	// miliseconds * seconds * minutes * hour * day
	@Scheduled(fixedRate = 1000 * 60 * 60) // 1h
	public void checkReservations() {

		List<Reservation> reservationList = reservationService.getAllReservation(true);
		Message message;

		for (Reservation reservation : reservationList) {

			if (isReservationExpired(reservation.getStartDate())) {

				message = createNewMessage(reservation.getUser(), reservation.getBook().getTitle());

				messageService.sendMessage(message);
				reservationService.deleteReservationDueToOutdated(reservation);

			}

		}

	}

}
