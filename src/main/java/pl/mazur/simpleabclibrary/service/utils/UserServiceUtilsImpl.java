package pl.mazur.simpleabclibrary.service.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;

@Component
public class UserServiceUtilsImpl implements UserServiceUtils {

	private PasswordUtils passwordUtils;
	private PeselValidator peselValidator;

	@Autowired
	public UserServiceUtilsImpl(PasswordUtils passwordUtils, PeselValidator peselValidator) {
		this.passwordUtils = passwordUtils;
		this.peselValidator = peselValidator;
	}

	@Override
	public void setAdditionalData(User theUser) {
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
			return "Zwiêkszono uprawnienia do poziomu: Pracownik";
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(true);
			return "Zwiêkszono uprawnienia do poziomu: Administrator";
		} else
			return "Nie mo¿na zwiêkszyæ uprawnieñ, osi¹gniêto maksymalny poziom";
	}

	@Override
	public String decreaseUserAccessLevel(User theUser) {
		if (theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(false);
			return "Zmniejszono uprawnienia do poziomu: Pracownik";
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setEmployee(false);
			return "Zmniejszono uprawnienia do poziomu: Klient";
		} else
			return "Nie mo¿na zmniejszyæ uprawnieñ, osi¹gniêto minimalny poziom";
	}
}
