package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PeselValidator peselValidator;

	@Autowired
	private PasswordUtils passwordUtils;

	@Override
	@Transactional
	public void saveUser(User theUser) {
		theUser.setActive(true);
		theUser.setAdmin(false);
		theUser.setEmployee(false);
		theUser.setStartDate(new Date());
		theUser.setPassword(passwordUtils.encryptPassword(theUser.getPassword().trim()));
		userDAO.saveUser(theUser);
	}

	@Override
	@Transactional
	public User getUser(int theId) {
		return userDAO.getUser(theId);
	}

	@Override
	@Transactional
	public boolean checkEmailIsExist(String email) {
		return userDAO.checkEmailIsExist(email);
	}

	@Override
	@Transactional
	public boolean verificationAndAuthentication(String email, String password) {
		return userDAO.verificationAndAuthentication(email, password);
	}

	@Override
	@Transactional
	public User getUser(String email) {
		return userDAO.getUser(email.trim());
	}

	@Override
	@Transactional
	public void updateUser(User theUser) {

		User tempUser = userDAO.getUser(theUser.getId());

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
		if (!theUser.getPesel().equals("")) {
			tempUser.setSex(peselValidator.getSex(theUser.getPesel()));
			tempUser.setBirthday(peselValidator.getBirthDate(theUser.getPesel()));
		}

		userDAO.updateUser(theUser);
	}

	@Override
	public boolean validatePesel(String pesel) {
		return peselValidator.validatePesel(pesel);
	}

	@Override
	@Transactional
	public void changePassword(int userId, String newPassword) {
		userDAO.changePassword(userId, newPassword);

	}

	@Override
	@Transactional
	public List<User> getAllUsers(int startResult) {
		return userDAO.getAllUsers(startResult);
	}

	@Override
	@Transactional
	public List<User> getUserSearchResult(String[] userSearchParameters, int startResult) {

		String hql = prepareHqlUsingUserSearchParameters(userSearchParameters, "from User where ");
		return userDAO.getUserSearchResult(hql, startResult);
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] userSearchParameters) {

		String hql = prepareHqlUsingUserSearchParameters(userSearchParameters, "SELECT COUNT(*) FROM User where ");
		return userDAO.getAmountOfSearchResult(hql);
	}

	public String prepareHqlUsingUserSearchParameters(String[] userSearchParameters, String searchType) {
		// 1 - userId, 2 - first name, 3 - last name, 4 - email, 5 - pesel,
		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append(searchType);

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

		return sb.toString();
	}

	@Override
	@Transactional
	public long getAmountOfAllBooks() {
		return userDAO.getAmountOfAllBooks();
	}

	@Override
	@Transactional
	public void increaseUserAccessLevel(User theUser) {
		userDAO.increaseUserAccessLevel(theUser);
	}

	@Override
	@Transactional
	public void decreaseUserAccessLevel(User theUser) {
		userDAO.decreaseUserAccessLevel(theUser);
	}

}
