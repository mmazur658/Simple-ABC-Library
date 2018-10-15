package pl.mazur.simpleabclibrary.utils;

import org.springframework.stereotype.Component;

@Component
public class LoginAndAccessLevelCheckImpl implements LoginAndAccessLevelCheck {

	@Override
	public boolean loginCheck(Integer userId) {

		if (userId != null)
			return true;
		else
			return false;
	}

	@Override
	public boolean isAdmin(String userAccessLevel) {

		if (userAccessLevel != null) {
			if (userAccessLevel.equals("Administrator"))
				return true;
			else
				return false;
		} else {
			return false;
		}

	}

	@Override
	public boolean isEmployee(String userAccessLevel) {

		if (userAccessLevel != null) {
			if (userAccessLevel.equals("Employee") || userAccessLevel.equals("Administrator"))
				return true;
			else
				return false;
		} else {
			return false;
		}

	}

	@Override
	public boolean isCustomer(String userAccessLevel) {

		if (userAccessLevel != null) {
			if (userAccessLevel.equals("Customer") || userAccessLevel.equals("Employee")
					|| userAccessLevel.equals("Administrator"))
				return true;
			else
				return false;
		} else {
			return false;
		}

	}
}
