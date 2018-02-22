package pl.mazur.simpleabclibrary.utils;

import org.springframework.stereotype.Component;

@Component
public class LoginAndAccessLevelCheckImpl implements LoginAndAccessLevelCheck {

	@Override
	public boolean loginCheck(String firstName, String lastname) {

		if (firstName != null && lastname != null)
			return true;
		else
			return false;
	}

	@Override
	public boolean isAdmin(String userAccessLevel) {

		if (userAccessLevel.equals("Administrator"))
			return true;
		else
			return false;
	}

	@Override
	public boolean isEmployee(String userAccessLevel) {

		if (userAccessLevel.equals("Employee") || userAccessLevel.equals("Administrator"))
			return true;
		else
			return false;
	}

	@Override
	public boolean isCustomer(String userAccessLevel) {

		if (userAccessLevel.equals("Customer") || userAccessLevel.equals("Employee")
				|| userAccessLevel.equals("Administrator"))
			return true;
		else
			return false;
	}
}
