package pl.mazur.simpleabclibrary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "pesel")
	private String pesel;

	@Column(name = "street")
	private String street;

	@Column(name = "house_number")
	private String houseNumber;

	@Column(name = "city")
	private String city;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "password")
	private String password;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "is_employee")
	private boolean isEmployee;

	@Column(name = "is_admin")
	private boolean isAdmin;

	@Column(name = "sex")
	private String sex;

	@Column(name = "birthday")
	private Date birthday;

	public User() {

	}

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

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", startDate=" + startDate + ", pesel=" + pesel + ", street=" + street + ", houseNumber="
				+ houseNumber + ", city=" + city + ", postalCode=" + postalCode + ", password=" + password
				+ ", isActive=" + isActive + ", isEmployee=" + isEmployee + ", isAdmin=" + isAdmin + ", sex=" + sex
				+ ", birthday=" + birthday + "]";
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isEmployee() {
		return isEmployee;
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
