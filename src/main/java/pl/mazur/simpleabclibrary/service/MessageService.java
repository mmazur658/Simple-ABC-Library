package pl.mazur.simpleabclibrary.service;

import java.util.List;

import pl.mazur.simpleabclibrary.entity.Message;

/**
 * Interface for managing Message objects.
 * 
 * @author Marcin Mazur
 *
 */
public interface MessageService {

	/**
	 * Return the list of all messages from inbox for given id of the user.
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @param messageInboxStartResult
	 *            An int containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;&gt; representing the
	 */
	List<Message> getListOfInboxMessagesByUserId(int userId, int messageInboxStartResult);

	/**
	 * Return the Message with the given id
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @return A Message representing the Message with given id
	 */
	Message getMessageById(int messageId);

	/**
	 * Changes the isRead status of the message with given id depending on the type
	 * of the box.
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void changeReadStatus(int messageId, String boxType);

	/**
	 * Changes the isRead status of the message with given id depending on the type
	 * of the box to TRUE.
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void setIsReadStatusToTrue(int messageId, String boxType);

	/**
	 * Changes the isRead status of the message with given id depending on the type
	 * of the box to FALSE.
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void setIsReadStatusToFalse(int messageId, String boxType);

	/**
	 * Changes the isActive status of the message with given id depending on the
	 * type of the box of to FALSE.
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void deleteMessage(int messageId, String boxType);

	/**
	 * Saves a Message in the database.
	 * 
	 * @param message
	 *            The Message to be saved
	 */
	void sendMessage(Message message);

	/**
	 * Return the list of all messages from Sent box for given id of the user.
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @param messageSentStartResult
	 *            An int containing a value that specifies the first index of
	 *            returned results.
	 * @return A List&lt;Message&gt; representing the list of messages
	 */
	List<Message> getListOfSentMessagesByUserId(int userId, int messageSentStartResult);

	/**
	 * Returns the number of unread messages for given id of the user
	 * 
	 * @param userId
	 *            The int containing the id of the user
	 * @return A long representing the number of unread messages
	 */
	long getNumberOfUnreadMessages(int userId);

	/**
	 * Creates and saves the message with given parameters.
	 * 
	 * @param senderID
	 *            The int containing the id of the sender
	 * @param recipientEmail
	 *            The String containing the email of the recipient
	 * @param subject
	 *            The String containing the subject of the message
	 * @param text
	 *            The String containing the text of the message
	 */
	void sendMessage(int senderID, String recipientEmail, String subject, String text);
	
	/**
	 * Returns the number of  messages for given parameters.
	 * @param boxType The String containing the name of the box (inbox - sent)
	 * @param userId The int containing the id of the user
	 * 
	 * @return A long representing the number of all messages
	 */
	public long getNumberOfAllMessages(int userId, String boxType);

}
