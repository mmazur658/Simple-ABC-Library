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

/**
 * Utility class used to manage reservation reminders.
 * 
 * @author Marcin
 *
 */
@Component
@EnableScheduling
public class ReservationControl {
	
	private final long FOURTY_EIGHT_HOURS = (1000 * 60 * 60 * 48);
	private final long ONE_HOUR = (1000 * 60 * 60);

	/**
	 * The MessageService interface
	 */
	private MessageService messageService;

	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * The ReservationService interface
	 */
	private ReservationService reservationService;

	/**
	 * Constructs a ReservationControl with the MessageService, UserService and
	 * ReservationService.
	 * 
	 * @param messageService
	 *            The MessageService interface
	 * @param userService
	 *            The UserService interface
	 * @param reservationService
	 *            The ReservationService interface
	 */
	@Autowired
	public ReservationControl(MessageService messageService, UserService userService,
			ReservationService reservationService) {
		this.messageService = messageService;
		this.userService = userService;
		this.reservationService = reservationService;
	}

	/**
	 * Creates and returns the message with given parameters.
	 * 
	 * @param theUser
	 *            The User containing the recipient
	 * @param bookTitle
	 *            The String containing the title of the book
	 * @return A Message representing the message with given parameters
	 */
	public Message createNewMessage(User theUser, String bookTitle) {

		Message theMessage = new Message();
		theMessage.setRecipient(theUser);
		theMessage.setRecipientIsActive(true);
		theMessage.setRecipientIsRead(false);
		theMessage.setSender(userService.getUserById(1));
		theMessage.setSenderIsActive(false);
		theMessage.setSenderIsRead(true);
		theMessage.setStartDate(new Date());
		theMessage.setSubject("Rzerwacja książki " + bookTitle.trim() + " została usunięta");
		theMessage.setText("Z przykrością informujemy, że twoja rezerwacja na książkę " + bookTitle.trim()
				+ " została usunięta ze względu na przekroczony termin odbioru");
		return theMessage;

	}

	/**
	 * Returns TRUE if the start date of reservation + 48h is lower than present
	 * time.
	 * 
	 * @param reservationStartDate
	 *            The Date containing the reservation date of created
	 * @return A boolean representing the result
	 */
	public boolean isReservationExpired(Date reservationStartDate) {

		Long currentTimeMillis = System.currentTimeMillis();
		Long expTimeMillis = reservationStartDate.getTime() + FOURTY_EIGHT_HOURS;

		boolean isReservationExpired = (currentTimeMillis > expTimeMillis) ? true : false;
		return isReservationExpired;

	}

	/**
	 * Gets the list of all active reservations and check, if they are expired.
	 * 
	 */
	@Scheduled(fixedRate = ONE_HOUR)
	public void checkReservations() {

		// Get the list of all borrowed book
		List<Reservation> reservationList = reservationService.getListOfAllReservationsByReservationStatus(true);

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
