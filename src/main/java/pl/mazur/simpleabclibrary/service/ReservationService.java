package pl.mazur.simpleabclibrary.service;

import java.util.List;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Reservation;

/**
 * Interface for managing Reservation objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface ReservationService {

	/**
	 * Saves the Reservation in the database.
	 * 
	 * @param reservation
	 *            The Reservation to be saved
	 */
	public void createReservation(Reservation reservation);

	/**
	 * Returns the list of Reservation objects for given id of the user.
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @return A List&lt;Reservation&gt; representing the list of the results
	 */
	public List<Reservation> getListOfReservationByUserId(int userId);

	/**
	 * Changes the isActive status of the Reservation to FALSE and isActive status
	 * of the associated book to TRUE.
	 * 
	 * @param reservation
	 *            The Reservation containing the reservation which status will be
	 *            changed
	 */
	public void deleteReservationInOrderToCreateBorrowedBook(Reservation reservation);

	/**
	 * Returns the Reservation with the given id.
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 * @return A Reservation representing the Reservation with given id
	 */
	public Reservation getReservationById(int reservationId);

	/**
	 * Returns the list of Reservations with given isActive status.
	 * 
	 * @param isActive
	 *            The boolean containing the isActive status of the Reservation
	 * @return A List&lt;Reservation&gt; representing the list of reservations
	 */
	public List<Reservation> getListOfAllReservationsByReservationStatus(boolean isActive);

	/**
	 * Changes the isActive status of the Reservation to FALSE and isActive status
	 * of the associated book to TRUE.
	 * 
	 * @param reservation
	 *            The Reservation containing the reservation which status will be
	 *            changed
	 */
	public void deleteReservationDueToOutdated(Reservation reservation);

	/**
	 * Changes the expiration date of the reservation with given id
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 */
	public void increaseExpirationDate(int reservationId);

	/**
	 * Changes the isActive status of the Reservation to FALSE and isActive status
	 * of the associated book to TRUE.<br>
	 * Creates and saves the message informing the owner about deleting the
	 * reservation.
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 */
	public void deleteReservationByEmployee(int reservationId);

	/**
	 * Returns the list of the Reservations.
	 * 
	 * @param startResult
	 *            An Integer containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;&gt; representing the
	 */
	public List<Reservation> getListOfAllReservations(Integer startResult);

	/**
	 * Returns the list of the Reservations for given search parameters.
	 * 
	 * @param reservationSearchParameters
	 *            The String[] containing the search parameters
	 * @param startResult
	 *            An Integer containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;Reservation&gt; representing the
	 */
	public List<Reservation> getListOfReservationForGivenSearchParams(String[] reservationSearchParameters,
			Integer startResult);

	/**
	 * Changes the isActive status of the Reservation to FALSE and isActive status
	 * of the associated book to TRUE.
	 * 
	 * @param reservation
	 *            The Reservation containing the reservation which status will be
	 *            changed
	 */
	public void deleteReservationByUser(Reservation reservation);

	/**
	 * Creates and saves the Reservation object with the given parameters.<br>
	 * Changes the isActive status of the book to FALSE.
	 * 
	 * @param tempBook
	 *            The Book containing the book associated with the reservation
	 * @param userId
	 *            The int containing the id of the user associated with the
	 *            reservation
	 */
	public void createReservation(Book tempBook, int userId);

}
