package pl.mazur.simpleabclibrary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a Book record in the database
 * 
 * @author Marcin Mazur
 */
@Entity
@Table(name = "books")
public class Book {

	/**
	 * Unique identification number
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * The title of the book.
	 */
	@Column(name = "title")
	private String title;

	/**
	 * The author of the book.
	 */
	@Column(name = "author")
	private String author;

	/**
	 * The isbn of the book. International Standard Book Number. The ISBN is 13
	 * digits long.
	 */
	@Column(name = "isbn")
	private String isbn;

	/**
	 * The isActive status. Determines whether any operations can be performed on
	 * the book.
	 */
	@Column(name = "active")
	private boolean isActive;

	/**
	 * The isAvailable status. Determines whether the book can be borrowed.
	 */
	@Column(name = "available")
	private boolean isAvailable;

	/**
	 * The name of the publisher
	 */
	@Column(name = "publisher")
	private String publisher;

	/**
	 * The language of the book
	 */
	@Column(name = "language")
	private String language;

	/**
	 * The number of book pages.
	 */
	@Column(name = "pages")
	private int pages;

	/**
	 * The date of added
	 */
	@Column(name = "date_of_added")
	private Date dateOfAdded;

	/*
	 * Setters and getters methods
	 */

	/**
	 * Gets the unique identification number of the Book
	 * 
	 * @return An int representing the unique identification number of the Book
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of the Book
	 * 
	 * @param id
	 *            An int containing the unique identification number of the Book
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the title of the Book
	 * 
	 * @return A String representing the title of the Book
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the Book
	 * 
	 * @param title
	 *            A String containing the title of the Book
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the author of the Book
	 * 
	 * @return A String representing the author of the Book
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author of the Book
	 * 
	 * @param author
	 *            A String containing the author of the Book
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Gets the ISBN number of the Book
	 * 
	 * @return A String representing the ISBN number of the Book
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * Sets the ISBN number of the Book
	 * 
	 * @param isbn
	 *            A String containing the ISBN number of the Book
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * Gets the isActive status of the Book
	 * 
	 * @return A boolean representing the isActive status of the Book
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets the isActive status of the Book
	 * 
	 * @param isActive
	 *            A boolean containing the isActive status of the Book
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the isAvailable status of the Book
	 * 
	 * @return A boolean representing the isAvailable status of the Book
	 */
	public boolean getIsAvailable() {
		return isAvailable;
	}

	/**
	 * Sets the isAvailable status of the Book
	 * 
	 * @param isAvailable
	 *            A boolean containing the isAvailable status of the Book
	 */
	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	/**
	 * Gets the name of publisher of the Book
	 * 
	 * @return A String representing the name of publisher of the Book
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * Sets the name of publisher of the Book
	 * 
	 * @param publisher
	 *            A String containing the name of publisher of the Book
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * Gets the language of the Book
	 * 
	 * @return A String representing the language of the Book
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language of the Book
	 * 
	 * @param language
	 *            A String containing the language of the Book
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Gets the number of pages of the Book
	 * 
	 * @return An int representing the number of pages of the Book
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * Sets the number of pages of the Book
	 * 
	 * @param pages
	 *            An int containing the number of pages of the Book
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * Gets the date of added of the Book
	 * 
	 * @return A Date representing the date of added of the Book
	 */
	public Date getDateOfAdded() {
		return dateOfAdded;
	}

	/**
	 * Sets the date of added of the Book
	 * 
	 * @param dateOfAdded
	 *            A Date containing the date of added of the Book
	 */
	public void setDateOfAdded(Date dateOfAdded) {
		this.dateOfAdded = dateOfAdded;
	}

	/**
	 * Constructs a Book object.
	 */
	public Book() {

	}

	/**
	 * Constructs a Book with the title, author, ISBN number, isActive status,
	 * isAvailable status, name of publisher, language, number of pages and date of
	 * added.
	 * 
	 * @param title
	 *            The title of the book
	 * @param author
	 *            The author of the book
	 * @param isbn
	 *            The ISBN number of the book
	 * @param isActive
	 *            The isActive status of the book
	 * @param isAvailable
	 *            The isAvailable status of the book
	 * @param publisher
	 *            The name of the publisher of the book
	 * @param language
	 *            The language of the book
	 * @param pages
	 *            The number of pages of the book
	 * @param dateOfAdded
	 *            The date of added of the book
	 */
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

	/**
	 * Returns the String representation of the Book object.
	 * 
	 * @return The String representation of the Book object.
	 */
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", isbn=" + isbn + ", isActive="
				+ isActive + ", isAvailable=" + isAvailable + ", publisher=" + publisher + ", language=" + language
				+ ", pages=" + pages + ", dateOfAdded=" + dateOfAdded + "]";
	}

}
