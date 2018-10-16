package pl.mazur.simpleabclibrary.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import pl.mazur.simpleabclibrary.entity.User;

public interface UserService {

	public void saveUser(User theUser);

	public User getUser(int theId);

	public boolean checkEmailIsExist(String email);

	public boolean verificationAndAuthentication(String email, String password);

	public User getUser(String email);

	public void updateUser(User theUser);

	public boolean validatePesel(String pesel);

	public void changePassword(int userId, String newPassword);

	public List<User> getAllUsers(int startResult);

	public List<User> getUserSearchResult(String[] userSearchParameters, int startResult);

	public long getAmountOfSearchResult(String[] userSearchParameters);

	public long getAmountOfAllUsers();

	public String increaseUserAccessLevel(Integer increaseAccessLevelUserId);

	public String decreaseUserAccessLevel(Integer decreaseAccessLevelUserId);

	public String getUserAccessLevel(User theUser);

	public String[] prepareTableToSearch(HttpSession session, String searchType, String userId, String userFirstName,
			String userLastName, String userEmail, String userPesel);

	public boolean hasTableAnyParameters(String[] userSearchParameters);

	public long generateShowLessLinkValue(Integer userManagementStartResult);

	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults);

	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue);

	public void clearSearchParameters(HttpSession session, String searchType);

}
