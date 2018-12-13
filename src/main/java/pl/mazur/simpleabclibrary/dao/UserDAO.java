package pl.mazur.simpleabclibrary.dao;

import java.util.List;

import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface for performing database operations on User objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface UserDAO {

	/**
	 * Saves the user in the data base
	 * 
	 * @param theUser
	 *            The user to be saved
	 */
	public void saveUser(User theUser);

	/**
	 * Returns the user with the given id
	 * 
	 * @param theId
	 *            The int containing the id of the user
	 * @return A User representing the user with given id
	 */
	public User getUserById(int theId);

	/**
	 * Returns TRUE if the given email is unique
	 * 
	 * @param email
	 *            The String containing the email
	 * @return A Boolean representing the result
	 */
	public boolean isEmailUnique(String email);

	/**
	 * Returns TRUE if the passwords are the same
	 * 
	 * @param thePasswordFromForm
	 *            The String containing the new password
	 * @param theEncryptedPasswordFromDatabase
	 *            The String containing new password
	 * @return A Boolean representing the result
	 */
	public boolean isPasswordCorrect(String thePasswordFromForm, String theEncryptedPasswordFromDatabase);

	/**
	 * Returns TRUE if the password and the email are correct
	 * 
	 * @param email
	 *            The String containing the email
	 * @param password
	 *            The String containing the password
	 * @return A Boolean representing the result
	 */
	public boolean isEmailAndPasswordCorrect(String email, String password);

	/**
	 * Returns the user with given email
	 * 
	 * @param email
	 *            The String containing the email
	 * @return A User representing the User with the given email
	 */
	public User getUserByEmail(String email);

	/**
	 * Returns the list of all users.
	 * 
	 * @param startResult
	 *            The int containing the first index of the results
	 * @return A List&lt;User&gt; representing the list of the users
	 */
	public List<User> getListOfAllUsers(int startResult);

	/**
	 * Returns the list of all users for given HQL statement
	 * 
	 * @param hql
	 *            The String containing the HQL Statement to be executed
	 * @param startResult
	 *            The int containing the first index of the results
	 * @return A List&lt;User&gt; representing the list of the users
	 */
	public List<User> getListOfUserForGivenSearchParams(String hql, int startResult);

	/**
	 * Returns the number of users for given HQL statement
	 * 
	 * @param hql
	 *            The String containing the HQL to be executed
	 * @return A long representing the number of users
	 */
	public long getNumberOfUsersForGivenHql(String hql);
}
