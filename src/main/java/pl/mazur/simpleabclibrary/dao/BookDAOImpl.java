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
import pl.mazur.simpleabclibrary.entity.BookBorrowing;

@Repository
public class BookDAOImpl implements BookDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	ReservationDAO reservationDAO;

	@Override
	public List<Book> getAllBooks(int startResult) {

		Session session = sessionFactory.getCurrentSession();
		List<Book> listOfAllBooks = new ArrayList<>();
		Query theQuery = session.createQuery("from Book where isActive = true ORDER BY id ASC");
		theQuery.setFirstResult(startResult);
		theQuery.setMaxResults(10);
		listOfAllBooks = theQuery.getResultList();

		return listOfAllBooks;
	}

	@Override
	public List<Book> bookSearchResult(String[] searchParameters, int startResult) {

		// 0 - title, 1 - author, 2 - publisher, 3 - isbn, 5-id

		boolean isContent = false;
		StringBuilder sb = new StringBuilder();
		List<Book> booksList = new ArrayList<>();

		sb.append("from Book where ");

		if (!searchParameters[0].equals("")) {
			sb.append("title like '%" + searchParameters[0] + "%'");
			isContent = true;
		}

		if (!searchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("author like '%" + searchParameters[1] + "%'");
			} else {
				sb.append("author like '%" + searchParameters[1] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("publisher like '%" + searchParameters[2] + "%'");
			} else {
				sb.append("publisher like '%" + searchParameters[2] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isbn like '%" + searchParameters[3] + "%'");
			} else {
				sb.append("isbn like '%" + searchParameters[3] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[5].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("id like '%" + searchParameters[5] + "%'");
			} else {
				sb.append("id like '%" + searchParameters[5] + "%'");
				isContent = true;
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");

		String hql = sb.toString();

		try {
			Session session = sessionFactory.getCurrentSession();
			Query theQuery = session.createQuery(hql);
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

		Session session = sessionFactory.getCurrentSession();
		session.save(tempBook);

	}

	@Override
	public Book getBook(int bookId) {

		Session session = sessionFactory.getCurrentSession();
		Book tempBook = session.get(Book.class, bookId);

		return tempBook;
	}

	@Override
	public void setBookActive(Book book) {

		Session session = sessionFactory.getCurrentSession();
		book.setIsAvailable(true);
		session.update(book);

	}

	@Override
	public void updateBook(Book book) {

		Session session = sessionFactory.getCurrentSession();

		Book tempBook = session.get(Book.class, book.getId());
		tempBook.setAuthor(book.getAuthor());
		tempBook.setTitle(book.getTitle());
		tempBook.setIsbn(book.getIsbn());
		tempBook.setLanguage(book.getLanguage());
		tempBook.setPages(book.getPages());
		tempBook.setPublisher(book.getPublisher());

		session.update(tempBook);

	}

	@Override
	public void deleteBook(Book tempBook) {

		Session session = sessionFactory.getCurrentSession();
		tempBook.setIsActive(false);
		session.update(tempBook);

	}

	@Override
	public void borrowBook(Book tempBook, User tempUser) {

		Session session = sessionFactory.getCurrentSession();

		BookBorrowing bookBorrowing = new BookBorrowing();
		bookBorrowing.setBook(tempBook);
		bookBorrowing.setUser(tempUser);
		bookBorrowing.setStartDate(new Date());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bookBorrowing.getStartDate());
		calendar.add(Calendar.DATE, 14);

		bookBorrowing.setExpectedEndDate(calendar.getTime());
		session.save(bookBorrowing);

		tempBook.setIsAvailable(false);
		session.update(tempBook);

	}

	@Override
	public List<BookBorrowing> getUserBookBorrowing(int userId) {

		Session session = sessionFactory.getCurrentSession();

		List<BookBorrowing> userBookBorrowingList = new ArrayList<>();
		Query theQuery = session
				.createQuery("from BookBorrowing where user.id=:id AND stopDate = null ORDER BY id ASC");
		theQuery.setParameter("id", userId);
		userBookBorrowingList = theQuery.getResultList();

		return userBookBorrowingList;

	}

	@Override
	public void returnBook(Book book) {

		Session session = sessionFactory.getCurrentSession();

		book.setIsAvailable(true);
		session.update(book);

	}

	@Override
	public void closeBoorowingBook(Book book) {

		Session session = sessionFactory.getCurrentSession();

		Query theQuery = session
				.createQuery("from BookBorrowing where book.id=:id and stopDate = null ORDER BY id ASC");
		theQuery.setParameter("id", book.getId());
		BookBorrowing bookBorrowing = (BookBorrowing) theQuery.getSingleResult();
		bookBorrowing.setStopDate(new Date());
		session.update(bookBorrowing);

	}

	@Override
	public BookBorrowing getBookBorrowing(int bookBorrowingId) {
		Session session = sessionFactory.getCurrentSession();
		BookBorrowing bookBorrowing = session.get(BookBorrowing.class, bookBorrowingId);
		return bookBorrowing;
	}

	@Override
	public Date getExpectedEndDate(User tempUser, Book book) {

		Session session = sessionFactory.getCurrentSession();

		Query theQuery = session.createQuery(
				"from BookBorrowing where book.id=:id and user.id=:userid and stopDate = null ORDER BY id ASC");
		theQuery.setParameter("id", book.getId());
		theQuery.setParameter("userid", tempUser.getId());
		BookBorrowing bookBorrowing = (BookBorrowing) theQuery.getSingleResult();

		return bookBorrowing.getExpectedEndDate();
	}

	@Override
	public long getAmountOfSearchResult(String[] searchParameters) {
		// 0 - title, 1 - author, 2 - publisher, 3 - isbn, 4- isAvailable

		boolean isContent = false;
		StringBuilder sb = new StringBuilder();

		sb.append("select count(*) from Book where ");

		if (!searchParameters[0].equals("")) {
			sb.append("title like '%" + searchParameters[0] + "%'");
			isContent = true;
		}

		if (!searchParameters[1].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("author like '%" + searchParameters[1] + "%'");
			} else {
				sb.append("author like '%" + searchParameters[1] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[2].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("publisher like '%" + searchParameters[2] + "%'");
			} else {
				sb.append("publisher like '%" + searchParameters[2] + "%'");
				isContent = true;
			}
		}

		if (!searchParameters[3].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isbn like '%" + searchParameters[3] + "%'");
			} else {
				sb.append("isbn like '%" + searchParameters[3] + "%'");
				isContent = true;
			}
		}

		if (searchParameters[4].equals("true")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("isAvailable=true");
			} else {
				sb.append("isAvailable=true");
				isContent = true;
			}
		}

		if (!searchParameters[5].equals("")) {
			if (isContent) {
				sb.append(" AND ");
				sb.append("id like '%" + searchParameters[5] + "%'");
			} else {
				sb.append("id like '%" + searchParameters[5] + "%'");
				isContent = true;
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");

		String hql = sb.toString();

		Session session = sessionFactory.getCurrentSession();
		Query theQuery = session.createQuery(hql);
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public long getAmountOfAllBooks() {

		Session session = sessionFactory.getCurrentSession();
		Query theQuery = session.createQuery("select count(*) from Book where isActive = true ORDER BY id ASC");
		Long count = (Long) theQuery.uniqueResult();

		return count;
	}

	@Override
	public List<BookBorrowing> getAllBookBorrowing() {

		Session session = sessionFactory.getCurrentSession();
		List<BookBorrowing> bookBorrowingList = new ArrayList<>();
		Query theQuery = session.createQuery("FROM BookBorrowing WHERE stopDate = null ORDER BY id ASC");
		bookBorrowingList = theQuery.getResultList();

		return bookBorrowingList;
	}

	@Override
	public void setBookActive(int reservationId) {

		Session session = sessionFactory.getCurrentSession();

		Reservation reservation = reservationDAO.getReservation(reservationId);
		Book book = reservation.getBook();
		book.setIsAvailable(true);

		session.update(book);

	}

}
