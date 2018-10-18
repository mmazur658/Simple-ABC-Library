package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import pl.mazur.simpleabclibrary.utils.ReservationControl;

class ReservationControlTest {

	ReservationControl reservationControl = new ReservationControl();
	Calendar theCal = Calendar.getInstance();

	@Test
	void shouldReturnTrueIfReservationIsExpired() {

		theCal.add(Calendar.DATE, -3);
		Date theDate = theCal.getTime();

		assertTrue(reservationControl.isReservationExpired(theDate));
	}

	@Test
	void shouldReturnFalseIfReservationIsNotExpired() {
		
		theCal.add(Calendar.DATE, -1);
		Date theDate = theCal.getTime()
				;
		assertFalse(reservationControl.isReservationExpired(theDate));
	}

}
