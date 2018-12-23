package pl.mazur.simpleabclibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Utility class used to validate the password.
 * 
 * @author Marcin Mazur
 *
 */
@Component
public class PasswordValidatorImpl implements PasswordValidator {

	/**
	 * A representation of a regular expression
	 */
	private Pattern pattern;

	/**
	 * An engine that performs match operations on a character sequence by
	 * interpreting a Pattern.
	 */
	private Matcher matcher;

	/*
	 * A String representation of regular expression
	 */
	private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";

	/**
	 * Constructs a PasswordValidatorImpl.
	 */
	public PasswordValidatorImpl() {
		pattern = Pattern.compile(PASSWORD_PATTERN);

	}

	@Override
	public boolean validate(String password) {

		matcher = pattern.matcher(password);
		return matcher.matches();
	}

}
