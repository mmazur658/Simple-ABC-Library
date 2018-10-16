package pl.mazur.simpleabclibrary.servicetest;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.service.UserServiceImpl;

class UserServiceImplTest {
	
	UserServiceImpl userServiceImpl = new UserServiceImpl();

	String[] userSearchParameters = {"5","Marcin","","",""};
	String  searchType= "SELECT COUNT(*) FROM User where ";
	
	@Test
	void shouldCreateHqlUsingSearchParameters() {
		String expectedHql = "SELECT COUNT(*) FROM User where id like '%5%' AND firstName like '%Marcin%' AND isActive = true ORDER BY id ASC";
		assertEquals(expectedHql, userServiceImpl.prepareHqlUsingUserSearchParameters(userSearchParameters, searchType));
	}
	
	@Test
	void shouldReturnFalseWhenHqlsArentTheSame() {
		String incorrectHql = "SELECT COUNT(*) FROM User where id like '%5%' AND firstName like '%Marcin%' AND lastName '%Mazur%' ANDisActive = true ORDER BY id ASC";
		assertFalse(incorrectHql.equals(userServiceImpl.prepareHqlUsingUserSearchParameters(userSearchParameters, searchType)));
	}

}
