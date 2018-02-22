package pl.mazur.simpleabclibrary.utils;

public interface LoginAndAccessLevelCheck {

	public boolean loginCheck(String firstName, String lastname);

	public boolean isAdmin(String userAccessLevel);

	public boolean isEmployee(String userAccessLevel);

	public boolean isCustomer(String userAccessLevel);

}
