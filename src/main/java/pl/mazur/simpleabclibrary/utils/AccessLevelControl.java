package pl.mazur.simpleabclibrary.utils;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface used to controlling the access level of the user
 * 
 * @author Marcin
 *
 */
public interface AccessLevelControl {

	/**
	 * Returns TRUE if the given id is not null.
	 * 
	 * @param userId The Integer containing the id to be checked
	 * @return A boolean representing the result
	 */
	public boolean isIdNotNull(Integer userId);

	/**
	 * Returns TRUE if the given access level is equal to "Administrator".
	 * @param userAccessLevel The String containing the access level
	 * @return A boolean representing the result
	 */
	public boolean isAdmin(String userAccessLevel);

	/**
	 * Returns TRUE if the given access level is equal to "Employee".
	 * @param userAccessLevel The String containing the access level
	 * @return A boolean representing the result
	 */
	public boolean isEmployee(String userAccessLevel);

	/**
	 * Returns TRUE if the given access level is equal to "Customer".
	 * @param userAccessLevel The String containing the access level
	 * @return A boolean representing the result
	 */
	public boolean isCustomer(String userAccessLevel);

	/**
	 * Returns the access level of the given user
	 * @param tempUser The User containing the user that access level will be returned
	 * @return A String representing the access level of the user
	 */
	public String getUserAccessLevel(User tempUser);

	/**
	 * Returns TRUE if the given access level is equal to "Customer".
	 * @param loggedInUser The LoggedInUser object containing the access level
	 * @return A boolean representing the result
	 */
	public boolean isCustomer(LoggedInUser loggedInUser);

	/**
	 * Returns TRUE if the given access level is equal to "Employee".
	 * 
	 * @param loggedInUser
	 *            The LoggedInUser object containing the access level
	 * @return A boolean representing the result
	 */
	public boolean isEmployee(LoggedInUser loggedInUser);

	/**
	 * Returns TRUE if the given access level is equal to "Administrator".
	 * @param loggedInUser The LoggedInUser object containing the access level
	 * @return A boolean representing the result
	 */
	public boolean isAdmin(LoggedInUser loggedInUser);

}
