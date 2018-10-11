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
@Table(name = "borrowed_books")
public class BorrowedBook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date stopDate;

	@Column(name = "expected_end_date")
	private Date expectedEndDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public Date getExpectedEndDate() {
		return expectedEndDate;
	}

	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}

	public BorrowedBook(Book book, User user, Date startDate, Date stopDate, Date expectedEndDate) {
		this.book = book;
		this.user = user;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.expectedEndDate = expectedEndDate;
	}

	public BorrowedBook() {

	}

	@Override
	public String toString() {
		return "BorrowedBook [id=" + id + ", book=" + book + ", user=" + user + ", startDate=" + startDate
				+ ", stopDate=" + stopDate + ", expectedEndDate=" + expectedEndDate + "]";
	}

}
