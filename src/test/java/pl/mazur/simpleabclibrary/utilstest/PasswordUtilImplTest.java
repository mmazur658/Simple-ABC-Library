package pl.mazur.simpleabclibrary.utilstest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.PasswordUtilImpl;

class PasswordUtilImplTest {

	String password = "HLop78^%";
	String encryptedPassword;
	PasswordUtilImpl passwordUtilImpl = new PasswordUtilImpl();

	@Test
	void shouldReturnTrueWhenPasswordsAreTheSame() {

		encryptedPassword = passwordUtilImpl.encryptPassword(password);

		assertTrue(passwordUtilImpl.checkPassword(password, encryptedPassword));

	}

	@Test
	void shouldReturnFalseWhenPasswordsArentTheSame() {

		encryptedPassword = passwordUtilImpl.encryptPassword(password);

		assertFalse(passwordUtilImpl.checkPassword("P@ssw0rd", encryptedPassword));

	}

}
