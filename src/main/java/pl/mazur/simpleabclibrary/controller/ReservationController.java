package pl.mazur.simpleabclibrary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;

@Controller
@Scope("session")
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	AccessLevelControl accessLevelControl;

	@Autowired
	ReservationService reservationService;

	@Autowired
	ForbiddenWords forbiddenWords;

	@RequestMapping("/reservation-management")
	public String reservationPage(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "customerId") String customerId,
			@RequestParam(required = false, name = "customerFirstName") String customerFirstName,
			@RequestParam(required = false, name = "customerLastName") String customerLastName,
			@RequestParam(required = false, name = "customerPesel") String customerPesel,
			@RequestParam(required = false, name = "bookId") String bookId,
			@RequestParam(required = false, name = "bookTitle") String bookTitle,
			@RequestParam(required = false, name = "startResult") Integer reservationStartResult,
			HttpServletRequest request, Model theModel) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] reservationSearchParameters = reservationService.prepareTableToSearch(session, customerId,
				customerFirstName, customerLastName, customerPesel, bookId, bookTitle, reservationStartResult);

		reservationStartResult = (reservationStartResult == null)
				? ((session.getAttribute("reservationStartResult") != null)
						? (Integer) session.getAttribute("reservationStartResult")
						: 0)
				: 0;
		session.setAttribute("reservationStartResult", reservationStartResult);

		boolean hasAnyParameters = reservationService.hasTableAnyParameters(reservationSearchParameters);
		List<Reservation> reservationList = hasAnyParameters
				? reservationService.reservationSearchResult(reservationSearchParameters, reservationStartResult)
				: reservationService.getAllReservation(reservationStartResult);
		long amountOfResults = hasAnyParameters
				? reservationService.getAmountOfSearchResult(reservationSearchParameters)
				: reservationService.getAmountOfAllReservation();

		long showMoreLinkValue = reservationService.generateShowMoreLinkValue(reservationStartResult, amountOfResults);
		String resultRange = reservationService.generateResultRange(reservationStartResult, amountOfResults,
				showMoreLinkValue);
		long showLessLinkValue = reservationService.generateShowLessLinkValue(reservationStartResult);

		session.setAttribute("reservationStartResult", reservationStartResult);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("startResult", reservationStartResult);
		theModel.addAttribute("reservationList", reservationList);
		theModel.addAttribute("systemMessage", systemMessage);
		theModel.addAttribute("amountOfResults", amountOfResults);

		return "reservation-management";
	}

	@RequestMapping("/clearReservationSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.clearReservationSearchParameters(session);

		return "redirect:/reservation/reservation-management";
	}

	@RequestMapping("/delete-reservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.deleteReservationByEmployee(reservationId);
		redirectAttributes.addAttribute("systemMessage", "Rezerwacja zosta³a usuniêta");

		return "redirect:/reservation/reservation-management";
	}

	@RequestMapping("/increase-exp-date")
	public String increaseExpDate(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.increaseExpirationDate(reservationId);
		redirectAttributes.addAttribute("systemMessage", "Czas rezerwacji wyd³u¿ony o 24h");

		return "redirect:/reservation/reservation-management";
	}
}