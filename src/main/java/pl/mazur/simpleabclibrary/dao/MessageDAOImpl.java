package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.Message;

@Repository
public class MessageDAOImpl implements MessageDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Message> getAllUserMessages(int userId, int messageInboxStartResult) {

		Session session = sessionFactory.getCurrentSession();

		List<Message> userMessagesList = new ArrayList<>();
		Query theQuery = session
				.createQuery("FROM Message WHERE recipient.id=:id AND recipientIsActive=true ORDER BY startDate DESC");
		theQuery.setParameter("id", userId);
		theQuery.setMaxResults(20);
		theQuery.setFirstResult(messageInboxStartResult);
		userMessagesList = theQuery.getResultList();

		return userMessagesList;
	}

	@Override
	public Message getMessage(int messageId) {

		Session session = sessionFactory.getCurrentSession();

		Message tempMessage = session.get(Message.class, messageId);

		return tempMessage;
	}

	@Override
	public void changeReadStatus(int messageId, String boxType) {

		Session session = sessionFactory.getCurrentSession();

		Message message = session.get(Message.class, messageId);
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
	public void setReadStatusTrue(int messageId, String boxType) {

		Session session = sessionFactory.getCurrentSession();

		Message message = session.get(Message.class, messageId);
		if (boxType.equals("sent")) {
			message.setSenderIsRead(true);
		} else {
			message.setRecipientIsRead(true);
		}

		session.update(message);

	}

	@Override
	public void setReadStatusFalse(int messageId, String boxType) {

		Session session = sessionFactory.getCurrentSession();

		Message message = session.get(Message.class, messageId);
		if (boxType.equals("sent")) {
			message.setSenderIsRead(false);
		} else {
			message.setRecipientIsRead(false);
		}

		session.update(message);

	}

	@Override
	public void deleteMessage(int messageId, String boxType) {

		Session session = sessionFactory.getCurrentSession();

		Message message = session.get(Message.class, messageId);
		if (boxType.equals("sent")) {
			message.setSenderIsActive(false);
		} else {
			message.setRecipientIsActive(false);
		}

		session.update(message);

	}

	@Override
	public void sendMessage(Message message) {

		Session session = sessionFactory.getCurrentSession();

		session.save(message);

	}

	@Override
	public List<Message> getAllUserSentMessages(int userId, int messageSentStartResult) {

		Session session = sessionFactory.getCurrentSession();

		List<Message> userSentMessagesList = new ArrayList<>();
		Query theQuery = session
				.createQuery("FROM Message WHERE sender.id=:id AND senderIsActive=true ORDER BY startDate DESC");
		theQuery.setParameter("id", userId);
		theQuery.setMaxResults(20);
		theQuery.setFirstResult(messageSentStartResult);
		userSentMessagesList = theQuery.getResultList();

		return userSentMessagesList;

	}

	@Override
	public long countUnreadMessages(int userId) {

		Session session = sessionFactory.getCurrentSession();

		Query theQuery = session.createQuery(
				"SELECT COUNT(*) FROM Message WHERE recipient.id=:id AND recipientIsActive=true AND recipientIsRead=false");
		theQuery.setParameter("id", userId);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllInboxMessages(int userId) {

		Session session = sessionFactory.getCurrentSession();

		Query theQuery = session
				.createQuery("SELECT COUNT(*) FROM Message WHERE recipient.id=:id AND recipientIsActive=true");
		theQuery.setParameter("id", userId);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllSentMessages(int userId) {

		Session session = sessionFactory.getCurrentSession();

		Query theQuery = session
				.createQuery("SELECT COUNT(*) FROM Message WHERE sender.id=:id AND senderIsActive=true");
		theQuery.setParameter("id", userId);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

}
