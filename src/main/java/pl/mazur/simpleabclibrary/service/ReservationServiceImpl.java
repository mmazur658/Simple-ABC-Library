package pl.mazur.simpleabclibrary.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.BookDAO;
import pl.mazur.simpleabclibrary.dao.MessageDAO;
import pl.mazur.simpleabclibrary.dao.ReservationDAO;
import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	ReservationDAO reservationDAO;

	@Autowired
	BookDAO bookDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	MessageDAO messageDAO;

	@Autowired
	SearchEngineUtils searchEngineUtils;

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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Reservation reservation = reservationDAO.getReservation(reservationId);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getEndDate());
		calendar.add(Calendar.DATE, 1); // 24h
		reservation.setEndDate(calendar.getTime());
		reservation.setStatus("Rezerwacja wa¿na do " + sdf.format(reservation.getEndDate()));

		reservationDAO.increaseExpirationDate(reservation);
	}

	@Override
	@Transactional
	public void deleteReservationByEmployee(int reservationId) {

		Reservation reservation = reservationDAO.getReservation(reservationId);
		reservation.setStatus("Rezerwacja usuniêta przez pracownika biblioteki.");
		reservation.setIsActive(false);

		reservationDAO.deleteReservationByEmployee(reservation);
		bookDAO.setBookActive(reservationId);

		Message message = new Message();
		message.setRecipient(reservation.getUser());
		message.setRecipientIsActive(true);
		message.setRecipientIsRead(false);
		message.setSender(userDAO.getUser(1));
		message.setSenderIsActive(false);
		message.setSenderIsRead(true);
		message.setStartDate(new Date());
		message.setSubject(
				"Rzerwacja ksi¹¿ki " + reservation.getBook().getTitle() + " zosta³a usuniêta przez pracownika");
		message.setText("Z przykroœci¹ informujemy, ¿e twoja rezerwacja ksi¹¿ki " + reservation.getBook().getTitle()
				+ " zosta³a usuniêta przez pracownika biblioteki.");

		messageDAO.sendMessage(message);
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

		Reservation reservation = new Reservation();
		reservation.setBook(tempBook);
		reservation.setUser(userDAO.getUser(userId));
		reservation.setIsActive(true);
		reservation.setStartDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getStartDate());
		calendar.add(Calendar.DATE, 2);
		reservation.setEndDate(calendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String date = sdf.format(calendar.getTime());
		reservation.setStatus("Rezerwacja wa¿na do " + date);
		tempBook.setIsAvailable(false);
		bookDAO.updateBook(tempBook);
		reservationDAO.createReservation(reservation);
	}

}
