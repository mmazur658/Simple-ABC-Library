package pl.mazur.simpleabclibrary.dao;

import java.util.List;
import pl.mazur.simpleabclibrary.entity.Reservation;

public interface ReservationDAO {

	void createReservation(Reservation reservation);

	List<Reservation> getUserReservations(int userId);

	Reservation getReservation(int reservationId);

	void deleteReservationInOrderToCreateBorrowedBook(Reservation reservation);

	List<Reservation> getAllReservation(boolean isActive);

	void deleteReservationDueToOutdated(Reservation reservation);

	void increaseExpirationDate(int reservationId);

	void deleteReservationByEmployee(int reservationId);

	long getAmountOfSearchResult(String[] reservationSearchParameters);

	long getAmountOfAllReservation();

	List<Reservation> getAllReservation(Integer startResult);

	List<Reservation> reservationSearchResult(String[] reservationSearchParameters, Integer startResult);

	void deleteReservationByUser(Reservation reservation);

}
