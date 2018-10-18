package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.PasswordValidatorImpl;

class PasswordValidatorImplTest {

	String validPassword = "HLop78^%";
	String invalidPassword = "admin123";
	PasswordValidatorImpl passwordValidatorImpl = new PasswordValidatorImpl();

	@Test
	void shouldReturnFalseWhenPasswordIsInvalid() {
		
		assertFalse(passwordValidatorImpl.validate(invalidPassword));
	}

	@Test
	void shouldReturnFalseWhenPasswordIsValid() {
		
		assertTrue(passwordValidatorImpl.validate(validPassword));
	}

}
