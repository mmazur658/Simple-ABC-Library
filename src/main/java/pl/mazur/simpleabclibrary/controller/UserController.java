package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.BorrowedBook;
import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.PasswordValidator;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"login-page"</li>
 * <li>"main-page"</li>
 * <li>"create-user-form"</li>
 * <li>"confirm-page"</li>
 * <li>"user-update"</li>
 * <li>"change-password-form"</li>
 * <li>"admin-panel"</li>
 * <li>"user-management"</li>
 * <li>"user-details"</li>
 * <li>"user-details-management"</li>
 * <li>"users-books"</li>
 * </ul>
 * 
 * <br>
 * <br>
 * 
 * This controller also perform the actions on the users.
 * 
 * @author Marcin Mazur
 *
 */
@Controller
@RequestMapping("/user")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class UserController {

	/**
	 * The array containing the names of user search parameters
	 */
	private final String[] NAMES_OF_USER_SEARCH_PARAMETERS = { "userManagementSelectedUserId",
			"userManagementFirstName", "userManagementLastName", "userManagementEmail", "userManagementPesel" };

	/**
	 * The array containing the names of user management search parameters
	 */
	private final String[] NAMES_OF_USER_MANAGEMENT_SEARCH_PARAMETERS = { "userManagementStartResult",
			"userManagementSelectedUserId", "userManagementFirstName", "userManagementLastName", "userManagementEmail",
			"userManagementPesel" };
	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * The PdfService interface
	 */
	private PdfService pdfService;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The AccessLevelControl interface
	 */
	private AccessLevelControl accessLevelControl;

	/**
	 * The ReservationService interface
	 */
	private ReservationService reservationService;

	/**
	 * The BookService interface
	 */
	private BookService bookService;

	/**
	 * The PasswordValidator interface
	 */
	private PasswordValidator passwordValidator;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * Constructs a UserController with the UserService, PdfService, Environment,
	 * AccessLevelControl, ReservationService, BookService, PasswordValidator and
	 * SearchEngineUtils.
	 * 
	 * @param userService
	 *            The UserService interface
	 * @param pdfService
	 *            PdfService interface
	 * @param env
	 *            Environment interface
	 * @param accessLevelControl
	 *            The AccessLevelControl interface
	 * @param reservationService
	 *            The ReservationService interface
	 * @param bookService
	 *            The BookService interface
	 * @param passwordValidator
	 *            The PasswordValidator interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 */
	@Autowired
	public UserController(UserService userService, PdfService pdfService, Environment env,
			AccessLevelControl accessLevelControl, ReservationService reservationService, BookService bookService,
			PasswordValidator passwordValidator, SearchEngineUtils searchEngineUtils) {

		this.userService = userService;
		this.pdfService = pdfService;
		this.env = env;
		this.accessLevelControl = accessLevelControl;
		this.reservationService = reservationService;
		this.bookService = bookService;
		this.passwordValidator = passwordValidator;
		this.searchEngineUtils = searchEngineUtils;
	}

	/**
	 * Returns the view of "login-page" with model attributes:<br>
	 * <ul>
	 * <li>incorrectPasswordMessage - The one of the system messages</li>
	 * 
	 * </ul>
	 * 
	 * @param incorrectPasswordMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view The String representing
	 *         the name of the view
	 */
	@RequestMapping("/login-page")
	public String showLoginPage(
			@RequestParam(required = false, name = "incorrectPasswordMessage") String incorrectPasswordMessage,
			Model theModel, HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		theModel.addAttribute("incorrectPasswordMessage", incorrectPasswordMessage);

		// If session contains the attribute "userId" then redirect to main
		if (session.getAttribute("userId") != null) {
			return "redirect:/user/main";
		} else {
			return "login-page";
		}
	}

	/**
	 * Validates email and password and redirects to the view of "login-page" or
	 * "main" with redirect attributes:<br>
	 * <ul>
	 * <li>incorrectPasswordMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param email
	 *            The String containing the email of the user
	 * @param locale
	 *            The Locale containing the user's locale
	 * @param thePasswordFromForm
	 *            The String containing the password from the form
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @return The String representing the name of the view The String representing
	 *         the name of the view
	 */
	@RequestMapping("/login")
	public String loginVerifying(@RequestParam("email") String email, Locale locale,
			@RequestParam("password") String thePasswordFromForm, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		// Get user's session
		HttpSession session = request.getSession();

		// Check the login whether is correct
		boolean isLoginOK = false;
		try {
			isLoginOK = userService.isEmailAndPasswordCorrect(email, thePasswordFromForm);
		} catch (Exception e) {
			redirectAttributes.addAttribute("incorrectPasswordMessage",
					env.getProperty(locale.getLanguage() + ".controller.UserController.loginVerifying.error.1"));
			return "redirect:/user/login-page";
		}

		// If login is OK then check the password
		if (isLoginOK) {
			User tempUser = userService.getUserByEmail(email);
			LoggedInUser loggedInUser = new LoggedInUser(tempUser.getId(), userService.getUserAccessLevel(tempUser));
			session.setAttribute("userFirstName", tempUser.getFirstName());
			session.setAttribute("userLastName", tempUser.getLastName());
			session.setAttribute("userId", tempUser.getId());
			session.setAttribute("userEmail", tempUser.getEmail());
			session.setAttribute("userAccessLevel", loggedInUser.getUserAccessLevel());
			session.setAttribute("loggedInUser", loggedInUser);
			return "redirect:/user/main";
		} else {
			redirectAttributes.addAttribute("incorrectPasswordMessage",
					env.getProperty(locale.getLanguage() + ".controller.UserController.loginVerifying.error.1"));
			return "redirect:/user/login-page";
		}
	}

	/**
	 * Returns the view of "main-page" with model attributes:<br>
	 * <ul>
	 * <li>userAccessLevel - The access level of the user</li>
	 * <li>user - The User object</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @return The String representing the name of the view The String representing
	 *         the name of the view
	 */
	@RequestMapping("/main")
	public String showMainPage(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request, Model theModel, RedirectAttributes redirectAttributes) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the id of the user from the session
		int userId = (int) session.getAttribute("userId");

		// Get the user with given id
		User theUser = userService.getUserById(userId);

		// Get the user access level from the session
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		// Set model and session attributes
		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "main-page";

	}

	/**
	 * Returns the view of "create-user-form" with model attributes:<br>
	 * <ul>
	 * <li>theUser - The User object</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/add-user")
	public String showUserForm(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		// Create a new blank user
		User theUser = new User();

		// Set model and session attributes
		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "create-user-form";
	}

	/**
	 * Returns the view of "confirm-page" with model attributes:<br>
	 * <ul>
	 * <li>isExist - The isExist determines whether the user account has been
	 * created or not</li>
	 * <li>theUser - The User object</li>
	 * </ul>
	 * 
	 * @param theUser
	 *            The User containing the User to be saved
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/saveUser")
	public String saveUser(@ModelAttribute("user") User theUser, Model theModel, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Check whether the given email address is unique
		boolean isExist = userService.isEmailCorrect(theUser.getEmail());

		// Check whether the given password match with the pattern
		if (!passwordValidator.validate(theUser.getPassword())) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.UserController.saveUser.error.1"));
			return "redirect:/user/create-user-form";
		}
		if (isExist) {
			theModel.addAttribute("isExist", isExist);
			return "confirm-page";
		} else {
			userService.saveUser(theUser);
			theModel.addAttribute("isExist", isExist);
			theModel.addAttribute("theUser", theUser);
			return "confirm-page";
		}
	}

	/**
	 * Logs out the user and invalidates the session and redirects to the view of
	 * "login-page".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {

		// Get the user's session and clean it
		HttpSession session = request.getSession();
		session.invalidate();

		return "redirect:/user/login-page";

	}

	/**
	 * Returns the view of "user-update" with model attributes:<br>
	 * <ul>
	 * <li>user - The User object</li>
	 * <li>systemMessage- The one of the system messages</li>
	 * <li></li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/user-update-form")
	public String showUserUpdateForm(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the id of the user from the session
		int userId = (Integer) session.getAttribute("userId");

		// Get the user with given id
		User theUser = userService.getUserById(userId);

		// Set model and session attributes
		session.setAttribute("oldUserEmail", theUser.getEmail());
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-update";
	}

	/**
	 * Returns the view of "user-update" with model attributes:<br>
	 * <ul>
	 * <li>user - The User object</li>
	 * <li>systemMessage- The one of the system messages</li>
	 * <li></li>
	 * </ul>
	 * 
	 * @param userUpdateUserId
	 *            The Integer containing the id of the user
	 * @param systemMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/user-management-update-form")
	public String showUserManagementUpdateForm(@RequestParam(name = "userUpdateUserId") Integer userUpdateUserId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage, Model theModel,
			HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the user with given id
		User theUser = userService.getUserById(userUpdateUserId);

		// Set model and session attributes
		session.setAttribute("oldUserEmail", theUser.getEmail());
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-update";
	}

	/**
	 * Updates the user data and redirects to the view of "user-update-form" or
	 * "user-details" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param theUser
	 *            The User containing the user to be updated
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/update-user")
	public String updateUser(@ModelAttribute("user") User theUser, HttpServletRequest request, Model theModel,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Check whether the given email address is unique
		boolean isExist = false;
		if (!session.getAttribute("oldUserEmail").equals(theUser.getEmail()))
			isExist = userService.isEmailCorrect(theUser.getEmail());
		if (isExist) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.UserController.updateUser.error.1"));
			return "redirect:/user/user-update-form";
		}

		// Validate the PESEL
		boolean isPeselCorrect;
		if (theUser.getPesel() != null || !theUser.getPesel().equals("")) {
			isPeselCorrect = userService.isPeselCorrect(theUser.getPesel());
			if (!isPeselCorrect) {
				redirectAttributes.addAttribute("systemMessage",
						env.getProperty(locale.getLanguage() + ".controller.UserController.updateUser.error.2"));
				return "redirect:/user/user-update-form";
			}
		}

		// Update the user
		userService.updateUser(theUser);

		// Set redirect and session attributes
		session.setAttribute("oldUserEmail", null);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty(locale.getLanguage() + ".controller.UserController.updateUser.success.1"));
		return "redirect:/user/user-details";

	}

	/**
	 * Returns the view of "change-password-form" with model attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/change-password-form")
	public String showChangePasswordForm(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Set model and session attributes
		theModel.addAttribute("systemMessage", systemMessage);

		return "change-password-form";

	}

	/**
	 * Changes the user password and redirects to the view of "user-details" or
	 * "change-password-form" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param oldPassword
	 *            The String containing the old password
	 * @param newPassword
	 *            The String containing the the new password
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/changePassword")
	public String changePassword(@RequestParam("old-password") String oldPassword,
			@RequestParam("password") String newPassword, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Check whether the old password is different then new one
		if (oldPassword.trim().equals(newPassword.trim())) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.UserController.changePassword.error.1"));
			return "redirect:/user/change-password-form";
		}

		// Get the user's email address and id from the session
		String email = (String) session.getAttribute("userEmail");
		int userId = (Integer) session.getAttribute("userId");

		// Check whether the old password is correct
		boolean isOldPasswordCorrect = userService.isEmailAndPasswordCorrect(email, oldPassword.trim());

		// Change the password if old password is correct
		if (isOldPasswordCorrect) {
			userService.changePassword(userId, newPassword.trim());
			redirectAttributes.addAttribute("systemMessage", "controller.UserController.changePassword.success.1");
			return "redirect:/user/user-details";
		} else {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.UserController.changePassword.error.2"));
			return "redirect:/user/change-password-form";
		}
	}

	/**
	 * Returns the view of "admin-panel".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/admin-panel")
	public String showAdministratorPanel(HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isAdmin((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		return "admin-panel";

	}

	/**
	 * Returns the confirmation as a PDF File
	 * 
	 * @param userId
	 *            The int containing the is of the user
	 * @return The String representing the name of the view
	 * @throws FileNotFoundException
	 *             A FileNotFoundException is thrown when the file can not be found.
	 */
	@RequestMapping("/generateAccountConfirmation")
	public ResponseEntity<InputStreamResource> generateAccountConfirmation(@RequestParam("userId") int userId)
			throws FileNotFoundException {

		User theUser = userService.getUserById(userId);
		File tempFile = pdfService.generateConfirmationForNewAccount(theUser);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);

	}

	/**
	 * Returns the view of "user-management" with model attributes:<br>
	 * <ul>
	 * <li>userManagementStartResult - The value of first index of the results</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>usersList - The list of the User objects</li>
	 * </ul>
	 * 
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param userManagementUserId
	 *            The String containing the id of the user
	 * @param userManagementFirstName
	 *            The String containing the first name of the user
	 * @param userManagementLastName
	 *            The String containing the last name of the user
	 * @param userManagementEmail
	 *            The String containing the email of the user
	 * @param userManagementPesel
	 *            The String containing the PESEL of the user
	 * @param userManagementStartResult
	 *            The Integer containing the first index of the results
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/user-management")
	public String showUserManagementModule(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "userManagementUserId") String userManagementUserId,
			@RequestParam(required = false, name = "userManagementFirstName") String userManagementFirstName,
			@RequestParam(required = false, name = "userManagementLastName") String userManagementLastName,
			@RequestParam(required = false, name = "userManagementEmail") String userManagementEmail,
			@RequestParam(required = false, name = "userManagementPesel") String userManagementPesel,
			@RequestParam(required = false, name = "userManagementStartResult") Integer userManagementStartResult) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// The Arrays containing the values of search parameters

		String[] searchParametersValue = { userManagementUserId, userManagementFirstName, userManagementLastName,
				userManagementEmail, userManagementPesel };

		// The Array containing the search parameters ready to search
		String[] userSearchParameters = searchEngineUtils.prepareTableToSearch(session, NAMES_OF_USER_SEARCH_PARAMETERS,
				searchParametersValue);

		// Get the userManagementStartResult from the session. If session doesn't
		// contain that attribute set default value
		if (userManagementStartResult == null)
			userManagementStartResult = (Integer) session.getAttribute("userManagementStartResult");
		if (userManagementStartResult == null)
			userManagementStartResult = 0;
		session.setAttribute("userManagementStartResult", userManagementStartResult);

		// Check whether the userSearchParameters array contains any parameters and get
		// the results and number of the results.
		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(userSearchParameters);
		List<User> usersList = hasAnyParameters
				? userService.getListOfUserByGivenSearchParams(userSearchParameters, userManagementStartResult)
				: userService.getListOfAllUsers(userManagementStartResult);
		long amountOfResults = hasAnyParameters ? userService.getNumberOfUsersForGivenSearchParams(userSearchParameters)
				: userService.getNumberOfAllUsers();

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(userManagementStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(userManagementStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(userManagementStartResult,
				searchResultLimit);

		// Set model and session attributes
		theModel.addAttribute("userManagementStartResult", userManagementStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("usersList", usersList);

		return "user-management";
	}

	/**
	 * * Returns the view of "user-details" with model attributes:<br>
	 * <ul>
	 * <li>theUser - The User object</li>
	 * <li>userAccessLevel - The access level of the user</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/user-details")
	public String showUserDetails(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the id of the user from the session
		int userId = (Integer) session.getAttribute("userId");

		// Get the user with given id
		User theUser = userService.getUserById(userId);

		// Get the user access level
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		// Set model and session attributes
		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-details";
	}

	/**
	 * Returns the view of "user-details-management" with model attributes:<br>
	 * <ul>
	 * <li>theUser - The User object</li>
	 * <li>userAccessLevel - The access level of the user</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param userDetailsUserId
	 *            The Integer containing the id of the user
	 * @param systemMessage
	 *            The String containing the system message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/user-details-management")
	public String userDetailsManagement(@RequestParam(name = "userDetailsUserId") Integer userDetailsUserId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage, Model theModel,
			HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (!session.getAttribute("userId").equals(userDetailsUserId)) {
			if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
				return "redirect:/user/logout";
		}

		// Get the User with given id
		User theUser = userService.getUserById(userDetailsUserId);

		// Get the user access level
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		// Set model and session attributes
		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-details-management";
	}

	/**
	 * Returns the view of "users-books" with model attributes:<br>
	 * <ul>
	 * <li>reservationList - The list of Reservation objects</li>
	 * <li>borrowedBookList - The list of BorrowedBook objects</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param systemMessage
	 *            The String containing the system message
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/users-books")
	public String showUsersBooks(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request, Model theModel) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the user id from the session
		int theUserId = (Integer) session.getAttribute("userId");

		// Get the list Reservation and BorrowedBook for given user id
		List<Reservation> reservationList = reservationService.getListOfReservationByUserId(theUserId);
		List<BorrowedBook> borrowedBookList = bookService.getListOfBorrowedBooksByUserId(theUserId);

		// Set model and session attributes
		theModel.addAttribute("systemMessage", systemMessage);
		theModel.addAttribute("reservationList", reservationList);
		theModel.addAttribute("borrowedBookList", borrowedBookList);

		return "users-books";
	}

	/**
	 * Cleans the search parameters and redirects to the view of "user-management".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/clearUserSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Clear search parameters
		searchEngineUtils.clearSearchParameters(session, NAMES_OF_USER_MANAGEMENT_SEARCH_PARAMETERS);

		return "redirect:/user/user-management";
	}

	/**
	 * Changes the access level of the user and redirects to the view of
	 * "user-details" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param increaseAccessLevelUserId
	 *            The int containing the id of the user
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/increase-access-level")
	public String increaseAccessLevel(@RequestParam("increaseAccessLevelUserId") int increaseAccessLevelUserId,
			RedirectAttributes redirectAttributes, HttpServletRequest request, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isAdmin((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Create the system message and set it as redirect attribute
		String systemMessage = userService.increaseUserAccessLevel(increaseAccessLevelUserId, locale);
		redirectAttributes.addAttribute("systemMessage", systemMessage);

		return "redirect:/user/user-details";

	}

	/**
	 * Changes the access level of the user and redirects to the view of
	 * "user-details" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param decreaseAccessLevelUserId
	 *            The int containing the id of the user
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/decrease-access-level")
	public String decreaseAccessLevel(@RequestParam("decreaseAccessLevelUserId") int decreaseAccessLevelUserId,
			RedirectAttributes redirectAttributes, HttpServletRequest request, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isAdmin((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Create the system message and set it as redirect attribute
		String systemMessage = userService.decreaseUserAccessLevel(decreaseAccessLevelUserId, locale);
		redirectAttributes.addAttribute("systemMessage", systemMessage);

		return "redirect:/user/user-details";
	}

}
