package pl.mazur.simpleabclibrary.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;

@Repository
public class ReservationDAOImpl implements ReservationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	BookDAO bookDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	MessageDAO messageDAO;

	@Override
	public void createReservation(Reservation reservation) {

		Session session = sessionFactory.getCurrentSession();
		
		session.save(reservation);
		Book book = reservation.getBook();
		book.setIsAvailable(false);
		
		session.update(book);

	}

	@Override
	public List<Reservation> getUserReservations(int userId) {

		Session session = sessionFactory.getCurrentSession();

		List<Reservation> userReservationsList = new ArrayList<>();
		Query theQuery = session.createQuery("from Reservation where user.id=:id and isActive=true ORDER BY id ASC");
		theQuery.setParameter("id", userId);
		userReservationsList = theQuery.getResultList();

		return userReservationsList;

	}

	@Override
	public Reservation getReservation(int reservationId) {

		Session session = sessionFactory.getCurrentSession();

		Reservation reservation = session.get(Reservation.class, reservationId);

		return reservation;
	}

	@Override
	public void deleteReservationInOrderToCreateBookBorrowing(Reservation reservation) {

		Session session = sessionFactory.getCurrentSession();
		
		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja zmieniona na wydanie");
		
		session.update(reservation);

	}

	@Override
	public List<Reservation> getAllReservation(boolean isActive) {

		Session session = sessionFactory.getCurrentSession();
		
		List<Reservation> reservationList = new ArrayList<>();
		Query theQuery = session.createQuery("from Reservation where isActive=:isActive ORDER BY id ASC");
		theQuery.setParameter("isActive", isActive);
		reservationList = theQuery.getResultList();

		return reservationList;
	}

	@Override
	public void deleteReservationDueToOutdated(Reservation reservation) {

		Session session = sessionFactory.getCurrentSession();
		
		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja po terminie");
		
		session.update(reservation);

	}

	@Override
	public void increaseExpirationDate(int reservationId) {

		Session session = sessionFactory.getCurrentSession();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Reservation reservation = session.get(Reservation.class, reservationId);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reservation.getEndDate());
		calendar.add(Calendar.DATE, 1); // 24h
		reservation.setEndDate(calendar.getTime());
		reservation.setStatus("Rezerwacja wa¿na do " + sdf.format(reservation.getEndDate()));
		
		session.update(reservation);

	}

	@Override
	public void deleteReservationByEmployee(int reservationId) {

		Session session = sessionFactory.getCurrentSession();
		
		Reservation reservation = session.get(Reservation.class, reservationId);
		reservation.setStatus("Rezerwacja usuniêta przez pracownika biblioteki.");
		reservation.setIsActive(false);
		
		session.update(reservation);
		
		bookDAO.setBookActive(reservation.getBook());
		
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
	public long getAmountOfSearchResult(String[] reservationSearchParameters) {
		// 0-customerId, 1-firstName, 2-lastName, 3-Pesel, 4-bookId, 5-bookTitle
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append("select count(*) from Reservation where ");

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
		String hql = sb.toString();
		Session session = sessionFactory.getCurrentSession();
		Query theQuery = session.createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllReservation() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query theQuery = session.createQuery("select count(*) from Reservation WHERE isActive = true ORDER BY id ASC");
		Long count = (Long) theQuery.uniqueResult();
		
		return count;
	}

	@Override
	public List<Reservation> getAllReservation(Integer startResult) {
		
		Session session = sessionFactory.getCurrentSession();

		List<Reservation> reservationList = new ArrayList<>();
		Query theQuery = session.createQuery("from Reservation where isActive = true ORDER BY id ASC");
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(10);
		reservationList = theQuery.getResultList();

		return reservationList;
	}

	@Override
	public List<Reservation> reservationSearchResult(String[] reservationSearchParameters, Integer startResult) {
		// 0-customerId, 1-firstName, 2-lastName, 3-Pesel, 4-bookId, 5-bookTitle

		boolean isContent = false;
		StringBuilder sb = new StringBuilder();
		List<Reservation> reservationList = new ArrayList<>();

		sb.append("from Reservation where ");

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
		String hql = sb.toString();

		try {
			Session session = sessionFactory.getCurrentSession();
			Query theQuery = session.createQuery(hql);
			theQuery.setFirstResult(startResult);
			theQuery.setMaxResults(10);
			reservationList = theQuery.getResultList();

			return reservationList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	@Override
	public void deleteReservationByUser(Reservation reservation) {

		Session session = sessionFactory.getCurrentSession();
		
		reservation.setIsActive(false);
		reservation.setStatus("Rezerwacja usuniêta przez u¿ytkownika.");
		
		session.update(reservation);	
	}
}
