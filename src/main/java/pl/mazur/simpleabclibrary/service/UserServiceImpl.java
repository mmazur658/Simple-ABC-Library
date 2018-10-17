package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	PeselValidator peselValidator;

	@Autowired
	PasswordUtils passwordUtils;

	@Autowired
	AccessLevelControl loginAndAccessLevelCheck;

	@Autowired
	SearchEngineUtils searchEngineUtils;

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

		userDAO.updateUser(tempUser);
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

		String searchType = "from User where ";
		String[] fieldsName = { "id", "firstName", "lastName", "email", "pesel" };
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(userSearchParameters, searchType, fieldsName);
		return userDAO.getUserSearchResult(hql, startResult);
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] userSearchParameters) {

		String searchType = "SELECT COUNT(*) FROM User where ";
		String[] fieldsName = { "id", "firstName", "lastName", "email", "pesel" };
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(userSearchParameters, searchType, fieldsName);
		return userDAO.getAmountOfSearchResult(hql);
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

}
