package pl.mazur.simpleabclibrary.service.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;

@Component
public class MessageServiceUtilsImpl implements MessageServiceUtils {

	@Override
	public void changeReadStatus(Message message, String boxType) {
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
	}

	@Override
	public void setReadStatusTrue(Message message, String boxType) {
		if (boxType.equals("sent")) {
			message.setSenderIsRead(true);
		} else {
			message.setRecipientIsRead(true);
		}
	}

	@Override
	public void setReadStatusFalse(Message message, String boxType) {
		if (boxType.equals("sent")) {
			message.setSenderIsRead(false);
		} else {
			message.setRecipientIsRead(false);
		}
	}

	@Override
	public Message sendMessage(User sender, User recipient, String subject, String text) {

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

		return message;

	}

}
