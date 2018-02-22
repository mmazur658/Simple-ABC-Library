package pl.mazur.simpleabclibrary.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtilImpl implements PasswordUtils {

	private static StrongPasswordEncryptor theStrongPasswordEncryptor = new StrongPasswordEncryptor();

	public String encryptPassword(String thePassword) {

		String result = theStrongPasswordEncryptor.encryptPassword(thePassword);

		return result;
	}

	public boolean checkPassword(String thePassword, String encryptedPassword) {

		return theStrongPasswordEncryptor.checkPassword(thePassword, encryptedPassword);
	}

}
