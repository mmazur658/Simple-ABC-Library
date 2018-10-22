package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void createReservation(Reservation reservation) {
		currentSession().save(reservation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getUserReservations(int userId) {

		List<Reservation> userReservationsList = new ArrayList<>();
		String hql = "from Reservation where user.id=:id and isActive=true ORDER BY id ASC";
		Query<Reservation> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		userReservationsList = theQuery.getResultList();

		return userReservationsList;
	}

	@Override
	public Reservation getReservation(int reservationId) {
		return currentSession().get(Reservation.class, reservationId);
	}

	@Override
	public void deleteReservationInOrderToCreateBorrowedBook(Reservation reservation) {
		currentSession().update(reservation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getAllReservation(boolean isActive) {

		List<Reservation> reservationList = new ArrayList<>();
		String hql = "from Reservation where isActive=:isActive ORDER BY id ASC";
		Query<Reservation> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("isActive", isActive);
		reservationList = theQuery.getResultList();

		return reservationList;
	}

	@Override
	public void deleteReservationDueToOutdated(Reservation reservation) {
		currentSession().update(reservation);
	}

	@Override
	public void increaseExpirationDate(Reservation reservation) {
		currentSession().update(reservation);
	}

	@Override
	public void deleteReservationByEmployee(Reservation reservation) {
		currentSession().update(reservation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getAmountOfSearchResult(String hql) {
		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getAmountOfAllReservation() {

		String hql = "select count(*) from Reservation WHERE isActive = true ORDER BY id ASC";
		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getAllReservation(Integer startResult) {

		List<Reservation> reservationList = new ArrayList<>();
		String hql = "from Reservation where isActive = true ORDER BY id ASC";
		Query<Reservation> theQuery = currentSession().createQuery(hql);
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(10);
		reservationList = theQuery.getResultList();

		return reservationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> reservationSearchResult(String hql, Integer startResult) {

		List<Reservation> reservationList = new ArrayList<>();
		try {
			Query<Reservation> theQuery = currentSession().createQuery(hql);
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
		currentSession().update(reservation);
	}
}