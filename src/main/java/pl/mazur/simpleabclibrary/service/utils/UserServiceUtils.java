package pl.mazur.simpleabclibrary.service.utils;

import java.util.Locale;

import pl.mazur.simpleabclibrary.entity.User;

public interface UserServiceUtils {

	void setAdditionalUserData(User theUser);

	void updateUserData(User tempUser, User theUser);

	String increaseUserAccessLevel(User theUser, Locale locale);

	String decreaseUserAccessLevel(User theUser, Locale locale);

}
