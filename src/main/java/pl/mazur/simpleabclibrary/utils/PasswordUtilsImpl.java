package pl.mazur.simpleabclibrary.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Component;

/**
 * Utility class used to perform operations on the passwords.
 * 
 * @author Marcin
 *
 */
@Component
public class PasswordUtilsImpl implements PasswordUtils {

	/**
	 * The StrongPasswordEncryptor class
	 */
	private static StrongPasswordEncryptor theStrongPasswordEncryptor = new StrongPasswordEncryptor();

	@Override
	public String encryptPassword(String thePassword) {

		String encryptedPassword = theStrongPasswordEncryptor.encryptPassword(thePassword);

		return encryptedPassword;
	}

	@Override
	public boolean isPasswordCorrect(String thePassword, String encryptedPassword) {

		return theStrongPasswordEncryptor.checkPassword(thePassword, encryptedPassword);
	}

}
