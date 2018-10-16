package pl.mazur.simpleabclibrary.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;

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
	ForbiddenWords forbiddenWords;

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

	public String prepareHqlUsingReservationSearchParameters(String[] reservationSearchParameters, String searchType) {
		// 0-customerId, 1-firstName, 2-lastName, 3-Pesel, 4-bookId, 5-bookTitle
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append(searchType);
		if (!reservationSearchParameters[0].equals("")) {
			sb.append("user.id like '%" + reservationSearchParameters[0] + "%'");
			isContent = true;
		}
		if (!reservationSearchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("user.firstName like '%" + reservationSearchParameters[1] + "%'");
			} else {
				sb.append("user.firstName like '%" + reservationSearchParameters[1] + "%'");
				isContent = true;
			}
		}
		if (!reservationSearchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("user.lastName like '%" + reservationSearchParameters[2] + "%'");
			} else {
				sb.append("user.lastName like '%" + reservationSearchParameters[2] + "%'");
				isContent = true;
			}
		}
		if (!reservationSearchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("user.pesel like '%" + reservationSearchParameters[3] + "%'");
			} else {
				sb.append("user.pesel like '%" + reservationSearchParameters[3] + "%'");
				isContent = true;
			}
		}
		if (!reservationSearchParameters[4].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("book.id like '%" + reservationSearchParameters[4] + "%'");
			} else {
				sb.append("book.id like '%" + reservationSearchParameters[4] + "%'");
				isContent = true;
			}
		}
		if (!reservationSearchParameters[5].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("book.title like '%" + reservationSearchParameters[5] + "%'");
			} else {
				sb.append("book.title like '%" + reservationSearchParameters[5] + "%'");
				isContent = true;
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");
		return sb.toString();
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] reservationSearchParameters) {

		String hql = prepareHqlUsingReservationSearchParameters(reservationSearchParameters, "select count(*) from Reservation where ");
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
		
		String hql = prepareHqlUsingReservationSearchParameters(reservationSearchParameters, "from Reservation where ");
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
	public void clearReservationSearchParameters(HttpSession session) {
		session.setAttribute("customerId", null);
		session.setAttribute("customerFirstName", null);
		session.setAttribute("customerLastName", null);
		session.setAttribute("customerPesel", null);
		session.setAttribute("bookId", null);
		session.setAttribute("bookTitle", null);
		session.setAttribute("reservationStartResult", null);
		
	}

	@Override
	public String[] prepareTableToSearch(HttpSession session, String customerId, String customerFirstName,
			String customerLastName, String customerPesel, String bookId, String bookTitle,
			Integer reservationStartResult) {


		if (!(customerId == null))
			session.setAttribute("customerId", customerId);
		if (!(customerFirstName == null))
			session.setAttribute("customerFirstName", customerFirstName);
		if (!(customerLastName == null))
			session.setAttribute("customerLastName", customerLastName);
		if (!(customerPesel == null))
			session.setAttribute("customerPesel", customerPesel);
		if (!(bookId == null))
			session.setAttribute("bookId", bookId);
		if (!(bookTitle == null))
			session.setAttribute("bookTitle", bookTitle);

		if ((customerId == null) && !(session.getAttribute("customerId") == null))
			customerId = (String) session.getAttribute("customerId");
		if ((customerFirstName == null) && !(session.getAttribute("customerFirstName") == null))
			customerFirstName = (String) session.getAttribute("customerFirstName");
		if ((customerLastName == null) && !(session.getAttribute("customerLastName") == null))
			customerLastName = (String) session.getAttribute("customerLastName");
		if ((customerPesel == null) && !(session.getAttribute("customerPesel") == null))
			customerPesel = (String) session.getAttribute("customerPesel");
		if ((bookId == null) && !(session.getAttribute("bookId") == null))
			bookId = (String) session.getAttribute("bookId");
		if ((bookTitle == null) && !(session.getAttribute("bookTitle") == null))
			bookTitle = (String) session.getAttribute("bookTitle");

		String[] reservationSearchParameters = { "", "", "", "", "", "" };
		reservationSearchParameters[0] = (customerId == null) ? "" : customerId;
		reservationSearchParameters[1] = (customerFirstName == null) ? "" : customerFirstName;
		reservationSearchParameters[2] = (customerLastName == null) ? "" : customerLastName;
		reservationSearchParameters[3] = (customerPesel == null) ? "" : customerPesel;
		reservationSearchParameters[4] = (bookId == null) ? "" : bookId;
		reservationSearchParameters[5] = (bookTitle == null) ? "" : bookTitle;
		
		for (int i = 0; i < reservationSearchParameters.length; i++) {
			if (forbiddenWords.findForbiddenWords(reservationSearchParameters[i])) {
				reservationSearchParameters[i] = "";
			}
		}
		
		return reservationSearchParameters;

	}

	@Override
	public boolean hasTableAnyParameters(String[] reservationSearchParameters) {

		boolean hasAnyParameters = false;
		for (int i = 0; i < reservationSearchParameters.length; i++) {
			if (reservationSearchParameters[i] != "")
				hasAnyParameters = true;
		}
		return hasAnyParameters;
	}

	@Override
	public long generateShowLessLinkValue(Integer startResult) {
		if ((startResult - 10) < 0) {
			return 0;
		} else {
			return startResult - 10;
		}
	}

	@Override
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults) {
		if ((startResult + 10) > amountOfResults) {
			return startResult;
		} else {
			return startResult + 10;
		}
	}

	@Override
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue) {
		if ((startResult + 10) > amountOfResults) {
			return "Wyniki od " + (startResult + 1) + " do " + amountOfResults;
		} else {
			return "Wyniki od " + (startResult + 1) + " do " + showMoreLinkValue;
		}
	}
	
}
