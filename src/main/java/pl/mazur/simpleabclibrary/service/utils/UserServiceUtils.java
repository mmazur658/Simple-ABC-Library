package pl.mazur.simpleabclibrary.service.utils;

import java.util.Locale;

import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface used to perform operations for user service classes.
 * 
 * @author Marcin Mazur
 *
 */
public interface UserServiceUtils {

	/**
	 * Sets the additional data of the user.
	 * 
	 * @param theUser
	 *            The User containing the user which data will be expanded
	 */
	void setAdditionalUserData(User theUser);

	/**
	 * Updates the user with the given parameters.
	 * 
	 * @param tempUser
	 *            The User to be updated
	 * @param theUser
	 *            The User containing the user which data will be used to update to
	 *            update the user
	 */
	void updateUserData(User tempUser, User theUser);

	/**
	 * Increases the access level of the user.<br>
	 * <br>
	 * Returns a message informing about new user`s access level.
	 * 
	 * @param theUser
	 *            The User containing the user which access level will be changed
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return A String representing the message with the user's new access level
	 */
	String increaseUserAccessLevel(User theUser, Locale locale);

	/**
	 * Decreases the access level of the user.<br>
	 * <br>
	 * Returns a message informing about new user`s access level.
	 * 
	 * @param theUser
	 *            The User containing the user which access level will be changed
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return A String representing the message with the user's new access level
	 */
	String decreaseUserAccessLevel(User theUser, Locale locale);

}
