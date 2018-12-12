package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.Message;

/**
 * Repository class for performing database operations on Message objects.
 * 
 * @author Marcin Mazur
 *
 */
@Repository
public class MessageDAOImpl implements MessageDAO {

	/**
	 * The SessionFactory interface
	 */
	private SessionFactory sessionFactory;

	/**
	 * Constructs a MessageDAOImpl with the SessionFactory.
	 * 
	 * @param sessionFactory
	 *            The SessionFactory interface
	 */
	@Autowired
	public MessageDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getListOfAllMessages(int userId, int messageInboxStartResult) {

		List<Message> userMessagesList = new ArrayList<>();
		String hql = "FROM Message WHERE recipient.id=:id AND recipientIsActive=true ORDER BY startDate DESC";
		Query<Message> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		theQuery.setMaxResults(20);
		theQuery.setFirstResult(messageInboxStartResult);
		userMessagesList = theQuery.getResultList();

		return userMessagesList;
	}

	@Override
	public Message getMessageById(int messageId) {
		return currentSession().get(Message.class, messageId);
	}

	/*
	 * @Override public void updateMessage(Message message) {
	 * currentSession().update(message); }
	 */

	/*
	 * @Override public void deleteMessage(int messageId, String boxType) {
	 * 
	 * Message message = currentSession().get(Message.class, messageId); if
	 * (boxType.equals("sent")) { message.setSenderIsActive(false); } else {
	 * message.setRecipientIsActive(false); }
	 * 
	 * currentSession().update(message); }
	 */

	@Override
	public void sendMessage(Message message) {
		currentSession().save(message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getAllUserSentMessages(int userId, int messageSentStartResult) {

		List<Message> userSentMessagesList = new ArrayList<>();
		String hql = "FROM Message WHERE sender.id=:id AND senderIsActive=true ORDER BY startDate DESC";
		Query<Message> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		theQuery.setMaxResults(20);
		theQuery.setFirstResult(messageSentStartResult);
		userSentMessagesList = theQuery.getResultList();

		return userSentMessagesList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getNumberOfUnreadMessages(int userId) {

		String hql = "SELECT COUNT(*) FROM Message WHERE recipient.id=:id AND recipientIsActive=true AND recipientIsRead=false";
		Query<Long> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

}