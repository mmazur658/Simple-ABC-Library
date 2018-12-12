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
 * Represents a BorrowedBook record in the database
 * 
 * @author Marcin Mazur
 */
@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

	/**
	 * Unique identification number
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * The book associated with the BorrowedBook
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	private Book book;

	/**
	 * The user associated with the BorrowedBook
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * The date, when the book has been borrowed
	 */
	@Column(name = "start_date")
	private Date startDate;

	/**
	 * The date, when the book has been returned
	 */
	@Column(name = "end_date")
	private Date stopDate;

	/**
	 * The date until the user should return the book
	 */
	@Column(name = "expected_end_date")
	private Date expectedEndDate;

	/*
	 * Setters and getters methods
	 */

	/**
	 * Gets the unique identification number of the BorrowedBook
	 * 
	 * @return An int representing the unique identification number of the
	 *         BorrowedBook
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of the BorrowedBook
	 * 
	 * @param id
	 *            An int containing the unique identification number of the
	 *            BorrowedBook
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the book associated with the BorrowedBook
	 * 
	 * @return A Book representing the book associated with the BorrowedBook
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Sets the book associated with the BorrowedBook
	 * 
	 * @param book
	 *            A Book containing the book associated with the BorrowedBook
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 * Gets the user associated with the BorrowedBook
	 * 
	 * @return A User representing the user associated with the BorrowedBook
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user associated with the BorrowedBook
	 * 
	 * @param user
	 *            A User containing the user associated with the BorrowedBook
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the date od added of the BorrowedBook
	 * 
	 * @return A Date representing the date od added of the BorrowedBook
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the date od added of the BorrowedBook
	 * 
	 * @param startDate
	 *            A Date containing the date od added of the BorrowedBook
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the date, when the book has been returned
	 * 
	 * @return A Date representing the date, when the book has been returned
	 */
	public Date getStopDate() {
		return stopDate;
	}

	/**
	 * Sets the date, when the book has been returned
	 * 
	 * @param stopDate
	 *            A Date containing the date, when the book has been returned
	 */
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	/**
	 * Gets the the date until the user should return the book
	 * 
	 * @return A Date representing the the date until the user should return the
	 *         book
	 */
	public Date getExpectedEndDate() {
		return expectedEndDate;
	}

	/**
	 * Sets the date until the user should return the book
	 * 
	 * @param expectedEndDate
	 *            A Date containing the the date until the user should return the
	 *            book
	 */
	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}

	/**
	 * Constructs a BorrowedBook object.
	 */
	public BorrowedBook() {

	}

	/**
	 * Constructs a BorrowedBook with the Book and User objects, date of borrow,
	 * date of return and expect date of return.
	 * 
	 * @param book
	 *            The book which has been borrowed
	 * @param user
	 *            The user who borrowed the book
	 * @param startDate
	 *            The date, when the book has been borrowed
	 * @param stopDate
	 *            The date, when the book has been returned
	 * @param expectedEndDate
	 *            The date until the user should return the book
	 */
	public BorrowedBook(Book book, User user, Date startDate, Date stopDate, Date expectedEndDate) {
		this.book = book;
		this.user = user;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.expectedEndDate = expectedEndDate;
	}

	/**
	 * Returns the String representation of the BorrowedBook object.
	 * 
	 * @return The String representation of the BorrowedBook object.
	 */
	@Override
	public String toString() {
		return "BorrowedBook [id=" + id + ", book=" + book + ", user=" + user + ", startDate=" + startDate
				+ ", stopDate=" + stopDate + ", expectedEndDate=" + expectedEndDate + "]";
	}

}
