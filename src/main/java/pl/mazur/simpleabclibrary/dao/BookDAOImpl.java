package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;

/**
 * Repository class for performing database operations on Message objects.
 * 
 * @author Marcin Mazur
 *
 */
@Repository
public class BookDAOImpl implements BookDAO {

	/**
	 * Determines the number of results returned from the database
	 */
	private final int RESULT_LIMIT = 10;

	/**
	 * The SessionFactory interface
	 */
	private SessionFactory sessionFactory;

	/**
	 * Constructs a BookDAOImpl with the SessionFactory.
	 * 
	 * @param sessionFactory
	 *            The SessionFactory interface
	 */
	@Autowired
	public BookDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Returns a session object.
	 * 
	 * @return A Session representing the current session
	 */
	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Book> getListOfBooks(String hql, int startResult) {

		if (hql == null || hql == "")
			hql = "from Book where isActive = true ORDER BY id ASC";

		List<Book> booksList = new ArrayList<>();

		try {
			Query<Book> theQuery = currentSession().createQuery(hql);
			theQuery.setFirstResult(startResult);
			theQuery.setMaxResults(RESULT_LIMIT);
			booksList = theQuery.getResultList();
			return booksList;

		} catch (NoResultException exception) {
			System.err.println("No results");
			exception.printStackTrace();
			return null;
		}

	}

	@Override
	public void saveBook(Book tempBook) {
		currentSession().save(tempBook);
	}

	@Override
	public Book getBookById(int bookId) {
		return currentSession().get(Book.class, bookId);
	}

	@Override
	public void updateBook(Book tempBook) {
		currentSession().update(tempBook);
	}

	@Override
	public void saveBorrowedBook(BorrowedBook borrowedBook) {
		currentSession().save(borrowedBook);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowedBook> getListOfBorrowedBookByUserId(int userId) {

		List<BorrowedBook> userBorrowedBookList = new ArrayList<>();
		String hql = "from BorrowedBook where user.id=:id AND stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		userBorrowedBookList = theQuery.getResultList();

		return userBorrowedBookList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowedBook> getListOfAllBorrowedBook() {

		List<BorrowedBook> borrowedBookList = new ArrayList<>();
		String hql = "FROM BorrowedBook WHERE stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		borrowedBookList = theQuery.getResultList();

		return borrowedBookList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BorrowedBook getBorrowedBookByBookId(int bookId) {

		String hql = "from BorrowedBook where book.id=:id and stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", bookId);
		BorrowedBook borrowedBook = (BorrowedBook) theQuery.getSingleResult();

		return borrowedBook;
	}

	@Override
	public BorrowedBook getBorrowedBookById(int borrowedBookId) {
		return currentSession().get(BorrowedBook.class, borrowedBookId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BorrowedBook getBorrowedBookByBookIdAndUserId(int bookId, int userId) {

		String hql = "from BorrowedBook where book.id=:id and user.id=:userid and stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", bookId);
		theQuery.setParameter("userid", userId);
		BorrowedBook borrowedBook = (BorrowedBook) theQuery.getSingleResult();

		return borrowedBook;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long getNumberOfBookForGivenHql(String hql) {

		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();
		
		return count;
	}

}