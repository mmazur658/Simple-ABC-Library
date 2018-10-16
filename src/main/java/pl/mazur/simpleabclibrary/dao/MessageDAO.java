package pl.mazur.simpleabclibrary.dao;

import java.util.List;
import pl.mazur.simpleabclibrary.entity.Message;

public interface MessageDAO {

	List<Message> getAllUserMessages(int userId, int messageInboxStartResult);

	Message getMessage(int messageId);

	void deleteMessage(int messageId, String boxType);

	void sendMessage(Message message);

	List<Message> getAllUserSentMessages(int userId, int messageSentStartResult);

	long countUnreadMessages(int userId);

	long getAmountOfAllInboxMessages(int userId);

	long getAmountOfAllSentMessages(int userId);

	public void updateMessage(Message message);

}
