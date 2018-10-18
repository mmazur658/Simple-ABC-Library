package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.ForbiddenWordsImpl;

class ForbiddenWordsImplTest {

	String forbiddenWord1 = "drop table customers";
	String forbiddenWord2 = "drop table      ";
	String forbiddenWord3 = "    DROP TABLE;";
	String noForbiddenWord = " Drop that Gun!!";
	ForbiddenWordsImpl forbiddenWordsImpl = new ForbiddenWordsImpl();

	@Test
	void containsForbiddenWordTest1() {
		
		assertTrue(forbiddenWordsImpl.findForbiddenWords(forbiddenWord1));
	}

	@Test
	void containsForbiddenWordTest2() {
		
		assertTrue(forbiddenWordsImpl.findForbiddenWords(forbiddenWord2));
	}

	@Test
	void containsForbiddenWordTest3() {
		
		assertTrue(forbiddenWordsImpl.findForbiddenWords(forbiddenWord3));
	}

	@Test
	void doesntContainForbiddenWord() {
		
		assertFalse(forbiddenWordsImpl.findForbiddenWords(noForbiddenWord));
	}

}
