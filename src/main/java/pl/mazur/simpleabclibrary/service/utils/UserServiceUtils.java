package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.User;

public interface UserServiceUtils {

	void setAdditionalUserData(User theUser);

	void updateUserData(User tempUser, User theUser);

	String increaseUserAccessLevel(User theUser);

	String decreaseUserAccessLevel(User theUser);

}
