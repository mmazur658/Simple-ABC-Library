package pl.mazur.simpleabclibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.UserServiceUtils;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.PeselValidator;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PeselValidator peselValidator;

	@Autowired
	private AccessLevelControl loginAndAccessLevelCheck;

	@Autowired
	private SearchEngineUtils searchEngineUtils;
	
	@Autowired
	private UserServiceUtils userServiceUtils;

	@Override
	@Transactional
	public void saveUser(User theUser) {
		userServiceUtils.setAdditionalUserData(theUser);
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
		userServiceUtils.updateUserData(tempUser,theUser);
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
		String systemMessage = userServiceUtils.increaseUserAccessLevel(theUser);
		userDAO.updateUser(theUser);
		
		return systemMessage;
	}

	@Override
	@Transactional
	public String decreaseUserAccessLevel(Integer decreaseAccessLevelUserId) {

		User theUser = userDAO.getUser(decreaseAccessLevelUserId);
		String systemMessage = userServiceUtils.decreaseUserAccessLevel(theUser);
		userDAO.updateUser(theUser);
		
		return systemMessage;
	}

	@Override
	public String getUserAccessLevel(User theUser) {
		return loginAndAccessLevelCheck.getUserAccessLevel(theUser);
	}
}