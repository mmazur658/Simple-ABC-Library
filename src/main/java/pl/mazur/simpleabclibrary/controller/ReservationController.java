package pl.mazur.simpleabclibrary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

@Controller
@Scope("session")
@RequestMapping("/reservation")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:library-configuration.properties")
public class ReservationController {

	@Autowired
	private AccessLevelControl accessLevelControl;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private Environment env;

	@Autowired
	private SearchEngineUtils searchEngineUtils;

	@RequestMapping("/reservation-management")
	public String reservationPage(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "customerId") String reservationCustomerId,
			@RequestParam(required = false, name = "customerFirstName") String reservationCustomerFirstName,
			@RequestParam(required = false, name = "customerLastName") String reservationCustomerLastName,
			@RequestParam(required = false, name = "customerPesel") String reservationCustomerPesel,
			@RequestParam(required = false, name = "bookId") String reservationBookId,
			@RequestParam(required = false, name = "bookTitle") String reservationBookTitle,
			@RequestParam(required = false, name = "startResult") Integer reservationStartResult,
			HttpServletRequest request, Model theModel) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "reservationCustomerId", "reservationCustomerFirstName", "reservationCustomerLastName", "reservationCustomerPesel",
				"reservationManagementBookId", "reservationBookTitle" };
		String[] searchParametersValue = { reservationCustomerId, reservationCustomerFirstName, reservationCustomerLastName, reservationCustomerPesel, reservationBookId,
				reservationBookTitle };
		String[] reservationSearchParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);

		if (reservationStartResult == null)
			reservationStartResult = (Integer) session.getAttribute("reservationStartResult");
		if (reservationStartResult == null)
			reservationStartResult = 0;
		session.setAttribute("reservationStartResult", reservationStartResult);

		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(reservationSearchParameters);
		List<Reservation> reservationList = hasAnyParameters
				? reservationService.reservationSearchResult(reservationSearchParameters, reservationStartResult)
				: reservationService.getAllReservation(reservationStartResult);
		long amountOfResults = hasAnyParameters
				? reservationService.getAmountOfSearchResult(reservationSearchParameters)
				: reservationService.getAmountOfAllReservation();

		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(reservationStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(reservationStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(reservationStartResult, searchResultLimit);

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

		String[] searchParametersName = { "reservationCustomerId", "reservationCustomerFirstName", "reservationCustomerLastName", "reservationCustomerPesel",
				"reservationManagementBookId", "reservationBookTitle" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/reservation/reservation-management";
	}

	@RequestMapping("/delete-reservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.deleteReservationByEmployee(reservationId);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty("controller.ReservationController.deleteReservation.success.1"));

		return "redirect:/reservation/reservation-management";
	}

	@RequestMapping("/increase-exp-date")
	public String increaseExpDate(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.increaseExpirationDate(reservationId);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty("controller.ReservationController.increaseExpDate.success.1"));

		return "redirect:/reservation/reservation-management";
	}
}