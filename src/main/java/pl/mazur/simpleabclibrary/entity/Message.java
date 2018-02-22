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

@Entity
@Table(name = "messages")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "text")
	private String text;

	@Column(name = "subject")
	private String subject;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "recipient_is_active")
	private boolean recipientIsActive;

	@ManyToOne(optional = false)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(optional = false)
	@JoinColumn(name = "recipient_id")
	private User recipient;

	@Column(name = "recipient_is_read")
	private boolean recipientIsRead;

	@Column(name = "sender_is_read")
	private boolean senderIsRead;

	@Column(name = "sender_is_active")
	private boolean senderIsActive;

	public Message() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isRecipientIsActive() {
		return recipientIsActive;
	}

	public void setRecipientIsActive(boolean recipientIsActive) {
		this.recipientIsActive = recipientIsActive;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public boolean getRecipientIsRead() {
		return recipientIsRead;
	}

	public void setRecipientIsRead(boolean recipientIsRead) {
		this.recipientIsRead = recipientIsRead;
	}

	public boolean getSenderIsRead() {
		return senderIsRead;
	}

	public void setSenderIsRead(boolean senderIsRead) {
		this.senderIsRead = senderIsRead;
	}

	public boolean getSenderIsActive() {
		return senderIsActive;
	}

	public void setSenderIsActive(boolean senderIsActive) {
		this.senderIsActive = senderIsActive;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", text=" + text + ", subject=" + subject + ", startDate=" + startDate
				+ ", recipientIsActive=" + recipientIsActive + ", sender=" + sender + ", recipient=" + recipient
				+ ", recipientIsRead=" + recipientIsRead + ", senderIsRead=" + senderIsRead + ", senderIsActive="
				+ senderIsActive + "]";
	}

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

}