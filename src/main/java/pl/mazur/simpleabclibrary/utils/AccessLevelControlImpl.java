package pl.mazur.simpleabclibrary.utils;

import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.User;

@Component
public class AccessLevelControlImpl implements AccessLevelControl {

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

	@Override
	public String getUserAccessLevel(User tempUser) {

		if (tempUser.isAdmin())
			return "Administrator";
		else if (!tempUser.isAdmin() && tempUser.isEmployee())
			return "Employee";
		else
			return "Customer";
	}

	@Override
	public boolean isCustomer(LoggedInUser loggedInUser) {
		
		if(loggedInUser==null)
			return false;
		if (isCustomer(loggedInUser.getUserAccessLevel()) && loginCheck(loggedInUser.getUserId()))
			return true;
		else
			return false;
	}

	@Override
	public boolean isEmployee(LoggedInUser loggedInUser) {
		if(loggedInUser==null)
			return false;
		if (isEmployee(loggedInUser.getUserAccessLevel()) && loginCheck(loggedInUser.getUserId()))
			return true;
		else
			return false;
	}

	@Override
	public boolean isAdmin(LoggedInUser loggedInUser) {
		if(loggedInUser==null)
			return false;
		if (isAdmin(loggedInUser.getUserAccessLevel()) && loginCheck(loggedInUser.getUserId()))
			return true;
		else
			return false;
	}
}
