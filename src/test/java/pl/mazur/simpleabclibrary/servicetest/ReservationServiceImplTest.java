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
		String expectedHQL = "select count(*) from Reservation where user.firstName like '%Marcin%' AND book.id like '%Pan Tadeusz%' AND isActive = true ORDER BY id ASC";	
		assertEquals(expectedHQL, reservationServiceImpl.prepareHqlUsingReservationSearchParameters(reservationSearchParameters, searchType));
	}

	
	@Test
	void shouldReturnFalseWhenHqlsArentTheSame() {
		String incorrectHQL = "select count(*) from Reservation where user.id like '%6%' AND user.firstName like '%Marcin%' AND book.id like '%Pan Tadeusz%' AND isActive = true ORDER BY id ASC";
		assertFalse(incorrectHQL.equals(reservationServiceImpl.prepareHqlUsingReservationSearchParameters(reservationSearchParameters, searchType)));
	}
}
