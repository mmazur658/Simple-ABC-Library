package pl.mazur.simpleabclibrary.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
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

	// miliseconds * seconds * minutes * hour * day

	@Scheduled(fixedRate = 1000 * 60 * 60) // 1h
	public void checkReservation() {

		List<Reservation> reservationList = reservationService.getAllReservation(true);

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis;
		Message message;

		for (Reservation reservation : reservationList) {

			expTimeMillis = reservation.getStartDate().getTime() + (1000 * 60 * 60 * 48);

			if (currentTimeMillis > expTimeMillis) {
				message = new Message();
				message.setRecipient(reservation.getUser());
				message.setRecipientIsActive(true);
				message.setRecipientIsRead(false);
				message.setSender(userService.getUser(1));
				message.setSenderIsActive(false);
				message.setSenderIsRead(true);
				message.setStartDate(new Date());
				message.setSubject("Rzerwacja ksi¹¿ki " + reservation.getBook().getTitle() + " zosta³a usuniêta");
				message.setText(
						"Z przykroœci¹ informuajemy, ¿e twoja rezerwacja na ksi¹¿kê " + reservation.getBook().getTitle()
								+ " zosta³a usuniêta ze wzglêdu na przekroczony termin odbioru");
				messageService.sendMessage(message);
				reservationService.deleteReservationDueToOutdated(reservation);

			}

		}

	}

}
