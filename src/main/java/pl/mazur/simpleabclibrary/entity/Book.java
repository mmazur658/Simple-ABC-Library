package pl.mazur.simpleabclibrary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "author")
	private String author;

	@Column(name = "isbn")
	private String isbn;

	@Column(name = "active")
	private boolean isActive;

	@Column(name = "available")
	private boolean isAvailable;

	@Column(name = "publisher")
	private String publisher;

	@Column(name = "language")
	private String language;

	@Column(name = "pages")
	private int pages;

	@Column(name = "date_of_added")
	private Date dateOfAdded;

	public Book(String title, String author, String isbn, boolean isActive, boolean isAvailable, String publisher,
			String language, int pages, Date dateOfAdded) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.isActive = isActive;
		this.isAvailable = isAvailable;
		this.publisher = publisher;
		this.language = language;
		this.pages = pages;
		this.dateOfAdded = dateOfAdded;
	}

	public Book() {

	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", isbn=" + isbn + ", isActive="
				+ isActive + ", isAvailable=" + isAvailable + ", publisher=" + publisher + ", language=" + language
				+ ", pages=" + pages + ", dateOfAdded=" + dateOfAdded + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Date getDateOfAdded() {
		return dateOfAdded;
	}

	public void setDateOfAdded(Date dateOfAdded) {
		this.dateOfAdded = dateOfAdded;
	}

	public void borrowBook(Book tempBook) {

	}

}
