package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.ForbiddenWordsImpl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtilsImpl;

class SearchEngineUtilsImplTest {

	long showMoreLinkValue;
	Integer startResult;
	int range;
	long amountOfResults;
	String[] searchParametersName = { "Param1", "Param2", "Param3" };
	String[] searchParametersValue = { "15", "", "Marcin" };
	String[] searchParameters = { "", "", "", "", "", "", "" };
	String searchType;
	protected MockHttpSession session;
	protected MockHttpServletRequest request;

	ForbiddenWords forbiddenWords = new ForbiddenWordsImpl();

	SearchEngineUtilsImpl searchEngineUtilsImpl = new SearchEngineUtilsImpl(forbiddenWords);

	@Test
	void tableHasNoParamters() {
		assertFalse(searchEngineUtilsImpl.hasTableAnyParameters(searchParameters));
	}

	@Test
	void tableHasParameters() {
		searchParameters[2] = "Marcin";
		assertTrue(searchEngineUtilsImpl.hasTableAnyParameters(searchParameters));
	}

	@Test
	void shouldGenerateShowMoreLinkValueTest1() {
		startResult = 10;
		range = 10;
		amountOfResults = 17;
		assertEquals(10, searchEngineUtilsImpl.generateShowMoreLinkValue(startResult, amountOfResults, range));
	}

	@Test
	void shouldGenerateShowLessLinkValueTest1() {
		startResult = 0;
		range = 10;
		assertEquals(0, searchEngineUtilsImpl.generateShowLessLinkValue(startResult, range));
	}

	@Test
	void shouldGenerateResultRangeTest1() {
		showMoreLinkValue = 17;
		startResult = 10;
		range = 10;
		amountOfResults = 17;
		String expectedString = "Wyniki od 11 do 17";
		assertEquals(expectedString,
				searchEngineUtilsImpl.generateResultRange(startResult, amountOfResults, showMoreLinkValue, range));
	}

	@Test
	void shouldGenerateShowMoreLinkValueTest2() {
		startResult = 10;
		range = 10;
		amountOfResults = 50;
		assertEquals(20, searchEngineUtilsImpl.generateShowMoreLinkValue(startResult, amountOfResults, range));
	}

	@Test
	void shouldGenerateShowLessLinkValueTest2() {
		startResult = 30;
		range = 10;
		assertEquals(20, searchEngineUtilsImpl.generateShowLessLinkValue(startResult, range));
	}

	@Test
	void shouldGenerateResultRangeTest2() {
		showMoreLinkValue = 20;
		startResult = 10;
		range = 10;
		amountOfResults = 50;
		String expectedString = "Wyniki od 11 do 20";
		assertEquals(expectedString,
				searchEngineUtilsImpl.generateResultRange(startResult, amountOfResults, showMoreLinkValue, range));
	}

	@Test
	void shouldClearSearchParameters() {

		session = new MockHttpSession();
		session.setAttribute("Param1", 100);
		session.setAttribute("Param2", "Java");
		session.setAttribute("Param3", 2500);

		searchEngineUtilsImpl.clearSearchParameters(session, searchParametersName);

		assertNull(session.getAttribute("Param1"));
		assertNull(session.getAttribute("Param2"));
		assertNull(session.getAttribute("Param3"));
	}

	@Test
	void shouldReturnPreparedTableToSearch() {

		session = new MockHttpSession();

		String[] expectedSearchParametersTable = { "15", "", "Marcin" };

		assertArrayEquals(expectedSearchParametersTable,
				searchEngineUtilsImpl.prepareTableToSearch(session, searchParametersName, searchParametersValue));
		assertEquals(expectedSearchParametersTable[0], session.getAttribute("Param1"));
		assertEquals(expectedSearchParametersTable[1], session.getAttribute("Param2"));
		assertEquals(expectedSearchParametersTable[2], session.getAttribute("Param3"));

	}

	@Test
	void shouldReturHqlUsingSearchParameters() {
		searchType = "from User where ";
		String expectedHql = searchType+"Param1 like '%15%' AND Param3 like '%Marcin%' AND isActive = true ORDER BY id ASC";
		assertEquals(expectedHql, searchEngineUtilsImpl.prepareHqlUsingSearchParameters(searchParametersValue, searchType, searchParametersName));
		
	}

}