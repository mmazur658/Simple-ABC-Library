package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;

/**
 * Repository class for performing database operations on User objects.
 * 
 * @author Marcin Mazur
 *
 */
@Repository
public class UserDAOImpl implements UserDAO {

	/**
	 * The number of results to be returned
	 */
	private final int RESULT_LIMIT = 10;
	
	/**
	 * The SessionFactory interface
	 */
	private SessionFactory sessionFactory;

	/**
	 * The PasswordUtils interface
	 */
	private PasswordUtils passwordUtils;

	/**
	 * Constructs a UserDAOImpl with the SessionFactory and PasswordUtils.
	 * 
	 * @param sessionFactory
	 *            The SessionFactory interface
	 * @param passwordUtils
	 *            The PasswordUtils interface
	 */
	@Autowired
	public UserDAOImpl(SessionFactory sessionFactory, PasswordUtils passwordUtils) {
		this.sessionFactory = sessionFactory;
		this.passwordUtils = passwordUtils;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void saveUser(User theUser) {
		currentSession().save(theUser);
	}

	@Override
	public User getUserById(int theId) {
		return currentSession().get(User.class, theId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getUserByEmail(String email) {

		String hql = "select id from User where email=:email";
		Query<Integer> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("email", email);
		int theId = (int) theQuery.getSingleResult();
		User tempUser = getUserById(theId);

		return tempUser;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEmailUnique(String email) {

		String hql = "select count(*) from User where email=:email";
		Query<Long> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("email", email);
		Long count = (Long) theQuery.uniqueResult();

		if (count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean isPasswordCorrect(String thePasswordFromForm, String theEncryptedPasswordFromDatabase) {
		return passwordUtils.isPasswordCorrect(thePasswordFromForm, theEncryptedPasswordFromDatabase);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEmailAndPasswordCorrect(String email, String thePasswordFromForm) {

		String hql = "select isActive from User where email=:email";
		Query<Boolean> theIsActiveQuery = currentSession().createQuery(hql);
		theIsActiveQuery.setParameter("email", email.trim());
		boolean isActive = (boolean) theIsActiveQuery.getSingleResult();

		if (isActive) {

			hql = "select password from User where email=:email";
			Query<String> theQuery = currentSession().createQuery(hql);
			theQuery.setParameter("email", email.trim());
			String theEncryptedPasswordFromDatabase = (String) theQuery.getSingleResult();
			return isPasswordCorrect(thePasswordFromForm, theEncryptedPasswordFromDatabase);

		} else {
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getListOfAllUsers(int startResult) {

		List<User> usersList = new ArrayList<>();
		String hql = "from User where isActive = true ORDER BY id ASC";
		Query<User> theQuery = currentSession().createQuery(hql);
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(RESULT_LIMIT);
		usersList = theQuery.getResultList();

		return usersList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getListOfUserForGivenSearchParams(String hql, int startResult) {

		List<User> userList = new ArrayList<>();

		try {
			Query<User> theQuery = currentSession().createQuery(hql);
			theQuery.setFirstResult(startResult);
			theQuery.setMaxResults(RESULT_LIMIT);
			userList = theQuery.getResultList();

			return userList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getNumberOfUsersForGivenHql(String hql) {

		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();
		
		return count;
	}

}
