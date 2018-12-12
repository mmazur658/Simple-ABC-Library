package pl.mazur.simpleabclibrary.utils;

/**
 * Interface used to perform operations on the passwords.
 * 
 * @author Marcin Mazur
 *
 */
public interface PasswordUtils {

	/**
	 * Returns the encrypted password for given plain password.
	 * 
	 * @param thePassword
	 *            The String containing the plain password
	 * @return A String representing the encrypted password
	 */
	String encryptPassword(String thePassword);

	/**
	 * Returns TRUE if the given passwords are the same.
	 * 
	 * @param thePassword
	 *            The String containing the plain password
	 * @param encryptedPassword
	 *            The String containing the encrypted password
	 * @return A boolean representing the result
	 */
	boolean isPasswordCorrect(String thePassword, String encryptedPassword);
}
