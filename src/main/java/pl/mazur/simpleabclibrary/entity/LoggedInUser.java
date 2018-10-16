package pl.mazur.simpleabclibrary.entity;

public class LoggedInUser {

	private String userAccessLevel;

	private int userId;

	public String getUserAccessLevel() {
		return userAccessLevel;
	}

	public void setUserAccessLevel(String userAccessLevel) {
		this.userAccessLevel = userAccessLevel;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LoggedInUser( int userId, String userAccessLevel) {
		this.userAccessLevel = userAccessLevel;
		this.userId = userId;
	}

	public LoggedInUser() {

	}

	@Override
	public String toString() {
		return "LoggedInUser [userAccessLevel=" + userAccessLevel + ", userId=" + userId + "]";
	}

}
