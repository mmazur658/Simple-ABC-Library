package pl.mazur.simpleabclibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.dao.ReservationDAO;
import pl.mazur.simpleabclibrary.entity.Reservation;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	ReservationDAO reservationDAO;

	@Autowired
	BookDAO bookDAO;

	@Override
	@Transactional
	public void createReservation(Reservation reservation) {
		reservationDAO.createReservation(reservation);
	}

	@Override
	@Transactional
	public List<Reservation> getUserReservations(int userId) {
		return reservationDAO.getUserReservations(userId);
	}

	@Override
	@Transactional
	public void deleteReservationInOrderToCreateBorrowedBook(Reservation reservation) {
		reservationDAO.deleteReservationInOrderToCreateBorrowedBook(reservation);
		bookDAO.setBookActive(reservation.getBook());
	}

	@Override
	@Transactional
	public Reservation getReservation(int reservationId) {
		return reservationDAO.getReservation(reservationId);
	}

	@Override
	@Transactional
	public List<Reservation> getAllReservation(boolean isActive) {
		return reservationDAO.getAllReservation(isActive);
	}

	@Override
	@Transactional
	public void deleteReservationDueToOutdated(Reservation reservation) {
		reservationDAO.deleteReservationDueToOutdated(reservation);
		bookDAO.setBookActive(reservation.getBook());
	}

	@Override
	@Transactional
	public void increaseExpirationDate(int reservationId) {
		reservationDAO.increaseExpirationDate(reservationId);
	}

	@Override
	@Transactional
	public void deleteReservationByEmployee(int reservationId) {
		reservationDAO.deleteReservationByEmployee(reservationId);
		bookDAO.setBookActive(reservationId);
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] reservationSearchParameters) {
		return reservationDAO.getAmountOfSearchResult(reservationSearchParameters);
	}

	@Override
	@Transactional
	public long getAmountOfAllReservation() {
		return reservationDAO.getAmountOfAllReservation();
	}

	@Override
	@Transactional
	public List<Reservation> getAllReservation(Integer startResult) {
		return reservationDAO.getAllReservation(startResult);
	}

	@Override
	@Transactional
	public List<Reservation> reservationSearchResult(String[] reservationSearchParameters, Integer startResult) {
		return reservationDAO.reservationSearchResult(reservationSearchParameters, startResult);
	}

	@Override
	@Transactional
	public void deleteReservationByUser(Reservation reservation) {
		reservationDAO.deleteReservationByUser(reservation);
		bookDAO.setBookActive(reservation.getBook());
	}
}
