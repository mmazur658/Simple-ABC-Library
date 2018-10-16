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

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PasswordUtils passwordUtils;

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void saveUser(User theUser) {
		currentSession().save(theUser);
	}

	@Override
	public User getUser(int theId) {
		return currentSession().get(User.class, theId);
	}

	@Override
	public User getUser(String email) {

		String hql = "select id from User where email=:email";
		Query theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("email", email);
		int theId = (int) theQuery.getSingleResult();
		User tempUser = getUser(theId);

		return tempUser;
	}

	@Override
	public boolean checkEmailIsExist(String email) {

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
	public boolean authenticatePassword(String thePasswordFromForm, String theEncryptedPasswordFromDatabase) {
		return passwordUtils.checkPassword(thePasswordFromForm, theEncryptedPasswordFromDatabase);
	}

	@Override
	public boolean verificationAndAuthentication(String email, String thePasswordFromForm) {

		String hql = "select isActive from User where email=:email";
		Query theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("email", email.trim());
		boolean isActive = (boolean) theQuery.getSingleResult();
		System.out.println("userDAO: " + isActive);

		if (isActive) {

			hql = "select password from User where email=:email";
			theQuery = currentSession().createQuery(hql);
			theQuery.setParameter("email", email.trim());
			String theEncryptedPasswordFromDatabase = (String) theQuery.getSingleResult();
			return authenticatePassword(thePasswordFromForm, theEncryptedPasswordFromDatabase);

		} else {
			return false;
		}

	}

	@Override
	public void updateUser(User theUser) {
		currentSession().update(theUser);
	}

	@Override
	public void changePassword(int userId, String newPassword) {

		User tempUser = currentSession().get(User.class, userId);
		tempUser.setPassword(passwordUtils.encryptPassword(newPassword));

		currentSession().update(tempUser);
	}

	@Override
	public List<User> getAllUsers(int startResult) {

		List<User> usersList = new ArrayList<>();
		String hql = "from User where isActive = true ORDER BY id ASC";
		Query theQuery = currentSession().createQuery(hql);
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(10);
		usersList = theQuery.getResultList();

		return usersList;
	}

	@Override
	public List<User> getUserSearchResult(String hql, int startResult) {

		List<User> userList = new ArrayList<>();

		try {
			Query<User> theQuery = currentSession().createQuery(hql);
			theQuery.setFirstResult(startResult);
			theQuery.setMaxResults(10);
			userList = theQuery.getResultList();

			return userList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getAmountOfSearchResult(String hql) {

		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllBooks() {

		String hql = "select count(*) from User WHERE isActive = true ORDER BY id ASC";
		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public void increaseUserAccessLevel(User theUser) {
		currentSession().update(theUser);
	}

	@Override
	public void decreaseUserAccessLevel(User theUser) {
		currentSession().update(theUser);
	}

}
