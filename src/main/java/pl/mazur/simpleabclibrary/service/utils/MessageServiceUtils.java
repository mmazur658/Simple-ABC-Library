package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;

public interface MessageServiceUtils {

	void changeReadStatus(Message message, String boxType);

	void setReadStatusTrue(Message message, String boxType);

	void setReadStatusFalse(Message message, String boxType);

	Message createNewMessage(User sender, User recipient, String subject, String text);

}
