package pl.mazur.simpleabclibrary.dao;

import java.util.List;

import pl.mazur.simpleabclibrary.entity.User;

public interface UserDAO {

	public void saveUser(User theUser);

	public User getUser(int theId);

	public boolean checkEmailIsExist(String email);

	public boolean authenticatePassword(String thePasswordFromForm, String theEncryptedPasswordFromDatabase);

	public boolean verificationAndAuthentication(String email, String password);

	public User getUser(String email);

	public void updateUser(User theUser);

	public void changePassword(int userId, String newPassword);

	public List<User> getAllUsers(int startResult);

	public List<User> getUserSearchResult(String hql, int startResult);

	public long getAmountOfSearchResult(String hql);

	public long getAmountOfAllBooks();

	public void increaseUserAccessLevel(User theUser);

	public void decreaseUserAccessLevel(User theUser);

}
