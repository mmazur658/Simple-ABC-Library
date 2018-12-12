package pl.mazur.simpleabclibrary.service.utils;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;

/**
 * Utility class used to perform operations for user service classes.
 * 
 * @author Marcin Mazur
 *
 */
@Component
@PropertySource("classpath:systemMessages.properties")
public class UserServiceUtilsImpl implements UserServiceUtils {

	/**
	 * The PasswordUtils interface
	 */
	private PasswordUtils passwordUtils;

	/**
	 * The PeselValidator interface
	 */
	private PeselValidator peselValidator;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * Constructs a UserServiceUtilsImpl with the PasswordUtils, PeselValidator and
	 * Environment.
	 * 
	 * @param passwordUtils
	 *            The PasswordUtils interface
	 * @param peselValidator
	 *            The PeselValidator interface
	 * @param env
	 *            The Environment interface
	 */
	@Autowired
	public UserServiceUtilsImpl(PasswordUtils passwordUtils, PeselValidator peselValidator, Environment env) {
		this.passwordUtils = passwordUtils;
		this.peselValidator = peselValidator;
		this.env = env;
	}

	@Override
	public void setAdditionalUserData(User theUser) {
		theUser.setIsActive(true);
		theUser.setIsAdmin(false);
		theUser.setIsEmployee(false);
		theUser.setStartDate(new Date());
		theUser.setPassword(passwordUtils.encryptPassword(theUser.getPassword().trim()));
	}

	@Override
	public void updateUserData(User tempUser, User theUser) {
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
	}

	@Override
	public String increaseUserAccessLevel(User theUser, Locale locale) {
		if (!theUser.getIsAdmin() && !theUser.getIsEmployee()) {
			theUser.setIsEmployee(true);
			return env.getProperty(
					locale.getLanguage() + ".service.utils.UserServiceUtilsImpl.increaseUserAccessLevel.success.1");
		} else if (!theUser.getIsAdmin() && theUser.getIsEmployee()) {
			theUser.setIsAdmin(true);
			return env.getProperty(
					locale.getLanguage() + ".service.utils.UserServiceUtilsImpl.increaseUserAccessLevel.success.2");
		} else
			return env.getProperty(
					locale.getLanguage() + ".service.utils.UserServiceUtilsImpl.increaseUserAccessLevel.error.1");
	}

	@Override
	public String decreaseUserAccessLevel(User theUser, Locale locale) {
		if (theUser.getIsAdmin() && theUser.getIsEmployee()) {
			theUser.setIsAdmin(false);
			return env.getProperty(
					locale.getLanguage() + ".service.utils.UserServiceUtilsImpl.decreaseUserAccessLevel.success.1");
		} else if (!theUser.getIsAdmin() && theUser.getIsEmployee()) {
			theUser.setIsEmployee(false);
			return env.getProperty(
					locale.getLanguage() + ".service.utils.UserServiceUtilsImpl.decreaseUserAccessLevel.success.2");
		} else
			return env.getProperty(
					locale.getLanguage() + ".service.utils.UserServiceUtilsImpl.decreaseUserAccessLevel.error.1");
	}
}
