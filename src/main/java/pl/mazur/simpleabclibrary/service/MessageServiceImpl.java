package pl.mazur.simpleabclibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.MessageDAO;
import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.MessageServiceUtils;

/**
 * Service class for managing Message objects.
 * 
 * @author Marcin Mazur
 *
 */
@Service
public class MessageServiceImpl implements MessageService {

	/**
	 * The MessageDAO interface
	 */
	private MessageDAO messageDAO;

	/**
	 * The UserDAO interface
	 */
	private UserDAO userDAO;

	/**
	 * The MessageServiceUtils interface
	 */
	private MessageServiceUtils messageServiceUtils;

	/**
	 * Constructs a MessageServiceImpl with the MessageDAO, UserDAO and
	 * MessageServiceUtils.
	 * 
	 * @param messageDAO
	 *            The MessageDAO interface
	 * @param userDAO
	 *            The UserDAO interface
	 * @param messageServiceUtils
	 *            The MessageServiceUtils interface
	 */
	@Autowired
	public MessageServiceImpl(MessageDAO messageDAO, UserDAO userDAO, MessageServiceUtils messageServiceUtils) {
		this.messageDAO = messageDAO;
		this.userDAO = userDAO;
		this.messageServiceUtils = messageServiceUtils;
	}

	@Override
	@Transactional
	public List<Message> getListOfInboxMessagesByUserId(int userId, int messageInboxStartResult) {
		return messageDAO.getListOfAllMessages(userId, messageInboxStartResult);
	}

	@Override
	@Transactional
	public Message getMessageById(int messageId) {
		return messageDAO.getMessageById(messageId);
	}

	@Override
	@Transactional
	public void changeReadStatus(int messageId, String boxType) {

		Message message = messageDAO.getMessageById(messageId);
		messageServiceUtils.changeReadStatus(message, boxType);

	}

	@Override
	@Transactional
	public void setIsReadStatusToTrue(int messageId, String boxType) {

		Message message = messageDAO.getMessageById(messageId);
		messageServiceUtils.setReadStatusTrue(message, boxType);

	}

	@Override
	@Transactional
	public void setIsReadStatusToFalse(int messageId, String boxType) {

		Message message = messageDAO.getMessageById(messageId);
		messageServiceUtils.setReadStatusFalse(message, boxType);

	}

	@Override
	@Transactional
	public void deleteMessage(int messageId, String boxType) {

		Message message = messageDAO.getMessageById(messageId);

		if (boxType.equals("sent")) {
			message.setSenderIsActive(false);
		} else {
			message.setRecipientIsActive(false);
		}

	}

	@Override
	@Transactional
	public void sendMessage(Message message) {
		messageDAO.sendMessage(message);
	}

	@Override
	@Transactional
	public List<Message> getListOfSentMessagesByUserId(int userId, int messageSentStartResult) {
		return messageDAO.getAllUserSentMessages(userId, messageSentStartResult);
	}

	@Override
	@Transactional
	public long getNumberOfUnreadMessages(int userId) {
		return messageDAO.getNumberOfUnreadMessages(userId);
	}

	@Override
	@Transactional
	public void sendMessage(int senderID, String recipientEmail, String subject, String text) {

		User sender = userDAO.getUserById(senderID);
		User recipient = userDAO.getUserByEmail(recipientEmail);
		Message message = messageServiceUtils.createNewMessage(sender, recipient, subject, text);

		messageDAO.sendMessage(message);
	}

	@Override
	public long getNumberOfAllMessages(int userId, String boxType) {

		String hql = null;
		if (boxType.equals("sent")) {
			hql = "SELECT COUNT(*) FROM Message WHERE sender=" + userId + " and senderIsActive=true";
		} else if (boxType.equals("inbox")) {
			hql = "SELECT COUNT(*) FROM Message WHERE recipient=" + userId + " and recipientIsActive=true";
		}

		return messageDAO.getNumberOfMessagesForGivenHql(hql);
	}

}
