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

	private final String ADMINISTRATOR = "Administrator";
	private final String EMPLOYEE = "Employee";
	private final String CUSTOMER = "Customer";

	@Override
	public boolean isIdNotNull(Integer userId) {
		return (userId != null);

	}

	@Override
	public boolean isAdmin(String userAccessLevel) {

		if (userAccessLevel != null)
			return userAccessLevel.equals(ADMINISTRATOR);
		else
			return false;

	}

	@Override
	public boolean isEmployee(String userAccessLevel) {

		if (userAccessLevel != null)
			return (userAccessLevel.equals(EMPLOYEE) || userAccessLevel.equals(ADMINISTRATOR));
		else
			return false;

	}

	@Override
	public boolean isCustomer(String userAccessLevel) {

		if (userAccessLevel != null)
			return (userAccessLevel.equals(CUSTOMER) || userAccessLevel.equals(EMPLOYEE)
					|| userAccessLevel.equals(ADMINISTRATOR));
		else
			return false;

	}

	@Override
	public String getUserAccessLevel(User tempUser) {

		if (tempUser.getIsAdmin())
			return ADMINISTRATOR;
		else if (!tempUser.getIsAdmin() && tempUser.getIsEmployee())
			return EMPLOYEE;
		else
			return CUSTOMER;
	}

	@Override
	public boolean isCustomer(LoggedInUser loggedInUser) {

		if (loggedInUser == null)
			return false;

		return isCustomer(loggedInUser.getUserAccessLevel()) && isIdNotNull(loggedInUser.getUserId());

	}

	@Override
	public boolean isEmployee(LoggedInUser loggedInUser) {

		if (loggedInUser == null)
			return false;

		return isEmployee(loggedInUser.getUserAccessLevel()) && isIdNotNull(loggedInUser.getUserId());

	}

	@Override
	public boolean isAdmin(LoggedInUser loggedInUser) {

		if (loggedInUser == null)
			return false;

		return isAdmin(loggedInUser.getUserAccessLevel()) && isIdNotNull(loggedInUser.getUserId());

	}
}
