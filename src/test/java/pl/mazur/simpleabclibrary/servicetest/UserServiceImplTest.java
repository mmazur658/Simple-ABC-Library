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
		
	}
	
	@Test
	void shouldReturnFalseWhenHqlsArentTheSame() {
		
	}

}
