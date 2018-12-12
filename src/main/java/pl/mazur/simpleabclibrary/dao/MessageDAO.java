package pl.mazur.simpleabclibrary.dao;

import java.util.List;
import pl.mazur.simpleabclibrary.entity.Message;

/**
 * Interface for performing database operations on Message objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface MessageDAO {

	/**
	 * Returns the list of all received messages for given user id
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @param messageInboxStartResult
	 *            The int containing the first index of the results
	 * @return A List&lt;Message&gt; representing the list of messages
	 */
	List<Message> getListOfAllMessages(int userId, int messageInboxStartResult);

	/**
	 * Returns the Message with given id
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @return A representing the
	 */
	Message getMessageById(int messageId);

	/**
	 * Saves the Message in the database
	 * 
	 * @param message
	 *            The Message containing the message to be saved
	 */
	void sendMessage(Message message);

	/**
	 * Returns the list of the all sent messages for given user id
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @param messageSentStartResult
	 *            The int containing the first index of the results
	 * @return A List&lt;Message&gt; representing the list of the users
	 */
	List<Message> getAllUserSentMessages(int userId, int messageSentStartResult);

	/**
	 * Returns the number of unread messages for given user id
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @return A long representing the number of unread messages
	 */
	long getNumberOfUnreadMessages(int userId);

}
