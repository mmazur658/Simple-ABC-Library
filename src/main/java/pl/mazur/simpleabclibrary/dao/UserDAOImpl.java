package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.Date;
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

	@Override
	public void saveUser(User theUser) {

		Session session = sessionFactory.getCurrentSession();

		theUser.setActive(true);
		theUser.setAdmin(false);
		theUser.setEmployee(false);
		theUser.setStartDate(new Date());
		theUser.setPassword(passwordUtils.encryptPassword(theUser.getPassword().trim()));

		session.save(theUser);
	}

	@Override
	public User getUser(int theId) {

		Session session = sessionFactory.getCurrentSession();

		User tempUser = session.get(User.class, theId);

		return tempUser;
	}

	@Override
	public User getUser(String email) {

		Session session = sessionFactory.getCurrentSession();

		String hql = "select id from User where email=:email";
		Query theQuery = session.createQuery(hql);
		theQuery.setParameter("email", email.trim());
		int theId = (int) theQuery.getSingleResult();
		User tempUser = getUser(theId);

		return tempUser;
	}

	@Override
	public boolean checkEmailIsExist(String email) {

		Session session = sessionFactory.getCurrentSession();

		String hql = "select count(*) from User where email=:email";
		Query theQuery = session.createQuery(hql);
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

		Session session = sessionFactory.getCurrentSession();

		String hql = "select isActive from User where email=:email";
		Query theQuery = session.createQuery(hql);
		theQuery.setParameter("email", email.trim());
		boolean isActive = (boolean) theQuery.getSingleResult();
		System.out.println("userDAO: " + isActive);

		if (isActive) {

			hql = "select password from User where email=:email";
			theQuery = session.createQuery(hql);
			theQuery.setParameter("email", email.trim());
			String theEncryptedPasswordFromDatabase = (String) theQuery.getSingleResult();
			return authenticatePassword(thePasswordFromForm, theEncryptedPasswordFromDatabase);

		} else {
			return false;
		}

	}

	@Override
	public void updateUser(User theUser) {

		Session session = sessionFactory.getCurrentSession();

		User tempUser = session.get(User.class, theUser.getId());
		tempUser.setFirstName(theUser.getFirstName());
		tempUser.setLastName(theUser.getLastName());
		tempUser.setEmail(theUser.getEmail());
		tempUser.setPesel(theUser.getPesel());
		tempUser.setStreet(theUser.getStreet());
		tempUser.setHouseNumber(theUser.getHouseNumber());
		tempUser.setCity(theUser.getCity());
		tempUser.setPostalCode(theUser.getPostalCode());
		tempUser.setSex(theUser.getSex());
		tempUser.setBirthday(theUser.getBirthday());

		session.update(tempUser);
	}

	@Override
	public void changePassword(int userId, String newPassword) {

		Session session = sessionFactory.getCurrentSession();

		User tempUser = session.get(User.class, userId);
		tempUser.setPassword(passwordUtils.encryptPassword(newPassword));

		session.update(tempUser);

	}

	@Override
	public List<User> getAllUsers(int startResult) {

		Session session = sessionFactory.getCurrentSession();

		List<User> usersList = new ArrayList<>();
		Query theQuery = session.createQuery("from User where isActive = true ORDER BY id ASC");
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(10);
		usersList = theQuery.getResultList();

		return usersList;
	}

	@Override
	public List<User> getUserSearchResult(String[] userSearchParameters, int startResult) {
		// 1 - userId, 2 - first name, 3 - last name, 4 - email, 5 - pesel,
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();
		List<User> userList = new ArrayList<>();

		sb.append("from User where ");

		if (!userSearchParameters[0].equals("")) {
			sb.append("id like '%" + userSearchParameters[0] + "%'");
			isContent = true;
		}
		if (!userSearchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("firstName like '%" + userSearchParameters[1] + "%'");
			} else {
				sb.append("firstName like '%" + userSearchParameters[1] + "%'");
				isContent = true;
			}
		}
		if (!userSearchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("lastName like '%" + userSearchParameters[2] + "%'");
			} else {
				sb.append("lastName like '%" + userSearchParameters[2] + "%'");
				isContent = true;
			}
		}
		if (!userSearchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("email like '%" + userSearchParameters[3] + "%'");
			} else {
				sb.append("email like '%" + userSearchParameters[3] + "%'");
				isContent = true;
			}
		}
		if (!userSearchParameters[4].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("pesel like '%" + userSearchParameters[4] + "%'");
			} else {
				sb.append("pesel like '%" + userSearchParameters[4] + "%'");
				isContent = true;
			}
		}
		if (isContent) {
			sb.append(" AND isActive = true ORDER BY id ASC");
		} else {
			sb.append("isAdmin=true");

		}

		String hql = sb.toString();

		try {
			Session session = sessionFactory.getCurrentSession();
			Query theQuery = session.createQuery(hql);
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
	public long getAmountOfSearchResult(String[] userSearchParameters) {

		boolean isContent = false;
		StringBuilder sb = new StringBuilder();
		List<User> userList = new ArrayList<>();

		sb.append("SELECT COUNT(*) FROM User where ");

		if (!userSearchParameters[0].equals("")) {
			sb.append("id like '%" + userSearchParameters[0] + "%'");
			isContent = true;
		}
		if (!userSearchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("firstName like '%" + userSearchParameters[1] + "%'");
			} else {
				sb.append("firstName like '%" + userSearchParameters[1] + "%'");
				isContent = true;
			}
		}
		if (!userSearchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("lastName like '%" + userSearchParameters[2] + "%'");
			} else {
				sb.append("lastName like '%" + userSearchParameters[2] + "%'");
				isContent = true;
			}
		}
		if (!userSearchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("email like '%" + userSearchParameters[3] + "%'");
			} else {
				sb.append("email like '%" + userSearchParameters[3] + "%'");
				isContent = true;
			}
		}
		if (!userSearchParameters[4].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("pesel like '%" + userSearchParameters[4] + "%'");
			} else {
				sb.append("pesel like '%" + userSearchParameters[4] + "%'");
				isContent = true;
			}
		}

		if (isContent) {
			sb.append(" AND isActive = true ORDER BY id ASC");
		} else {
			sb.append("isAdmin=true");

		}

		Session session = sessionFactory.getCurrentSession();

		String hql = sb.toString();
		Query theQuery = session.createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllBooks() {

		Session session = sessionFactory.getCurrentSession();

		Query theQuery = session.createQuery("select count(*) from User WHERE isActive = true ORDER BY id ASC");
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public void increaseUserAccessLevel(User theUser) {

		Session session = sessionFactory.getCurrentSession();
		session.update(theUser);

	}

	@Override
	public void decreaseUserAccessLevel(User theUser) {

		Session session = sessionFactory.getCurrentSession();
		session.update(theUser);

	}

}
