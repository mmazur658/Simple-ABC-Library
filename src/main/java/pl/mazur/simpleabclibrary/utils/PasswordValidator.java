package pl.mazur.simpleabclibrary.utils;

/**
 * Interface used to validate the password.
 * 
 * @author Marcin Mazur
 *
 */
public interface PasswordValidator {

	/**
	 * Returns TRUE if the given password match to the pattern
	 * 
	 * @param password
	 *            The String containing the plain password.
	 * @return A boolean representing the result
	 */
	public boolean validate(String password);

}
