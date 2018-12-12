package pl.mazur.simpleabclibrary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a Message record in the database
 * 
 * @author Marcin Mazur
 */
@Entity
@Table(name = "messages")
public class Message {

	/**
	 * Unique identification number
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * The text of the message
	 */
	@Column(name = "text")
	private String text;

	/**
	 * The subject of the message
	 */
	@Column(name = "subject")
	private String subject;

	/**
	 * The date of sending the message
	 */
	@Column(name = "start_date")
	private Date startDate;

	/**
	 * The recipient isActive status which determines whether the user has deleted
	 * the message.
	 */
	@Column(name = "recipient_is_active")
	private boolean recipientIsActive;

	/**
	 * The user, who sends the message
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "sender_id")
	private User sender;

	/**
	 * The user, who receives the message
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "recipient_id")
	private User recipient;

	/**
	 * The status of the message that determines whether the message has been read
	 * by the recipient.
	 */
	@Column(name = "recipient_is_read")
	private boolean recipientIsRead;

	/**
	 * The status of the message that determines whether the message has been read
	 * by the sender.
	 */
	@Column(name = "sender_is_read")
	private boolean senderIsRead;

	/**
	 * The recipient isActive status which determines whether the user has deleted
	 * the message.
	 */
	@Column(name = "sender_is_active")
	private boolean senderIsActive;

	/**
	 * Gets the unique identification number of the Message
	 * 
	 * @return An int representing the unique identification number of the Message
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of the Message
	 * 
	 * @param id
	 *            An int containing the unique identification number of the Message
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the text of the Message
	 * 
	 * @return A String representing the text of the Message
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text of the Message
	 * 
	 * @param text
	 *            A String containing the text of the Message
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the subject of the Message
	 * 
	 * @return A String representing the subject of the Message
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of the Message
	 * 
	 * @param subject
	 *            A String containing the subject of the Message
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the date of sending the message
	 * 
	 * 
	 * @return A Date representing the date of sending the message
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the date of sending the message
	 * 
	 * @param startDate
	 *            A Date containing the date of sending the message
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the isActive status of the recipient
	 * 
	 * @return A boolean representing the isActive status of the recipient
	 */
	public boolean getRecipientIsActive() {
		return recipientIsActive;
	}

	/**
	 * Sets the isActive status of the recipient
	 * 
	 * @param recipientIsActive
	 *            A boolean containing the isActive status of the recipient
	 */
	public void setRecipientIsActive(boolean recipientIsActive) {
		this.recipientIsActive = recipientIsActive;
	}

	/**
	 * Gets the user as a sender of the Message
	 * 
	 * @return A User representing the user as a sender of the Message
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * Sets the user as a sender of the Message
	 * 
	 * @param sender
	 *            A User containing the user as a sender of the Message
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}

	/**
	 * Gets the user as a recipient of the Message
	 * 
	 * @return A User representing the user as a recipient of the Message
	 */
	public User getRecipient() {
		return recipient;
	}

	/**
	 * Sets the user as a recipient of the Message
	 * 
	 * @param recipient
	 *            A User containing the user as a recipient of the Message
	 */
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	/**
	 * Gets the isRead status of the recipient
	 * 
	 * @return A boolean representing the isRead status of the recipient
	 */
	public boolean getRecipientIsRead() {
		return recipientIsRead;
	}

	/**
	 * Sets the isRead status of the recipient
	 * 
	 * @param recipientIsRead
	 *            A boolean containing the isRead status of the recipient
	 */
	public void setRecipientIsRead(boolean recipientIsRead) {
		this.recipientIsRead = recipientIsRead;
	}

	/**
	 * Gets the isRead status of the sender
	 * 
	 * @return A boolean representing the isRead status of the sender
	 */
	public boolean getSenderIsRead() {
		return senderIsRead;
	}

	/**
	 * Sets the isRead status of the sender
	 * 
	 * @param senderIsRead
	 *            A boolean containing the isRead status of the sender
	 */
	public void setSenderIsRead(boolean senderIsRead) {
		this.senderIsRead = senderIsRead;
	}

	/**
	 * Gets the isActive status of the sender
	 * 
	 * @return A boolean representing the isActive status of the sender
	 */
	public boolean getSenderIsActive() {
		return senderIsActive;
	}

	/**
	 * Sets the isActive status of the sender
	 * 
	 * @param senderIsActive
	 *            A boolean containing the isActive status of the sender
	 */
	public void setSenderIsActive(boolean senderIsActive) {
		this.senderIsActive = senderIsActive;
	}

	/**
	 * Constructs a Message object.
	 */
	public Message() {

	}

	/**
	 * Constructs a Message with the text, subject, date of added, recipient
	 * isAvtice status, sender, recipient, recipient isRead status, sender isActive
	 * status and sender isActive status.
	 * 
	 * @param text
	 *            The text of the message.
	 * @param subject
	 *            The subject of the message
	 * @param startDate
	 *            The date of sending the message
	 * @param recipientIsActive
	 *            The isActive status of the recipient
	 * @param sender
	 *            The sender of the message
	 * @param recipient
	 *            The recipient of the message
	 * @param recipientIsRead
	 *            he isRead status of the recipient
	 * @param senderIsRead
	 *            The isRead status of the sender
	 * @param senderIsActive
	 *            The isActive status of the sender
	 */
	public Message(String text, String subject, Date startDate, boolean recipientIsActive, User sender, User recipient,
			boolean recipientIsRead, boolean senderIsRead, boolean senderIsActive) {
		this.text = text;
		this.subject = subject;
		this.startDate = startDate;
		this.recipientIsActive = recipientIsActive;
		this.sender = sender;
		this.recipient = recipient;
		this.recipientIsRead = recipientIsRead;
		this.senderIsRead = senderIsRead;
		this.senderIsActive = senderIsActive;
	}

	/**
	 * Returns the String representation of the Message object.
	 * 
	 * @return The String representation of the Message object.
	 */
	@Override
	public String toString() {
		return "Message [id=" + id + ", text=" + text + ", subject=" + subject + ", startDate=" + startDate
				+ ", recipientIsActive=" + recipientIsActive + ", sender=" + sender + ", recipient=" + recipient
				+ ", recipientIsRead=" + recipientIsRead + ", senderIsRead=" + senderIsRead + ", senderIsActive="
				+ senderIsActive + "]";
	}
}