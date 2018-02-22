package pl.mazur.simpleabclibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.utils.PeselValidator;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PeselValidator peselValidator;

	@Override
	@Transactional
	public void saveUser(User theUser) {
		userDAO.saveUser(theUser);
	}

	@Override
	@Transactional
	public User getUser(int theId) {
		return userDAO.getUser(theId);
	}

	@Override
	@Transactional
	public boolean checkEmailIsExist(String email) {
		return userDAO.checkEmailIsExist(email);
	}

	@Override
	@Transactional
	public boolean verificationAndAuthentication(String email, String password) {
		return userDAO.verificationAndAuthentication(email, password);
	}

	@Override
	@Transactional
	public User getUser(String email) {
		return userDAO.getUser(email);
	}

	@Override
	@Transactional
	public void updateUser(User theUser) {

		if (!theUser.getPesel().equals("")) {
			theUser.setSex(peselValidator.getSex(theUser.getPesel()));
			theUser.setBirthday(peselValidator.getBirthDate(theUser.getPesel()));
		}

		userDAO.updateUser(theUser);

	}

	@Override
	public boolean validatePesel(String pesel) {
		return peselValidator.validatePesel(pesel);
	}

	@Override
	@Transactional
	public void changePassword(int userId, String newPassword) {
		userDAO.changePassword(userId, newPassword);

	}

	@Override
	@Transactional
	public List<User> getAllUsers(int startResult) {
		return userDAO.getAllUsers(startResult);
	}

	@Override
	@Transactional
	public List<User> getUserSearchResult(String[] userSearchParameters, int startResult) {
		return userDAO.getUserSearchResult(userSearchParameters, startResult);
	}

	@Override
	@Transactional
	public long getAmountOfSearchResult(String[] userSearchParameters) {
		return userDAO.getAmountOfSearchResult(userSearchParameters);
	}

	@Override
	@Transactional
	public long getAmountOfAllBooks() {
		return userDAO.getAmountOfAllBooks();
	}

	@Override
	@Transactional
	public void increaseUserAccessLevel(User theUser) {
		userDAO.increaseUserAccessLevel(theUser);
	}

	@Override
	@Transactional
	public void decreaseUserAccessLevel(User theUser) {
		userDAO.decreaseUserAccessLevel(theUser);
	}

}
