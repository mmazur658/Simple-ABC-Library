package pl.mazur.simpleabclibrary.service.utils;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;

public interface ReservationServiceUtils {

	Reservation createReservation(Book tempBook, User theUser);

	Message prepareReservationToDeleteAndCreateNewMessage(Reservation reservation, User adminUser);

	void increaseExpirationDate(Reservation reservation);

}
