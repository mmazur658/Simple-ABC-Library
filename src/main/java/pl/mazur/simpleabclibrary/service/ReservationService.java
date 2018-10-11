package pl.mazur.simpleabclibrary.service;

import java.util.List;

import pl.mazur.simpleabclibrary.entity.Reservation;

public interface ReservationService {

	public void createReservation(Reservation reservation);

	public List<Reservation> getUserReservations(int userId);

	public void deleteReservationInOrderToCreateBorrowedBook(Reservation reservation);

	public Reservation getReservation(int reservationId);

	public List<Reservation> getAllReservation(boolean isActive);

	public void deleteReservationDueToOutdated(Reservation reservation);

	public void increaseExpirationDate(int reservationId);

	public void deleteReservationByEmployee(int reservationId);

	public long getAmountOfSearchResult(String[] reservationSearchParameters);

	public long getAmountOfAllReservation();

	public List<Reservation> getAllReservation(Integer startResult);

	public List<Reservation> reservationSearchResult(String[] reservationSearchParameters, Integer startResult);

	public void deleteReservationByUser(Reservation reservation);
}
