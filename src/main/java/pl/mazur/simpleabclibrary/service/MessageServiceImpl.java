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

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private MessageServiceUtils messageServiceUtils;

	@Override
	@Transactional
	public List<Message> getAllUserMessages(int userId, int messageInboxStartResult) {
		return messageDAO.getAllUserMessages(userId, messageInboxStartResult);
	}

	@Override
	@Transactional
	public Message getMessage(int messageId) {
		return messageDAO.getMessage(messageId);
	}

	@Override
	@Transactional
	public void changeReadStatus(int messageId, String boxType) {

		Message message = messageDAO.getMessage(messageId);
		messageServiceUtils.changeReadStatus(message, boxType);

		messageDAO.updateMessage(message);
	}

	@Override
	@Transactional
	public void setReadStatusTrue(int messageId, String boxType) {

		Message message = messageDAO.getMessage(messageId);
		messageServiceUtils.setReadStatusTrue(message, boxType);

		messageDAO.updateMessage(message);
	}

	@Override
	@Transactional
	public void setReadStatusFalse(int messageId, String boxType) {

		Message message = messageDAO.getMessage(messageId);
		messageServiceUtils.setReadStatusFalse(message, boxType);

		messageDAO.updateMessage(message);
	}

	@Override
	@Transactional
	public void deleteMessage(int messageId, String boxType) {
		messageDAO.deleteMessage(messageId, boxType);
	}

	@Override
	@Transactional
	public void sendMessage(Message message) {
		messageDAO.sendMessage(message);
	}

	@Override
	@Transactional
	public List<Message> getAllUserSentMessages(int userId, int messageSentStartResult) {
		return messageDAO.getAllUserSentMessages(userId, messageSentStartResult);
	}

	@Override
	@Transactional
	public long countUnreadMessages(int userId) {
		return messageDAO.countUnreadMessages(userId);
	}

	@Override
	@Transactional
	public long getAmountOfAllInboxMessages(int userId) {
		return messageDAO.getAmountOfAllInboxMessages(userId);
	}

	@Override
	@Transactional
	public long getAmountOfAllSentMessages(int userId) {
		return messageDAO.getAmountOfAllSentMessages(userId);
	}

	@Override
	@Transactional
	public void sendMessage(int senderID, String recipientEmail, String subject, String text) {

		User sender = userDAO.getUser(senderID);
		User recipient = userDAO.getUser(recipientEmail);
		Message message = messageServiceUtils.createNewMessage(sender, recipient, subject, text);

		messageDAO.sendMessage(message);
	}

}
