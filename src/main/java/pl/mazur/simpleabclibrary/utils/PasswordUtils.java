package pl.mazur.simpleabclibrary.utils;

public interface PasswordUtils {
	
	String encryptPassword(String thePassword);

	boolean checkPassword(String thePassword, String encryptedPassword);
}
