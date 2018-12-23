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
	 * The number of results to be returned
	 */
	private final int RESULT_LIMIT = 20;

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
		theQuery.setMaxResults(RESULT_LIMIT);
		theQuery.setFirstResult(messageInboxStartResult);
		userMessagesList = theQuery.getResultList();

		return userMessagesList;
	}

	@Override
	public Message getMessageById(int messageId) {
		return currentSession().get(Message.class, messageId);
	}

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
		theQuery.setMaxResults(RESULT_LIMIT);
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

	@SuppressWarnings("unchecked")
	@Override
	public long getNumberOfMessagesForGivenHql(String hql) {

		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

}