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

import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.utils.ForbiddenWords;
import pl.mazur.simpleabclibrary.utils.LoginAndAccessLevelCheck;

@Controller
@Scope("session")
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	LoginAndAccessLevelCheck loginAndAccessLevelCheck;

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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		if (!(customerId == null))
			session.setAttribute("customerId", customerId);
		if (!(customerFirstName == null))
			session.setAttribute("customerFirstName", customerFirstName);
		if (!(customerLastName == null))
			session.setAttribute("customerLastName", customerLastName);
		if (!(customerPesel == null))
			session.setAttribute("customerPesel", customerPesel);
		if (!(bookId == null))
			session.setAttribute("bookId", bookId);
		if (!(bookTitle == null))
			session.setAttribute("bookTitle", bookTitle);

		if ((customerId == null) && !(session.getAttribute("customerId") == null))
			customerId = (String) session.getAttribute("customerId");
		if ((customerFirstName == null) && !(session.getAttribute("customerFirstName") == null))
			customerFirstName = (String) session.getAttribute("customerFirstName");
		if ((customerLastName == null) && !(session.getAttribute("customerLastName") == null))
			customerLastName = (String) session.getAttribute("customerLastName");
		if ((customerPesel == null) && !(session.getAttribute("customerPesel") == null))
			customerPesel = (String) session.getAttribute("customerPesel");
		if ((bookId == null) && !(session.getAttribute("bookId") == null))
			bookId = (String) session.getAttribute("bookId");
		if ((bookTitle == null) && !(session.getAttribute("bookTitle") == null))
			bookTitle = (String) session.getAttribute("bookTitle");

		String[] reservationSearchParameters = { "", "", "", "", "", "" };
		reservationSearchParameters[0] = (customerId == null) ? "" : customerId;
		reservationSearchParameters[1] = (customerFirstName == null) ? "" : customerFirstName;
		reservationSearchParameters[2] = (customerLastName == null) ? "" : customerLastName;
		reservationSearchParameters[3] = (customerPesel == null) ? "" : customerPesel;
		reservationSearchParameters[4] = (bookId == null) ? "" : bookId;
		reservationSearchParameters[5] = (bookTitle == null) ? "" : bookTitle;
		
		for (String word:reservationSearchParameters) {
			if(forbiddenWords.findForbiddenWords(word)) {
				return "redirect:/error";
			}
		}

		List<Reservation> reservationList;
		long amountOfResults;

		if (reservationStartResult == null)
			reservationStartResult = (Integer) session.getAttribute("reservationStartResult");
		if (reservationStartResult == null)
			reservationStartResult = 0;

		if (!reservationSearchParameters[0].equals("") || !reservationSearchParameters[1].equals("")
				|| !reservationSearchParameters[2].equals("") || !reservationSearchParameters[3].equals("")
				|| !reservationSearchParameters[4].equals("") || !reservationSearchParameters[5].equals("")) {
			amountOfResults = reservationService.getAmountOfSearchResult(reservationSearchParameters);
			reservationList = reservationService.reservationSearchResult(reservationSearchParameters,
					reservationStartResult);
		} else {
			amountOfResults = reservationService.getAmountOfAllReservation();
			reservationList = reservationService.getAllReservation(reservationStartResult);
		}

		String resultRange;
		long showMoreLinkValue = 0;
		if ((reservationStartResult + 10) > amountOfResults) {
			showMoreLinkValue = reservationStartResult;
			resultRange = "Wyniki od " + (reservationStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = reservationStartResult + 10;
			resultRange = "Wyniki od " + (reservationStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((reservationStartResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = reservationStartResult - 10;
		}

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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		session.setAttribute("customerId", null);
		session.setAttribute("customerFirstName", null);
		session.setAttribute("customerLastName", null);
		session.setAttribute("customerPesel", null);
		session.setAttribute("bookId", null);
		session.setAttribute("bookTitle", null);
		session.setAttribute("reservationStartResult", null);

		return "redirect:/return-book/return-book-choose-books";
	}

	@RequestMapping("/delete-reservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		reservationService.deleteReservationByEmployee(reservationId);
		
		String systemMessage = "Rezerwacja zosta³a usuniêta";

		redirectAttributes.addAttribute("systemMessage", systemMessage);

		return "redirect:/reservation/reservation-management";
	}

	@RequestMapping("/increase-exp-date")
	public String increaseExpDate(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		reservationService.increaseExpirationDate(reservationId);
		String systemMessage = "Czas rezerwacji wyd³u¿ony o 24h";

		redirectAttributes.addAttribute("systemMessage", systemMessage);

		return "redirect:/reservation/reservation-management";
	}

}
