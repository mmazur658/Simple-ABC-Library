package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Interface used to perform operations for message service classes.
 * 
 * @author Marcin Mazur
 *
 */
public interface MessageServiceUtils {

	/**
	 * Changes the isRead status of the message depending on the type of the box.
	 * 
	 * @param message
	 *            The Message containing the message which status will be changed
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void changeReadStatus(Message message, String boxType);

	/**
	 * Changes the isRead status of the message depending on the type of the box to
	 * TRUE.
	 * 
	 * @param message
	 *            The Message containing the message which status will be changed
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void setReadStatusTrue(Message message, String boxType);

	/**
	 * Changes the isRead status of the message depending on the type of the box to
	 * FALSE.
	 * 
	 * @param message
	 *            The Message containing the message which status will be changed
	 * @param boxType
	 *            A String containing the name of the box ( inbox - sent )
	 */
	void setReadStatusFalse(Message message, String boxType);

	/**
	 * Creates and returns the message with given parameters.
	 * 
	 * @param sender
	 *            The User containing the sender of the message
	 * @param recipient
	 *            The User containing the recipient of the message
	 * @param subject
	 *            The String containing the subject of the message
	 * @param text
	 *            The String containing the text of the message
	 * @return A Message representing the message with given parameters
	 */
	Message createNewMessage(User sender, User recipient, String subject, String text);

}
