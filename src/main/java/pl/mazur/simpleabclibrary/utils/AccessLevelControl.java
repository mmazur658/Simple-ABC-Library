package pl.mazur.simpleabclibrary.utils;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.User;

public interface AccessLevelControl {

	public boolean loginCheck(Integer userId);

	public boolean isAdmin(String userAccessLevel);

	public boolean isEmployee(String userAccessLevel);

	public boolean isCustomer(String userAccessLevel);
	
	public String getUserAccessLevel(User tempUser);

	public boolean isCustomer(LoggedInUser loggedInUser);

	public boolean isEmployee(LoggedInUser loggedInUser);

	public boolean isAdmin(LoggedInUser loggedInUser);

}
