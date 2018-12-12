package pl.mazur.simpleabclibrary.controller;

import java.util.List;
import java.util.Locale;

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

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"reservation-management"</li>
 * </ul>
 * 
 * <br>
 * <br>
 * 
 * This controller also perform the actions on the reservations.
 * 
 * @author Marcin Mazur
 *
 */
@Controller
@Scope("session")
@RequestMapping("/reservation")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class ReservationController {

	/**
	 * The AccessLevelControl interface
	 */
	private AccessLevelControl accessLevelControl;

	/**
	 * The ReservationService interface
	 */
	private ReservationService reservationService;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * Constructs a ReservationController with the AccessLevelControl,
	 * ReservationService, Environment and SearchEngineUtils.
	 * 
	 * @param accessLevelControl
	 *            The AccessLevelControl interface
	 * @param reservationService
	 *            The ReservationService interface
	 * @param env
	 *            The Environment interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 */
	@Autowired
	public ReservationController(AccessLevelControl accessLevelControl, ReservationService reservationService,
			Environment env, SearchEngineUtils searchEngineUtils) {
		this.accessLevelControl = accessLevelControl;
		this.reservationService = reservationService;
		this.env = env;
		this.searchEngineUtils = searchEngineUtils;

	}

	/**
	 * 
	 * Returns the view of "reservation-management" with model attributes:<br>
	 * <ul>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>startResult - The value of first index of the results</li>
	 * <li>reservationList - The list of Reservation objects</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * <li>amountOfResults - The number of total results</li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param reservationCustomerId
	 *            The String containing the id of the user
	 * @param reservationCustomerFirstName
	 *            The String containing the first name of the user
	 * @param reservationCustomerLastName
	 *            The String containing the last name of the user
	 * @param reservationCustomerPesel
	 *            The String containing the PESEL of the user
	 * @param reservationBookId
	 *            The String containing the id of the book
	 * @param reservationBookTitle
	 *            The String containing the title of the book
	 * @param reservationStartResult
	 *            The String containing the The Integer containing the first index
	 *            of the results
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/reservation-management")
	public String showReservationPage(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "customerId") String reservationCustomerId,
			@RequestParam(required = false, name = "customerFirstName") String reservationCustomerFirstName,
			@RequestParam(required = false, name = "customerLastName") String reservationCustomerLastName,
			@RequestParam(required = false, name = "customerPesel") String reservationCustomerPesel,
			@RequestParam(required = false, name = "bookId") String reservationBookId,
			@RequestParam(required = false, name = "bookTitle") String reservationBookTitle,
			@RequestParam(required = false, name = "startResult") Integer reservationStartResult,
			HttpServletRequest request, Model theModel) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// The Arrays containing the names and values of search parameters
		String[] searchParametersName = { "reservationCustomerId", "reservationCustomerFirstName",
				"reservationCustomerLastName", "reservationCustomerPesel", "reservationManagementBookId",
				"reservationBookTitle" };
		String[] searchParametersValue = { reservationCustomerId, reservationCustomerFirstName,
				reservationCustomerLastName, reservationCustomerPesel, reservationBookId, reservationBookTitle };

		// The Array containing the search parameters ready to search
		String[] reservationSearchParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);

		// Get the reservationStartResult from the session. If session doesn't contain
		// that attribute set default value
		if (reservationStartResult == null)
			reservationStartResult = (Integer) session.getAttribute("reservationStartResult");
		if (reservationStartResult == null)
			reservationStartResult = 0;
		session.setAttribute("reservationStartResult", reservationStartResult);

		// Check whether the reservationSearchParameters array contains any parameters
		// and get the results and number of the results.
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(reservationSearchParameters);
		List<Reservation> reservationList = hasAnyParameters
				? reservationService.getListOfReservationForGivenSearchParams(reservationSearchParameters,
						reservationStartResult)
				: reservationService.getListOfAllReservations(reservationStartResult);
		long amountOfResults = reservationList.size();

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(reservationStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(reservationStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(reservationStartResult, searchResultLimit);

		// Set model and session attributes
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

	/**
	 * Cleans the search parameters and redirects to the view of
	 * "reservation-management".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/clearReservationSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clear search parameters
		String[] searchParametersName = { "reservationCustomerId", "reservationCustomerFirstName",
				"reservationCustomerLastName", "reservationCustomerPesel", "reservationManagementBookId",
				"reservationBookTitle" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/reservation/reservation-management";
	}

	/**
	 * Deletes the reservation with the given id and redirects to the view of ""
	 * with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param reservationId
	 *            The int containing the id of the reservation
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/delete-reservation")
	public String deleteReservation(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.deleteReservationByEmployee(reservationId);

		// Set redirect attributes
		redirectAttributes.addAttribute("systemMessage", env
				.getProperty(locale.getLanguage() + ".controller.ReservationController.deleteReservation.success.1"));

		return "redirect:/reservation/reservation-management";
	}

	/**
	 * Changes the expiration date of the reservation and redirects to the view of
	 * "reservation-management" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param reservationId
	 *            The the containing the id of the reservation
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/increase-exp-date")
	public String increaseExpDate(@RequestParam("reservationId") int reservationId, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get user's session and check whether the user is permitted to see this view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		reservationService.increaseExpirationDate(reservationId);

		// Set redirect attributes
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty(locale.getLanguage() + ".controller.ReservationController.increaseExpDate.success.1"));

		return "redirect:/reservation/reservation-management";
	}
}