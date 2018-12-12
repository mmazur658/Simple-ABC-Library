package pl.mazur.simpleabclibrary.dao;

import java.util.List;
import pl.mazur.simpleabclibrary.entity.Reservation;

/**
 * Interface for performing database operations on Reservation objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface ReservationDAO {

	/**
	 * Saves the Reservation in the database.
	 * 
	 * @param reservation
	 *            The Reservation containing the reservation to be saved
	 */
	void createReservation(Reservation reservation);

	/**
	 * Returns the list of the reservation for given user id.
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @return A List&lt;Reservation&gt; representing the list of reservations
	 */
	List<Reservation> getListOfReservationByUserId(int userId);

	/**
	 * Get the reservation with given id.
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 * @return A Reservation representing the Reservation with given id
	 */
	Reservation getReservationById(int reservationId);

	/**
	 * Returns the list of all reservation with given status.
	 * 
	 * @param isActive
	 *            The boolean containing the isActive status
	 * @return A List&lt;Reservation&gt; representing the list of reservations
	 */
	List<Reservation> getListOfAllActiveReservation(boolean isActive);

	/**
	 * Returns the list of all reservations.
	 * 
	 * @param startResult
	 *            The Integer containing the first index of the results
	 * @return A List&lt;Reservation&gt; representing the list of reservations
	 */
	List<Reservation> getListOfAllReservation(Integer startResult);

	/**
	 * Returns the list of reservations for given HQL Statement.
	 * 
	 * @param hql
	 *            The String containing the HQL to be executed
	 * @param startResult
	 *            The Integer containing the first index of the results
	 * @return A List&lt;Reservation&gt; representing the list of reservations
	 */
	List<Reservation> getListOfReservationForGivenSearchParams(String hql, Integer startResult);

	/**
	 * Updates the reservation.
	 * 
	 * @param reservation
	 *            The Reservation containing the Reservation to be updated
	 */
	void updateReservation(Reservation reservation);

}
