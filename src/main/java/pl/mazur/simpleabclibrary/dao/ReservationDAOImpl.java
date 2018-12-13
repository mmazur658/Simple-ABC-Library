package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.Reservation;

/**
 * Repository class for performing database operations on Reservation objects.
 * 
 * @author Marcin Mazur
 *
 */
@Repository
public class ReservationDAOImpl implements ReservationDAO {

	/**
	 * The number of results to be returned
	 */
	private final int RESULT_LIMIT = 10;
	
	/**
	 * The SessionFactory interface
	 */
	private SessionFactory sessionFactory;

	/**
	 * Constructs a ReservationDAOImpl with the SessionFactory.
	 * 
	 * @param sessionFactory
	 *            The SessionFactory interface
	 */
	@Autowired
	public ReservationDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void createReservation(Reservation reservation) {
		currentSession().save(reservation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getListOfReservationByUserId(int userId) {

		List<Reservation> userReservationsList = new ArrayList<>();
		String hql = "from Reservation where user.id=:id and isActive=true ORDER BY id ASC";
		Query<Reservation> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		userReservationsList = theQuery.getResultList();

		return userReservationsList;
	}

	@Override
	public Reservation getReservationById(int reservationId) {
		return currentSession().get(Reservation.class, reservationId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getListOfAllActiveReservation(boolean isActive) {

		List<Reservation> reservationList = new ArrayList<>();
		String hql = "from Reservation where isActive=:isActive ORDER BY id ASC";
		Query<Reservation> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("isActive", isActive);
		reservationList = theQuery.getResultList();

		return reservationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getListOfAllReservation(Integer startResult) {

		List<Reservation> reservationList = new ArrayList<>();
		String hql = "from Reservation where isActive = true ORDER BY id ASC";
		Query<Reservation> theQuery = currentSession().createQuery(hql);
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(RESULT_LIMIT);
		reservationList = theQuery.getResultList();

		return reservationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getListOfReservationForGivenSearchParams(String hql, Integer startResult) {

		List<Reservation> reservationList = new ArrayList<>();
		try {
			Query<Reservation> theQuery = currentSession().createQuery(hql);
			theQuery.setFirstResult(startResult);
			theQuery.setMaxResults(RESULT_LIMIT);
			reservationList = theQuery.getResultList();
			return reservationList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateReservation(Reservation reservation) {
		currentSession().update(reservation);

	}

	@SuppressWarnings("unchecked")
	@Override
	public long getNumberOfReservationsForGivenHql(String hql) {

		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}
}