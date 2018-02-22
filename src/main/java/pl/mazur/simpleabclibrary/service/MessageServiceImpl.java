package pl.mazur.simpleabclibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mazur.simpleabclibrary.dao.MessageDAO;
import pl.mazur.simpleabclibrary.entity.Message;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDAO messageDAO;

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
		messageDAO.changeReadStatus(messageId, boxType);
	}

	@Override
	@Transactional
	public void setReadStatusTrue(int messageId, String boxType) {
		messageDAO.setReadStatusTrue(messageId, boxType);
	}

	@Override
	@Transactional
	public void setReadStatusFalse(int messageId, String boxType) {
		messageDAO.setReadStatusFalse(messageId, boxType);
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

}
