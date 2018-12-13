package pl.mazur.simpleabclibrary.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.UserServiceUtils;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

/**
 * Service Class for managing User objects.
 * 
 * @author Marcin Mazur
 *
 */
@Service
public class UserServiceImpl implements UserService {

	/**
	 * The array containing the names of user fields
	 */
	private final String[] NAMES_OF_USER_FIELDS = { "id", "firstName", "lastName", "email", "pesel" };

	/**
	 * The UserDAO interface
	 */
	private UserDAO userDAO;

	/**
	 * The PeselValidator interface
	 */
	private PeselValidator peselValidator;

	/**
	 * The AccessLevelControl interface
	 */
	private AccessLevelControl accessLevelControl;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * The UserServiceUtils interface
	 */
	private UserServiceUtils userServiceUtils;

	/**
	 * The PasswordUtils interface
	 */
	private PasswordUtils passwordUtils;

	/**
	 * Constructs a UserServiceImpl with the UserDAO, PeselValidator,
	 * AccessLevelControl, SearchEngineUtils, UserServiceUtils and PasswordUtils.
	 * 
	 * @param userDAO
	 *            The UserDAO interface
	 * @param peselValidator
	 *            The PeselValidator interface
	 * @param accessLevelControl
	 *            The AccessLevelControl interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 * @param userServiceUtils
	 *            The UserServiceUtils interface
	 * @param passwordUtils
	 *            The PasswordUtils interface
	 */
	@Autowired
	public UserServiceImpl(UserDAO userDAO, PeselValidator peselValidator, AccessLevelControl accessLevelControl,
			SearchEngineUtils searchEngineUtils, UserServiceUtils userServiceUtils, PasswordUtils passwordUtils) {

		this.userDAO = userDAO;
		this.peselValidator = peselValidator;
		this.accessLevelControl = accessLevelControl;
		this.searchEngineUtils = searchEngineUtils;
		this.userServiceUtils = userServiceUtils;
		this.passwordUtils = passwordUtils;
	}

	@Override
	@Transactional
	public void saveUser(User theUser) {
		userServiceUtils.setAdditionalUserData(theUser);
		userDAO.saveUser(theUser);
	}

	@Override
	@Transactional
	public User getUserById(int theId) {
		return userDAO.getUserById(theId);
	}

	@Override
	@Transactional
	public boolean isEmailCorrect(String email) {
		return userDAO.isEmailUnique(email);
	}

	@Override
	@Transactional
	public boolean isEmailAndPasswordCorrect(String email, String password) {
		return userDAO.isEmailAndPasswordCorrect(email, password);
	}

	@Override
	@Transactional
	public User getUserByEmail(String email) {
		return userDAO.getUserByEmail(email.trim());
	}

	@Override
	@Transactional
	public void updateUser(User theUser) {
		User tempUser = userDAO.getUserById(theUser.getId());
		userServiceUtils.updateUserData(tempUser, theUser);

	}

	@Override
	public boolean isPeselCorrect(String pesel) {
		return peselValidator.validatePesel(pesel);
	}

	@Override
	@Transactional
	public void changePassword(int userId, String newPassword) {

		User tempUser = userDAO.getUserById(userId);
		tempUser.setPassword(passwordUtils.encryptPassword(newPassword));

	}

	@Override
	@Transactional
	public List<User> getListOfAllUsers(int startResult) {
		return userDAO.getListOfAllUsers(startResult);
	}

	@Override
	@Transactional
	public List<User> getListOfUserByGivenSearchParams(String[] userSearchParameters, int startResult) {

		String searchType = "from User where ";
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(userSearchParameters, searchType,
				NAMES_OF_USER_FIELDS);

		return userDAO.getListOfUserForGivenSearchParams(hql, startResult);
	}

	@Override
	@Transactional
	public String increaseUserAccessLevel(Integer increaseAccessLevelUserId, Locale locale) {

		User theUser = userDAO.getUserById(increaseAccessLevelUserId);
		String systemMessage = userServiceUtils.increaseUserAccessLevel(theUser, locale);

		return systemMessage;
	}

	@Override
	@Transactional
	public String decreaseUserAccessLevel(Integer decreaseAccessLevelUserId, Locale locale) {

		User theUser = userDAO.getUserById(decreaseAccessLevelUserId);
		String systemMessage = userServiceUtils.decreaseUserAccessLevel(theUser, locale);

		return systemMessage;
	}

	@Override
	public String getUserAccessLevel(User theUser) {
		return accessLevelControl.getUserAccessLevel(theUser);
	}

	@Override
	public long getNumberOfUsersForGivenSearchParams(String[] userSearchParameters) {

		String searchType = "SELECT COUNT(*) FROM User WHERE ";
		String hql = searchEngineUtils.prepareHqlUsingSearchParameters(userSearchParameters, searchType,
				NAMES_OF_USER_FIELDS);

		return userDAO.getNumberOfUsersForGivenHql(hql);
	}

	@Override
	public long getNumberOfAllUsers() {

		String hql = "SELECT COUNT(*) FROM User";

		return userDAO.getNumberOfUsersForGivenHql(hql);
	}
}