package pl.mazur.simpleabclibrary.service;

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
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.ReservationServiceUtils;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

/**
 * Service Class for managing Reservation objects.
 * 
 * @author Marcin Mazur
 *
 */
@Service
public class ReservationServiceImpl implements ReservationService {

	/**
	 * The array containing the names of reservation fields
	 */
	private final String[] NAMES_OF_RESERVATION_FIELDS = { "user.id", "user.firstName", "user.lastName", "user.pesel",
			"book.id", "book.title" };

	/**
	 * The ReservationDAO interface
	 */
	private ReservationDAO reservationDAO;

	/**
	 * The BookDAO interface
	 */
	private BookDAO bookDAO;

	/**
	 * The UserDAO interface
	 */
	private UserDAO userDAO;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * The ReservationServiceUtils interface
	 */
	private ReservationServiceUtils reservationServiceUtils;

	/**
	 * The MessageDAO interface
	 */
	private MessageDAO messageDAO;

	/**
	 * Constructs a ReservationServiceImpl with the ReservationDAO, BookDAO,
	 * UserDAO, SearchEngineUtils, ReservationServiceUtils and MessageDAO.
	 * 
	 * @param reservationDAO
	 *            The ReservationDAO interface
	 * @param bookDAO
	 *            The BookDAO interface
	 * @param userDAO
	 *            The UserDAO interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 * @param reservationServiceUtils
	 *            The ReservationServiceUtils interface
	 * @param messageDAO
	 *            The MessageDAO interface
	 */
	@Autowired
	public ReservationServiceImpl(ReservationDAO reservationDAO, BookDAO bookDAO, UserDAO userDAO,
			SearchEngineUtils searchEngineUtils, ReservationServiceUtils reservationServiceUtils,
			MessageDAO messageDAO) {

		this.reservationDAO = reservationDAO;
		this.bookDAO = bookDAO;
		this.userDAO = userDAO;
		this.searchEngineUtils = searchEngineUtils;
		this.reservationServiceUtils = reservationServiceUtils;
		this.messageDAO = messageDAO;
	}

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
	public List<Reservation> getListOfReservationByUserId(int userId) {
		return reservationDAO.getListOfReservationByUserId(userId);
	}

	@Override
	@Transactional
	public void deleteReservationInOrderToCreateBorrowedBook(Reservation reservation) {

		// Change the status of reservation, then update
		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja zmieniona na wydanie");
		reservationDAO.updateReservation(reservation);

		// Get the Book from the reservation and change its status, then update
		reservation.getBook().setIsAvailable(true);
		bookDAO.updateBook(reservation.getBook());
	}

	@Override
	@Transactional
	public Reservation getReservationById(int reservationId) {
		return reservationDAO.getReservationById(reservationId);
	}

	@Override
	@Transactional
	public List<Reservation> getListOfAllReservationsByReservationStatus(boolean isActive) {
		return reservationDAO.getListOfAllActiveReservation(isActive);
	}

	@Override
	@Transactional
	public void deleteReservationDueToOutdated(Reservation reservation) {

		// Change the status of reservation, then update
		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja po terminie");
		reservationDAO.updateReservation(reservation);

		// Get the Book from the reservation and change its status, then update
		reservation.getBook().setIsAvailable(true);
		bookDAO.updateBook(reservation.getBook());

	}

	@Override
	@Transactional
	public void increaseExpirationDate(int reservationId) {

		Reservation reservation = reservationDAO.getReservationById(reservationId);
		reservationServiceUtils.increaseExpirationDate(reservation);

	}

	@Override
	@Transactional
	public void deleteReservationByEmployee(int reservationId) {

		// Get the Reservation by given id
		Reservation reservation = reservationDAO.getReservationById(reservationId);

		// Get the MASTER USER
		User adminUser = userDAO.getUserById(1);

		// Create and save a Message informing the user that the reservation has been
		// deleted
		Message message = reservationServiceUtils.prepareReservationToDeleteAndCreateNewMessage(reservation, adminUser);
		messageDAO.sendMessage(message);

		// Get the Book from the reservation and change its status, then update
		reservation.getBook().setIsAvailable(true);
		bookDAO.updateBook(reservation.getBook());

	}

	@Override
	@Transactional
	public List<Reservation> getListOfAllReservations(Integer startResult) {
		return reservationDAO.getListOfAllReservation(startResult);
	}

	@Override
	@Transactional
	public List<Reservation> getListOfReservationForGivenSearchParams(String[] reservationSearchParameters,
			Integer startResult) {

		String searchType = "from Reservation where ";

		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(reservationSearchParameters, searchType,
				NAMES_OF_RESERVATION_FIELDS);

		return reservationDAO.getListOfReservationForGivenSearchParams(hql, startResult);
	}

	@Override
	@Transactional
	public void deleteReservationByUser(Reservation reservation) {

		// Change the status of reservation, then update
		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja usuniêta przez u¿ytkownika.");
		reservationDAO.updateReservation(reservation);

		// Get the Book from the reservation and change its status, then update
		reservation.getBook().setIsAvailable(true);
		bookDAO.updateBook(reservation.getBook());

	}

	@Override
	@Transactional
	public void createReservation(Book tempBook, int userId) {

		User theUser = userDAO.getUserById(userId);
		Reservation reservation = reservationServiceUtils.createReservation(tempBook, theUser);

		bookDAO.updateBook(tempBook);
		reservationDAO.createReservation(reservation);
	}

	@Override
	public long getNumberOfReservationsForGivenSearchParams(String[] searchParameters) {

		String searchType = "SELECT COUNT(*) FROM Reservation WHERE ";
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(searchParameters, searchType,
				NAMES_OF_RESERVATION_FIELDS);

		return reservationDAO.getNumberOfReservationsForGivenHql(hql);
	}

	@Override
	public long getNumberOfAllReservations() {

		String hql = "SELECT COUNT(*) FROM Reservation";

		return reservationDAO.getNumberOfReservationsForGivenHql(hql);
	}

}
