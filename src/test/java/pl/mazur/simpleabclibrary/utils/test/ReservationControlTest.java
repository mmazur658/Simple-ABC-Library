package pl.mazur.simpleabclibrary.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.ReservationControl;

class ReservationControlTest {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReservationService reservationService;

	ReservationControl reservationControl = new ReservationControl(messageService, userService, reservationService);

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
		Date theDate = theCal.getTime();
		assertFalse(reservationControl.isReservationExpired(theDate));
	}

}
