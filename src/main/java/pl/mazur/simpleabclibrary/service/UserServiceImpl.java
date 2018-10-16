package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
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

	@Autowired
	private AccessLevelControl loginAndAccessLevelCheck;

	@Autowired
	ForbiddenWords forbiddenWords;

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
	public long getAmountOfAllUsers() {
		return userDAO.getAmountOfAllUsers();
	}

	@Override
	@Transactional
	public String increaseUserAccessLevel(Integer increaseAccessLevelUserId) {

		User theUser = userDAO.getUser(increaseAccessLevelUserId);
		String systemMessage;

		if (!theUser.isAdmin() && !theUser.isEmployee()) {
			theUser.setEmployee(true);
			systemMessage = "Zwiêkszono uprawnienia do poziomu: Pracownik";
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(true);
			systemMessage = "Zwiêkszono uprawnienia do poziomu: Administrator";
		} else
			systemMessage = "Nie mo¿na zwiêkszyæ uprawnieñ, osi¹gniêto maksymalny poziom";

		userDAO.updateUser(theUser);
		return systemMessage;
	}

	@Override
	@Transactional
	public String decreaseUserAccessLevel(Integer decreaseAccessLevelUserId) {

		User theUser = userDAO.getUser(decreaseAccessLevelUserId);
		String systemMessage;

		if (theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(false);
			systemMessage = "Zmniejszono uprawnienia do poziomu: Pracownik";
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setEmployee(false);
			systemMessage = "Zmniejszono uprawnienia do poziomu: Klient";
		} else
			systemMessage = "Nie mo¿na zmniejszyæ uprawnieñ, osi¹gniêto minimalny poziom";

		userDAO.updateUser(theUser);
		return systemMessage;
	}

	@Override
	public String getUserAccessLevel(User theUser) {
		return loginAndAccessLevelCheck.getUserAccessLevel(theUser);
	}

	@Override
	public String[] prepareTableToSearch(HttpSession session, String searchType, String userId, String userFirstName,
			String userLastName, String userEmail, String userPesel) {

		if (!(userId == null))
			session.setAttribute(searchType + "SelectedUserId", userId);
		if (!(userFirstName == null))
			session.setAttribute(searchType + "FirstName", userFirstName);
		if (!(userLastName == null))
			session.setAttribute(searchType + "LastName", userLastName);
		if (!(userEmail == null))
			session.setAttribute(searchType + "Email", userEmail);
		if (!(userPesel == null))
			session.setAttribute(searchType + "Pesel", userPesel);

		if ((userId == null) && !(session.getAttribute(searchType + "SelectedUserId") == null))
			userId = String.valueOf(session.getAttribute(searchType + "SelectedUserId"));
		if ((userFirstName == null) && !(session.getAttribute(searchType + "FirstName") == null))
			userFirstName = String.valueOf(session.getAttribute(searchType + "FirstName"));
		if ((userLastName == null) && !(session.getAttribute(searchType + "LastName") == null))
			userLastName = String.valueOf(session.getAttribute(searchType + "LastName"));
		if ((userEmail == null) && !(session.getAttribute(searchType + "Email") == null))
			userEmail = String.valueOf(session.getAttribute(searchType + "Email"));
		if ((userPesel == null) && !(session.getAttribute(searchType + "Pesel") == null))
			userPesel = String.valueOf(session.getAttribute(searchType + "Pesel"));

		String[] userSearchParameters = { "", "", "", "", "", "", "", "" };
		userSearchParameters[0] = (userId == null) ? "" : userId.trim();
		userSearchParameters[1] = (userFirstName == null) ? "" : userFirstName.trim();
		userSearchParameters[2] = (userLastName == null) ? "" : userLastName.trim();
		userSearchParameters[3] = (userEmail == null) ? "" : userEmail.trim();
		userSearchParameters[4] = (userPesel == null) ? "" : userPesel.trim();

		for (int i = 0; i < userSearchParameters.length; i++) {
			if (forbiddenWords.findForbiddenWords(userSearchParameters[i])) {
				userSearchParameters[i] = "";
			}
		}
		return userSearchParameters;
	}

	@Override
	public boolean hasTableAnyParameters(String[] userSearchParameters) {

		boolean hasAnyParameters = false;
		for (int i = 0; i < userSearchParameters.length; i++) {
			if (userSearchParameters[i] != "")
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

	@Override
	public void clearSearchParameters(HttpSession session, String searchType) {
		session.setAttribute(searchType + "StartResult", null);
		session.setAttribute(searchType + "SelectedUserId", null);
		session.setAttribute(searchType + "FirstName", null);
		session.setAttribute(searchType + "LastName", null);
		session.setAttribute(searchType + "Email", null);
		session.setAttribute(searchType + "Pesel", null);
	}
}
