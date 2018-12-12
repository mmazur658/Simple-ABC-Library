package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.PasswordUtilsImpl;

class PasswordUtilImplTest {

	String password = "HLop78^%";
	String encryptedPassword;
	PasswordUtilsImpl passwordUtilImpl = new PasswordUtilsImpl();

	@Test
	void shouldReturnTrueWhenPasswordsAreTheSame() {

		encryptedPassword = passwordUtilImpl.encryptPassword(password);

		assertTrue(passwordUtilImpl.isPasswordCorrect(password, encryptedPassword));

	}

	@Test
	void shouldReturnFalseWhenPasswordsArentTheSame() {

		encryptedPassword = passwordUtilImpl.encryptPassword(password);

		assertFalse(passwordUtilImpl.isPasswordCorrect("P@ssw0rd", encryptedPassword));

	}

}
