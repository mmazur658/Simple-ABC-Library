package pl.mazur.simpleabclibrary.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.entity.BorrowedBook;

@Repository
public class BookDAOImpl implements BookDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ReservationDAO reservationDAO;

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Book> getAllBooks(int startResult) {

		List<Book> listOfAllBooks = new ArrayList<>();
		String hql = "from Book where isActive = true ORDER BY id ASC";
		Query<Book> theQuery = currentSession().createQuery(hql);
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(10);
		listOfAllBooks = theQuery.getResultList();

		return listOfAllBooks;
	}

	@Override
	public List<Book> bookSearchResult(String hql, int startResult) {

		List<Book> booksList = new ArrayList<>();

		try {
			Query<Book> theQuery = currentSession().createQuery(hql);
			theQuery.setFirstResult(startResult);
			theQuery.setMaxResults(10);
			booksList = theQuery.getResultList();

			return booksList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveBook(Book tempBook) {
		currentSession().save(tempBook);
	}

	@Override
	public Book getBook(int bookId) {
		return currentSession().get(Book.class, bookId);
	}

	@Override
	public void setBookActive(Book book) {
		book.setIsAvailable(true);
		currentSession().update(book);
	}

	@Override
	public void updateBook(Book book) {
		currentSession().update(book);
	}

	@Override
	public void deleteBook(Book tempBook) {
		currentSession().update(tempBook);
	}

	@Override
	public void borrowBook(BorrowedBook borrowedBook) {
		currentSession().save(borrowedBook);
	}

	@Override
	public List<BorrowedBook> getUserBorrowedBookList(int userId) {

		List<BorrowedBook> userBorrowedBookList = new ArrayList<>();
		String hql = "from BorrowedBook where user.id=:id AND stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", userId);
		userBorrowedBookList = theQuery.getResultList();

		return userBorrowedBookList;
	}

	@Override
	public void closeBorrowedBook(Book book) {

		String hql = "from BorrowedBook where book.id=:id and stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", book.getId());
		BorrowedBook borrowedBook = (BorrowedBook) theQuery.getSingleResult();
		borrowedBook.setStopDate(new Date());

		currentSession().update(borrowedBook);
	}

	@Override
	public BorrowedBook getBorrowedBook(int borrowedBookId) {
		return currentSession().get(BorrowedBook.class, borrowedBookId);
	}

	@Override
	public Date getExpectedEndDate(User tempUser, Book book) {

		String hql = "from BorrowedBook where book.id=:id and user.id=:userid and stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		theQuery.setParameter("id", book.getId());
		theQuery.setParameter("userid", tempUser.getId());
		BorrowedBook borrowedBook = (BorrowedBook) theQuery.getSingleResult();

		return borrowedBook.getExpectedEndDate();
	}

	@Override
	public long getAmountOfSearchResult(String hql) {

		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllBooks() {

		String hql = "select count(*) from Book where isActive = true ORDER BY id ASC";
		Query<Long> theQuery = currentSession().createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public List<BorrowedBook> getAllBorrowedBookList() {

		List<BorrowedBook> borrowedBookList = new ArrayList<>();
		String hql = "FROM BorrowedBook WHERE stopDate = null ORDER BY id ASC";
		Query<BorrowedBook> theQuery = currentSession().createQuery(hql);
		borrowedBookList = theQuery.getResultList();

		return borrowedBookList;
	}

	@Override
	public void setBookActive(int reservationId) {

		Reservation reservation = reservationDAO.getReservation(reservationId);
		Book book = reservation.getBook();
		book.setIsAvailable(true);

		currentSession().update(book);
	}
}