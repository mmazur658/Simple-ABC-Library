package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.AccessLevelControlImpl;

class LoginAndAccessLevelCheckImplTest {

	AccessLevelControlImpl loginAndAccessLevelCheckImpl = new AccessLevelControlImpl();

	Integer userId;
	String userAccessLevel;

	@Test
	void isLoginOK() {
		
		userId = new Integer("5");
		
		assertTrue(loginAndAccessLevelCheckImpl.loginCheck(userId));
	}

	@Test
	void isLoginNotOK() {
		
		userId = null;
		
		assertFalse(loginAndAccessLevelCheckImpl.loginCheck(userId));
	}

	@Test
	void isAdmin() {
		
		userAccessLevel = "Administrator";
		
		assertTrue(loginAndAccessLevelCheckImpl.isAdmin(userAccessLevel));
	}

	@Test
	void isNotAdmin() {
		
		userAccessLevel = "Employee";
		
		assertFalse(loginAndAccessLevelCheckImpl.isAdmin(userAccessLevel));
	}

	@Test
	void isEmployee() {
		
		userAccessLevel = "Employee";
		
		assertTrue(loginAndAccessLevelCheckImpl.isEmployee(userAccessLevel));
	}

	@Test
	void isNotEmployee() {
		
		userAccessLevel = "Boss";
		
		assertFalse(loginAndAccessLevelCheckImpl.isEmployee(userAccessLevel));
	}

	@Test
	void isCustomer() {
		
		userAccessLevel = "Customer";
		
		assertTrue(loginAndAccessLevelCheckImpl.isCustomer(userAccessLevel));
	}

	@Test
	void isNotCustomer() {
		
		userAccessLevel = null;
		
		assertFalse(loginAndAccessLevelCheckImpl.isCustomer(userAccessLevel));
	}
}
