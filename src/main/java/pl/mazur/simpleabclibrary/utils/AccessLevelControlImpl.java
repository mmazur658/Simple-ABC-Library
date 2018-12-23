package pl.mazur.simpleabclibrary.utils;

import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Utility class used to controlling the access level of the user
 * 
 * @author Marcin
 *
 */
@Component
public class AccessLevelControlImpl implements AccessLevelControl {

	@Override
	public boolean isIdNotNull(Integer userId) {

		boolean isIdNotNull = (userId != null) ? true : false;
		return isIdNotNull;

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

		if (tempUser.getIsAdmin())
			return "Administrator";	
		else if (!tempUser.getIsAdmin() && tempUser.getIsEmployee())
			return "Employee";
		else
			return "Customer";
	}

	@Override
	public boolean isCustomer(LoggedInUser loggedInUser) {

		if (loggedInUser == null)
			return false;
		
		if (isCustomer(loggedInUser.getUserAccessLevel()) && isIdNotNull(loggedInUser.getUserId()))
			return true;
		else
			return false;
	}

	@Override
	public boolean isEmployee(LoggedInUser loggedInUser) {
		
		if (loggedInUser == null)
			return false;
		
		if (isEmployee(loggedInUser.getUserAccessLevel()) && isIdNotNull(loggedInUser.getUserId()))
			return true;
		else
			return false;
	}

	@Override
	public boolean isAdmin(LoggedInUser loggedInUser) {
				
		if (loggedInUser == null)
			return false;
		
		if (isAdmin(loggedInUser.getUserAccessLevel()) && isIdNotNull(loggedInUser.getUserId()))
			return true;
		else
			return false;
	}
}
