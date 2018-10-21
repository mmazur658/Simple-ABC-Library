package pl.mazur.simpleabclibrary.service.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;

@Component
@PropertySource("classpath:messages.properties")
public class UserServiceUtilsImpl implements UserServiceUtils {

	private PasswordUtils passwordUtils;
	private PeselValidator peselValidator;
	private Environment env;

	@Autowired
	public UserServiceUtilsImpl(PasswordUtils passwordUtils, PeselValidator peselValidator, Environment env) {
		this.passwordUtils = passwordUtils;
		this.peselValidator = peselValidator;
		this.env = env;
	}

	@Override
	public void setAdditionalUserData(User theUser) {
		theUser.setActive(true);
		theUser.setAdmin(false);
		theUser.setEmployee(false);
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
	public String increaseUserAccessLevel(User theUser) {
		if (!theUser.isAdmin() && !theUser.isEmployee()) {
			theUser.setEmployee(true);
			return env.getProperty("service.utils.UserServiceUtilsImpl.increaseUserAccessLevel.success.1");
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(true);
			return env.getProperty("service.utils.UserServiceUtilsImpl.increaseUserAccessLevel.success.2");
		} else
			return env.getProperty("service.utils.UserServiceUtilsImpl.increaseUserAccessLevel.error.1");
	}

	@Override
	public String decreaseUserAccessLevel(User theUser) {
		if (theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(false);
			return env.getProperty("service.utils.UserServiceUtilsImpl.decreaseUserAccessLevel.success.1");
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setEmployee(false);
			return env.getProperty("service.utils.UserServiceUtilsImpl.decreaseUserAccessLevel.success.2");
		} else
			return env.getProperty("service.utils.UserServiceUtilsImpl.decreaseUserAccessLevel.error.1");
	}
}
