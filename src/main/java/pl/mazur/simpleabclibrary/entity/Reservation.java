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
 * Represents a Reservation record in the database
 * 
 * @author Marcin Mazur
 */
@Entity
@Table(name = "reservation")
public class Reservation {

	/**
	 * Unique identification number
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * The user who has reserved the book.
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * The book which has been reserved.
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	private Book book;

	/**
	 * The isActive status of reservation
	 */
	@Column(name = "is_active")
	private boolean isActive;

	/**
	 * The description of the current state of the reservation
	 */
	@Column(name = "status")
	private String status;

	/**
	 * The date, when the reservation has been created
	 */
	@Column(name = "start_date")
	private Date startDate;

	/**
	 * The date, when the reservation isActive status has been change to false
	 */
	@Column(name = "end_date")
	private Date endDate;

	/**
	 * Gets the unique identification number of the Reservation
	 * 
	 * @return An int representing the unique identification number of the
	 *         Reservation
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of the Reservation
	 * 
	 * @param id
	 *            An int containing the unique identification number of the
	 *            Reservation
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the user associated with the Reservation
	 * 
	 * @return A User representing the user associated with the Reservation
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user associated with the Reservation
	 * 
	 * @param user
	 *            A User containing the user associated with the Reservation
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the book associated with the Reservation
	 * 
	 * @return A Book representing the book associated with the Reservation
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Sets the the book associated with the Reservation
	 * 
	 * @param book
	 *            A Book containing the book associated with the Reservation
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 * Gets the isActive status of the Reservation
	 * 
	 * @return A boolean representing the isActive status of the Reservation
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets the isActive status of the Reservation
	 * 
	 * @param isActive
	 *            A boolean containing the isActive status of the Reservation
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the description of state of the Reservation
	 * 
	 * @return A String representing the description of state of the Reservation
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the description of state of the Reservation
	 * 
	 * @param status
	 *            A String containing the description of state of the Reservation
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the date of added of the Reservation
	 * 
	 * @return A Date representing the date of added of the Reservation
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the date of added of the Reservation
	 * 
	 * @param startDate
	 *            A Date containing the date of added of the Reservation
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the date, when the reservation has been deleted
	 * 
	 * @return A Date representing the date, when the reservation has been deleted
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the date, when the reservation has been deleted
	 * 
	 * @param endDate
	 *            A Date containing the date, when the reservation has been deleted
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Constructs a Reservation object.
	 */
	public Reservation() {

	}

	/**
	 * Constructs a Reservation with the user, book, isActive status, reservation
	 * status, date when the reservation has been created , date when the
	 * reservation has been deleted.
	 * 
	 * @param user
	 *            The user who has reserved the book.
	 * @param book
	 *            The book which has been reserved.
	 * @param isActive
	 *            The isActive status of reservation
	 * @param status
	 *            The description of the current state of the reservation
	 * @param startDate
	 *            The date, when the reservation has been created
	 * @param endDate
	 *            The date, when the reservation has been deleted
	 */
	public Reservation(User user, Book book, boolean isActive, String status, Date startDate, Date endDate) {
		this.user = user;
		this.book = book;
		this.isActive = isActive;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Returns the String representation of the Reservation object.
	 * 
	 * @return The String representation of the Reservation object.
	 */
	@Override
	public String toString() {
		return "Reservation [id=" + id + ", user=" + user + ", book=" + book + ", isActive=" + isActive + ", status="
				+ status + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
