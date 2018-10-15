package pl.mazur.simpleabclibrary.utilstest;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.*;

class PeselValidatorImplTest {

	private PeselValidatorImpl peselValidatorImpl = new PeselValidatorImpl();

	String correctPesel = "92071314764";
	String incorrectPesel = "910902515836";

	@Test
	void shouldReturnTrueOnCorrectPesel() {
		assertTrue(peselValidatorImpl.validatePesel(correctPesel));
	}

	@Test
	void shouldReturnFalseOnIncorrectPesel() {
		assertFalse(peselValidatorImpl.validatePesel(incorrectPesel));
	}

	@Test
	void shouldReturnBirthDate() {

		String expectedBirthDate = "1992-07-13";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		assertEquals(expectedBirthDate, sdf.format(peselValidatorImpl.getBirthDate(correctPesel)));

	}

	@Test
	void shouldReturnBirthYear() {

		peselValidatorImpl.validatePesel(correctPesel);
		int expectedYear = 1992;

		assertEquals(expectedYear, peselValidatorImpl.getBirthYear());
	}

	@Test
	void shouldReturnBirthMonth() {

		peselValidatorImpl.validatePesel(correctPesel);
		int expectedMonth = 07;

		assertEquals(expectedMonth, peselValidatorImpl.getBirthMonth());
	}

	@Test
	void shouldReturnBirthDay() {

		peselValidatorImpl.validatePesel(correctPesel);
		int expectedDay = 13;

		assertEquals(expectedDay, peselValidatorImpl.getBirthDay());
	}

	@Test
	void shouldReturnSex() {

		String expectedSex = "Kobieta";
		
		assertEquals(expectedSex, peselValidatorImpl.getSex(correctPesel));

	}

}
