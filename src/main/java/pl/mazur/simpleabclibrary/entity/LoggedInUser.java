package pl.mazur.simpleabclibrary.entity;

/**
 * Helper class used to store data of the logged-in user
 * 
 * @author Marcin Mazur
 */
public class LoggedInUser {

	/**
	 * The access level of the user
	 */
	private String userAccessLevel;

	/**
	 * The Unique identification number of the user
	 */
	private int userId;

	/**
	 * Gets the user's access level of the LoggedInUser
	 * 
	 * @return A String representing the user's access level of the LoggedInUser
	 */
	public String getUserAccessLevel() {
		return userAccessLevel;
	}

	/**
	 * Sets the user's access level of the LoggedInUser
	 * 
	 * @param userAccessLevel
	 *            A String containing the user's access level of the LoggedInUser
	 */
	public void setUserAccessLevel(String userAccessLevel) {
		this.userAccessLevel = userAccessLevel;
	}

	/**
	 * Gets the user's id of the LoggedInUser
	 * 
	 * @return An int userId representing the user's id of the LoggedInUser
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the user's id of the LoggedInUser
	 * 
	 * @param userId
	 *            An int containing the user's id of the LoggedInUser
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Constructs a LoggedInUser object.
	 */
	public LoggedInUser() {

	}

	/**
	 * Constructs a LoggedInUser with the user's id and user's access level.
	 * 
	 * @param userId
	 *            The user's id
	 * @param userAccessLevel
	 *            The user's access level
	 */
	public LoggedInUser(int userId, String userAccessLevel) {
		this.userAccessLevel = userAccessLevel;
		this.userId = userId;
	}

	/**
	 * Returns the String representation of the LoggedInUser object.
	 * 
	 * @return The String representation of the LoggedInUser object.
	 */
	@Override
	public String toString() {
		return "LoggedInUser [userAccessLevel=" + userAccessLevel + ", userId=" + userId + "]";
	}

}
