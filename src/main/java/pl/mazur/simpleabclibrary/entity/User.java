package pl.mazur.simpleabclibrary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a User record in the database
 * 
 * @author Marcin Mazur
 */

@Entity
@Table(name = "users")
public class User {

	/**
	 * Unique identification number
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * The first name of the user
	 */
	@Column(name = "first_name")
	private String firstName;

	/**
	 * The last name of the user
	 */
	@Column(name = "last_name")
	private String lastName;

	/**
	 * The email of the user
	 */
	@Column(name = "email")
	private String email;

	/**
	 * The date when the user account has been created.
	 */
	@Column(name = "start_date")
	private Date startDate;

	/**
	 * The PESEL number of the user. This is the national identification number used
	 * in Poland. It always has 11 digits.
	 */
	@Column(name = "pesel")
	private String pesel;

	/**
	 * The street of the user.
	 */
	@Column(name = "street")
	private String street;

	/**
	 * The house number of the user.
	 */
	@Column(name = "house_number")
	private String houseNumber;

	/**
	 * The city of the user.
	 */
	@Column(name = "city")
	private String city;

	/**
	 * The postal code of the user.
	 */
	@Column(name = "postal_code")
	private String postalCode;

	/**
	 * The password of the user.
	 */
	@Column(name = "password")
	private String password;

	/**
	 * The status of the user which determines whether the user is able to log in.
	 */
	@Column(name = "is_active")
	private boolean isActive;

	/**
	 * The one of the user access level.
	 */
	@Column(name = "is_employee")
	private boolean isEmployee;

	/**
	 * The one of the user access level.
	 */
	@Column(name = "is_admin")
	private boolean isAdmin;

	/**
	 * The sex of the user.
	 */
	@Column(name = "sex")
	private String sex;

	/**
	 * The day of birth of the user.
	 */
	@Column(name = "birthday")
	private Date birthday;

	/**
	 * Gets the city of the User
	 * 
	 * @return A String representing the city of the User
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city of the User
	 * 
	 * @param city
	 *            A String containing the city of the User
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the unique identification number of the User
	 * 
	 * @return An int representing the unique identification number of the User
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of the User
	 * 
	 * @param id
	 *            An int containing the unique identification number of the User
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the first name of the User
	 * 
	 * @return A String representing the first name of the User
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the User
	 * 
	 * @param firstName
	 *            A String containing the first name of the User
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name of the User
	 * 
	 * @return A String representing the last name of the User
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the User
	 * 
	 * @param lastName
	 *            A String containing the last name of the User
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the email of the User
	 * 
	 * @return A String representing the email of the User
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the User
	 * 
	 * @param email
	 *            A String containing the email of the User
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the date of added of the User
	 * 
	 * @return A Date representing the date of added of the User
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the date of added of the User
	 * 
	 * @param startDate
	 *            A Date containing the date of added of the User
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the pesel number of the User
	 * 
	 * @return A String representing the pesel number of the User
	 */
	public String getPesel() {
		return pesel;
	}

	/**
	 * Sets the pesel number of the User
	 * 
	 * @param pesel
	 *            A String containing the pesel number of the User
	 */
	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	/**
	 * Gets the street of the User
	 * 
	 * @return A String representing the street of the User
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the street of the User
	 * 
	 * @param street
	 *            A String containing the street of the User
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Gets the house number of the User
	 * 
	 * @return A String representing the house number of the User
	 */
	public String getHouseNumber() {
		return houseNumber;
	}

	/**
	 * Sets the house number of the User
	 * 
	 * @param houseNumber
	 *            A String containing the house number of the User
	 */
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * Gets the password of the User
	 * 
	 * @return A String representing the password of the User
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the User
	 * 
	 * @param password
	 *            A String containing the password of the User
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the isActive status of the User
	 * 
	 * @return A boolean representing the isActive status of the User
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets the isActive status of the User
	 * 
	 * @param isActive
	 *            A boolean containing the isActive status of the User
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the isEmployee status of the User
	 * 
	 * @return A boolean representing the isEmployee status of the User
	 */
	public boolean getIsEmployee() {
		return isEmployee;
	}

	/**
	 * Sets the isEmployee status of the User
	 * 
	 * @param isEmployee
	 *            A boolean containing the isEmployee status of the User
	 */
	public void setIsEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	/**
	 * Gets the isAdmin status of the User
	 * 
	 * @return A boolean representing the isAdmin status of the User
	 */
	public boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * Sets the isAdmin status of the User
	 * 
	 * @param isAdmin
	 *            A boolean containing the isAdmin status of the User
	 */
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * Gets the postal code of the User
	 * 
	 * @return A String representing the postal code of the User
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Sets the postal code of the User
	 * 
	 * @param postalCode
	 *            A String containing the postal code of the User
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Gets the sex of the User
	 * 
	 * @return A String representing the sex of the User
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Sets the sex of the User
	 * 
	 * @param sex
	 *            A String containing the sex of the User
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Gets the date of birth of the User
	 * 
	 * @return A Date representing the date of birth of the User
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * Sets the date of birth of the User
	 * 
	 * @param birthday
	 *            A Date containing the date of birth of the User
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * Constructs a User object.
	 */
	public User() {

	}

	/**
	 * Constructs a User with the first and last name, last name, email, date of
	 * added, PESEL number, street, house number, city, postal code, password,
	 * isActive status, isEmployee status, isAdmin status, sex and date of birth.
	 * 
	 * @param firstName
	 *            The first name of the user
	 * @param lastName
	 *            The last name of the user
	 * @param email
	 *            The email of the user
	 * @param startDate
	 *            The date of added
	 * @param pesel
	 *            The PESEL number of the user
	 * @param street
	 *            The street of the user
	 * @param houseNumber
	 *            The house of the user
	 * @param city
	 *            The city of the user
	 * @param postalCode
	 *            The postal code of the user
	 * @param password
	 *            The password of the user
	 * @param isActive
	 *            The isActive status of the user
	 * @param isEmployee
	 *            The isEmployee status of the user
	 * @param isAdmin
	 *            The isAdmin status of the user
	 * @param sex
	 *            The sex of the user
	 * @param birthday
	 *            The date of birth of the user
	 */
	public User(String firstName, String lastName, String email, Date startDate, String pesel, String street,
			String houseNumber, String city, String postalCode, String password, boolean isActive, boolean isEmployee,
			boolean isAdmin, String sex, Date birthday) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.startDate = startDate;
		this.pesel = pesel;
		this.street = street;
		this.houseNumber = houseNumber;
		this.city = city;
		this.postalCode = postalCode;
		this.password = password;
		this.isActive = isActive;
		this.isEmployee = isEmployee;
		this.isAdmin = isAdmin;
		this.sex = sex;
		this.birthday = birthday;
	}

	/**
	 * Returns the String representation of the User object.
	 * 
	 * @return The String representation of the User object.
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", startDate=" + startDate + ", pesel=" + pesel + ", street=" + street + ", houseNumber="
				+ houseNumber + ", city=" + city + ", postalCode=" + postalCode + ", password=" + password
				+ ", isActive=" + isActive + ", isEmployee=" + isEmployee + ", isAdmin=" + isAdmin + ", sex=" + sex
				+ ", birthday=" + birthday + "]";
	}
}
