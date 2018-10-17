package pl.mazur.simpleabclibrary.servicetest;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.service.ReservationServiceImpl;

class ReservationServiceImplTest {

	ReservationServiceImpl reservationServiceImpl = new ReservationServiceImpl();
	
	String[] reservationSearchParameters = {"","Marcin","","","Pan Tadeusz",""};
	String  searchType= "select count(*) from Reservation where ";
	
	@Test
	void shouldCreateHqlUsingSearchParameters() {
		
	}

	
	@Test
	void shouldReturnFalseWhenHqlsArentTheSame() {
		
	}
}
