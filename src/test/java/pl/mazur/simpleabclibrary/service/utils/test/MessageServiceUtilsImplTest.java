package pl.mazur.simpleabclibrary.service.utils.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.MessageServiceUtilsImpl;

class MessageServiceUtilsImplTest {

	private MessageServiceUtilsImpl messageServiceUtilsImpl = new MessageServiceUtilsImpl();
	private Message message;

	@Test
	void shoudlChangeReadStatus() {

		message = new Message();
		message.setRecipientIsRead(true);
		message.setSenderIsRead(true);

		messageServiceUtilsImpl.changeReadStatus(message, "sent");
		assertTrue(message.getRecipientIsRead());
		assertFalse(message.getSenderIsRead());

		messageServiceUtilsImpl.changeReadStatus(message, "sent");
		assertTrue(message.getRecipientIsRead());
		assertTrue(message.getSenderIsRead());

		messageServiceUtilsImpl.changeReadStatus(message, "inbox");
		assertFalse(message.getRecipientIsRead());
		assertTrue(message.getSenderIsRead());

		messageServiceUtilsImpl.changeReadStatus(message, "inbox");
		assertTrue(message.getRecipientIsRead());
		assertTrue(message.getSenderIsRead());

	}

	@Test
	void shoudlSetReadStatusTrue() {

		message = new Message();
		message.setRecipientIsRead(false);
		message.setSenderIsRead(false);

		messageServiceUtilsImpl.setReadStatusTrue(message, "sent");
		messageServiceUtilsImpl.setReadStatusTrue(message, "foo");

		assertTrue(message.getRecipientIsRead());
		assertTrue(message.getSenderIsRead());

	}

	@Test
	void shoudlSetReadStatusFalse() {

		message = new Message();
		message.setRecipientIsRead(true);
		message.setSenderIsRead(true);

		messageServiceUtilsImpl.setReadStatusFalse(message, "sent");
		messageServiceUtilsImpl.setReadStatusFalse(message, "");

		assertFalse(message.getRecipientIsRead());
		assertFalse(message.getSenderIsRead());

	}

	@Test
	void shoudlCreateNewMessage() {

		User sender = new User();
		sender.setFirstName("Marcin");
		User recipient = new User();
		recipient.setFirstName("Micha³");
		String messageSubject = "Test message";
		String messageText = "Small test message";

		Message expectedMessage = new Message();
		expectedMessage.setRecipientIsActive(true);
		expectedMessage.setRecipientIsRead(false);
		expectedMessage.setSenderIsActive(true);
		expectedMessage.setSenderIsRead(true);
		expectedMessage.setRecipient(recipient);
		expectedMessage.setSender(sender);
		expectedMessage.setStartDate(new Date());
		expectedMessage.setSubject(messageSubject);
		expectedMessage.setText(messageText);

		Message message = messageServiceUtilsImpl.createNewMessage(sender, recipient, messageSubject, messageText);

		assertEquals(expectedMessage.getRecipient().getFirstName(), message.getRecipient().getFirstName());
		assertEquals(expectedMessage.getSender().getFirstName(), message.getSender().getFirstName());
		assertEquals(expectedMessage.getSenderIsActive(), message.getSenderIsActive());
		assertEquals(expectedMessage.getSubject(), message.getSubject());
		assertEquals(expectedMessage.getText(), message.getText());

	}

}
