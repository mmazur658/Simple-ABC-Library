package pl.mazur.simpleabclibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.dao.ReservationDAO;
import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.ReservationServiceUtils;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationDAO reservationDAO;

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SearchEngineUtils searchEngineUtils;

	@Autowired
	private ReservationServiceUtils reservationServiceUtils;

	@Override
	@Transactional
	public void createReservation(Reservation reservation) {

		Book book = reservation.getBook();
		book.setIsAvailable(false);
		bookDAO.updateBook(book);
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

		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja zmieniona na wydanie");

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

		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja po terminie");

		reservationDAO.deleteReservationDueToOutdated(reservation);
		bookDAO.setBookActive(reservation.getBook());
	}

	@Override
	@Transactional
	public void increaseExpirationDate(int reservationId) {

		Reservation reservation = reservationDAO.getReservation(reservationId);
		reservationServiceUtils.increaseExpirationDate(reservation);
		reservationDAO.increaseExpirationDate(reservation);
	}

	@Override
	@Transactional
	public void deleteReservationByEmployee(int reservationId) {

		Reservation reservation = reservationDAO.getReservation(reservationId);
		User adminUser = userDAO.getUser(1);
		reservationServiceUtils.deleteReservationByEmployee(reservation, adminUser);

		reservationDAO.deleteReservationByEmployee(reservation);
		bookDAO.setBookActive(reservationId);

	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] reservationSearchParameters) {

		String searchType = "select count(*) from Reservation where ";
		String[] fieldsName = { "user.id", "user.firstName", "user.lastName", "user.pesel", "book.id", "book.title" };
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(reservationSearchParameters, searchType,
				fieldsName);

		return reservationDAO.getAmountOfSearchResult(hql);
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

		String searchType = "from Reservation where ";
		String[] fieldsName = { "user.id", "user.firstName", "user.lastName", "user.pesel", "book.id,", "book.title" };
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(reservationSearchParameters, searchType,
				fieldsName);

		return reservationDAO.reservationSearchResult(hql, startResult);
	}

	@Override
	@Transactional
	public void deleteReservationByUser(Reservation reservation) {

		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja usuniêta przez u¿ytkownika.");

		reservationDAO.deleteReservationByUser(reservation);
		bookDAO.setBookActive(reservation.getBook());
	}

	@Override
	@Transactional
	public void createReservation(Book tempBook, int userId) {

		User theUser = userDAO.getUser(userId);
		Reservation reservation = reservationServiceUtils.createReservation(tempBook, theUser);

		bookDAO.updateBook(tempBook);
		reservationDAO.createReservation(reservation);
	}

}
