package pl.mazur.simpleabclibrary.service;

import java.util.List;

import javax.servlet.http.HttpSession;

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

	public void clearReservationSearchParameters(HttpSession session);

	public String[] prepareTableToSearch(HttpSession session, String customerId, String customerFirstName,
			String customerLastName, String customerPesel, String bookId, String bookTitle,
			Integer reservationStartResult);

	public boolean hasTableAnyParameters(String[] reservationSearchParameters);

	public long generateShowLessLinkValue(Integer startResult);

	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults);

	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue);
}
