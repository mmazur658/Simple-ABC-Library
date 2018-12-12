package pl.mazur.simpleabclibrary.service;

import java.util.List;
import java.util.Locale;

import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface for managing User objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface UserService {

	/**
	 * Saves the User in the database.
	 * 
	 * @param theUser
	 *            The User to be saved.
	 */
	public void saveUser(User theUser);

	/**
	 * Returns the User with given id.
	 * 
	 * @param theId
	 *            The int containing the id of the user
	 * @return A User representing the user with given id
	 */
	public User getUserById(int theId);

	/**
	 * Returns TRUE if the given email is unique.
	 * 
	 * @param email
	 *            The String containing the email
	 * @return A boolean representing the result
	 */
	public boolean isEmailCorrect(String email);

	/**
	 * Returns TRUE if the given email and password are correct.
	 * 
	 * @param email
	 *            The String containing the email
	 * @param password
	 *            The String containing the password
	 * @return A boolean representing the result
	 */
	public boolean isEmailAndPasswordCorrect(String email, String password);

	/**
	 * Return the User with the given email.
	 * 
	 * @param email
	 *            The String containing the email
	 * @return A User representing the user with the given email.
	 */
	public User getUserByEmail(String email);

	/**
	 * Updates the user.
	 * 
	 * @param theUser
	 *            The User to be updated
	 */
	public void updateUser(User theUser);

	/**
	 * Returns TRUE if the PESEL is correct.
	 * 
	 * @param pesel
	 *            The String containing the PESEL
	 * @return A boolean representing the result
	 */
	public boolean isPeselCorrect(String pesel);

	/**
	 * Changes the password if the user with given id
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @param newPassword
	 *            The String containing the new password
	 */
	public void changePassword(int userId, String newPassword);

	/**
	 * Returns the list of all users.
	 * 
	 * @param startResult
	 *            An int containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;User&gt; representing the list of the users
	 */
	public List<User> getListOfAllUsers(int startResult);

	/**
	 * Returns the list of user for given search parameters.
	 * 
	 * @param userSearchParameters
	 *            The String[] containing the search parameters
	 * @param startResult
	 *            An int containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;User&gt; representing the list of the users
	 */
	public List<User> getListOfUserByGivenSearchParams(String[] userSearchParameters, int startResult);

	/**
	 * Increases the access level of the user with given id.<br>
	 * Returns the message as a String.
	 * 
	 * @param increaseAccessLevelUserId
	 *            The int containing the id of the user
	 * @param locale
	 *            The Locale containing the user`s locale
	 * @return A String representing the message
	 */
	public String increaseUserAccessLevel(Integer increaseAccessLevelUserId, Locale locale);

	/**
	 * Decreases the access level of the user with given id.<br>
	 * Returns the message as a String.
	 * 
	 * @param decreaseAccessLevelUserId
	 *            The int containing the id of the user
	 * @param locale
	 *            The Locale containing the user`s locale
	 * @return A String representing the message
	 */
	public String decreaseUserAccessLevel(Integer decreaseAccessLevelUserId, Locale locale);

	/**
	 * Returns the access level of the given user.
	 * 
	 * @param theUser
	 *            The User containing the user which access level will be returned
	 * @return A String representing the message
	 */
	public String getUserAccessLevel(User theUser);

}
