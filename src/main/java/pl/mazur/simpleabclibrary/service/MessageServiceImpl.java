package pl.mazur.simpleabclibrary.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.MessageDAO;
import pl.mazur.simpleabclibrary.dao.UserDAO;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDAO;

	@Autowired
	private UserDAO userDAO;

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
		if (boxType.equals("sent")) {
			if (message.getSenderIsRead())
				message.setSenderIsRead(false);
			else
				message.setSenderIsRead(true);
		} else {
			if (message.getRecipientIsRead())
				message.setRecipientIsRead(false);
			else
				message.setRecipientIsRead(true);
		}
		messageDAO.updateMessage(message);
	}

	@Override
	@Transactional
	public void setReadStatusTrue(int messageId, String boxType) {

		Message message = messageDAO.getMessage(messageId);
		if (boxType.equals("sent")) {
			message.setSenderIsRead(true);
		} else {
			message.setRecipientIsRead(true);
		}
		messageDAO.updateMessage(message);
	}

	@Override
	@Transactional
	public void setReadStatusFalse(int messageId, String boxType) {

		Message message = messageDAO.getMessage(messageId);
		if (boxType.equals("sent")) {
			message.setSenderIsRead(false);
		} else {
			message.setRecipientIsRead(false);
		}
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

		Message message = new Message();
		message.setRecipientIsActive(true);
		message.setRecipientIsRead(false);
		message.setSenderIsActive(true);
		message.setSenderIsRead(true);
		message.setRecipient(recipient);
		message.setSender(sender);
		message.setStartDate(new Date());
		message.setSubject(subject);
		message.setText(text);

		messageDAO.sendMessage(message);
	}

	@Override
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults) {
		if ((startResult + 20) > amountOfResults) {
			return startResult;
		} else {
			return startResult + 20;
		}
	}

	@Override
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue) {
		if ((startResult + 20) > amountOfResults) {
			return "Wyniki od " + (startResult + 1) + " do " + amountOfResults;
		} else {
			return "Wyniki od " + (startResult + 1) + " do " + showMoreLinkValue;
		}
	}

	@Override
	public long generateShowLessLinkValue(Integer startResult) {
		if ((startResult - 20) < 0) {
			return 0;
		} else {
			return startResult - 20;
		}
	}

}
