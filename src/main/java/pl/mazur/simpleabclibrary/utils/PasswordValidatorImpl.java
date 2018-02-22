package pl.mazur.simpleabclibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidatorImpl implements PasswordValidator{

	private Pattern pattern;
	private Matcher matcher;

	private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";

	public PasswordValidatorImpl() {

		pattern = Pattern.compile(PASSWORD_PATTERN);

	}

	public boolean validate(String password) {

		matcher = pattern.matcher(password);

		return matcher.matches();
	}

}
