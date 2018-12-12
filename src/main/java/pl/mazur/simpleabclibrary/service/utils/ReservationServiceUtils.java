package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface used to perform operations for reservation and message service
 * classes.
 * 
 * @author Marcin Mazur
 *
 */
public interface ReservationServiceUtils {

	/**
	 * Creates and returns the Reservation with the given User and Book
	 * 
	 * @param tempBook
	 *            The Book containing the book to be reserved
	 * @param theUser
	 *            The User containing the user who reserved the book
	 * @return A Reservation representing the reservation
	 */
	Reservation createReservation(Book tempBook, User theUser);

	/**
	 * Changes the status of the reservation, then creates and returns the message
	 * informing that the reservation has been deleted.
	 * 
	 * @param reservation
	 *            The Reservation containing the reservation which status will be
	 *            changed
	 * @param adminUser
	 *            The User containing the user who changed the status of the
	 *            reservation
	 * @return A Message representing the message informing that the reservation has
	 *         been deleted
	 */
	Message prepareReservationToDeleteAndCreateNewMessage(Reservation reservation, User adminUser);

	/**
	 * Extends the expiration date by 24h.
	 * 
	 * @param reservation
	 *            The Reservation containing the reservation which expiration date
	 *            will be extended
	 */
	void increaseExpirationDate(Reservation reservation);

}
