package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
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

@Controller
@Scope("session")
@RequestMapping("/user")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:library-configuration.properties")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private Environment env;

	@Autowired
	private AccessLevelControl accessLevelControl;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private BookService bookService;

	@Autowired
	private PasswordValidator passwordValidator;

	@Autowired
	private SearchEngineUtils searchEngineUtils;

	@RequestMapping("/login-page")
	public String loginPage(
			@RequestParam(required = false, name = "incorrectPasswordMessage") String incorrectPasswordRequestMessage,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		theModel.addAttribute("incorrectPasswordMessage", incorrectPasswordRequestMessage);

		if (session.getAttribute("userId") != null) {
			return "redirect:/user/main";
		} else {
			return "login-page";
		}
	}

	@RequestMapping("/login")
	public String loginVerifying(@RequestParam("email") String email,
			@RequestParam("password") String thePasswordFromForm, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		boolean isLoginOK = false;
		try {
			isLoginOK = userService.verificationAndAuthentication(email, thePasswordFromForm);
		} catch (Exception e) {
			redirectAttributes.addAttribute("incorrectPasswordMessage",
					env.getProperty("controller.UserController.loginVerifying.error.1"));
			return "redirect:/user/login-page";
		}

		if (isLoginOK) {
			User tempUser = userService.getUser(email);
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
					env.getProperty("controller.UserController.loginVerifying.error.1"));
			return "redirect:/user/login-page";
		}
	}

	@RequestMapping("/main")
	public String showMainPage(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request, Model theModel, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		int userId = (int) session.getAttribute("userId");
		User theUser = userService.getUser(userId);
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "main-page";

	}

	@RequestMapping("/add-user")
	public String addUserForm(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		User theUser = new User();
		theModel.addAttribute(theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "create-user-form";
	}

	@RequestMapping("/saveUser")
	public String saveUser(@ModelAttribute("user") User theUser, Model theModel, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		boolean isExist = userService.checkEmailIsExist(theUser.getEmail());

		if (!passwordValidator.validate(theUser.getPassword())) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty("controller.UserController.saveUser.error.1"));
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

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.invalidate();

		return "redirect:/user/login-page";

	}

	@RequestMapping("/user-update-form")
	public String userUpdateForm(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		int userId = (Integer) session.getAttribute("userId");
		User theUser = userService.getUser(userId);

		session.setAttribute("oldUserEmail", theUser.getEmail());
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-update";
	}

	@RequestMapping("/user-management-update-form")
	public String userManagementUpdateForm(@RequestParam(name = "userUpdateUserId") Integer userUpdateUserId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		User theUser = userService.getUser(userUpdateUserId);

		session.setAttribute("oldUserEmail", theUser.getEmail());
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-update";
	}

	@RequestMapping("/update-user")
	public String updateUser(@ModelAttribute("user") User theUser, HttpServletRequest request, Model theModel,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		boolean isExist = false;
		if (!session.getAttribute("oldUserEmail").equals(theUser.getEmail()))
			isExist = userService.checkEmailIsExist(theUser.getEmail());
		if (isExist) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty("controller.UserController.updateUser.error.1"));
			return "redirect:/user/user-update-form";
		}

		boolean isPeselCorrect;
		if (theUser.getPesel() != null || !theUser.getPesel().equals("")) {
			isPeselCorrect = userService.validatePesel(theUser.getPesel());
			if (!isPeselCorrect) {
				redirectAttributes.addAttribute("systemMessage",
						env.getProperty("controller.UserController.updateUser.error.2"));
				return "redirect:/user/user-update-form";
			}
		}

		userService.updateUser(theUser);

		session.setAttribute("oldUserEmail", null);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty("controller.UserController.updateUser.success.1"));
		return "redirect:/user/user-details";

	}

	@RequestMapping("/change-password-form")
	public String changePasswordForm(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		theModel.addAttribute("systemMessage", systemMessage);

		return "change-password-form";

	}

	@RequestMapping("/changePassword")
	public String changePassword(@RequestParam("old-password") String oldPassword,
			@RequestParam("password") String newPassword, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (oldPassword.trim().equals(newPassword.trim())) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty("controller.UserController.changePassword.error.1"));
			return "redirect:/user/change-password-form";
		}

		String email = (String) session.getAttribute("userEmail");
		int userId = (Integer) session.getAttribute("userId");
		boolean isOldPasswordCorrect = userService.verificationAndAuthentication(email, oldPassword.trim());

		if (isOldPasswordCorrect) {
			userService.changePassword(userId, newPassword.trim());
			redirectAttributes.addAttribute("systemMessage", "controller.UserController.changePassword.success.1");
			return "redirect:/user/user-details";
		} else {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty("controller.UserController.changePassword.error.2"));
			return "redirect:/user/change-password-form";
		}
	}

	@RequestMapping("/admin-panel")
	public String administratorPanel(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isAdmin((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		return "admin-panel";

	}

	@RequestMapping("/generateAccountConfirmation")
	public ResponseEntity<InputStreamResource> generateAccountConfirmation(@RequestParam("userId") int userId)
			throws FileNotFoundException {

		User theUser = userService.getUser(userId);
		File tempFile = pdfService.generateCreatedAccountConfirmation(theUser);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_PDF);
		responseHeaders.setContentLength(tempFile.length());
		responseHeaders.setContentDispositionFormData("attachment", tempFile.getName());

		InputStreamResource isr = new InputStreamResource(new FileInputStream(tempFile));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);

	}

	@RequestMapping("/user-management")
	public String userManagement(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "userManagementUserId") String userManagementUserId,
			@RequestParam(required = false, name = "userManagementFirstName") String userManagementFirstName,
			@RequestParam(required = false, name = "userManagementLastName") String userManagementLastName,
			@RequestParam(required = false, name = "userManagementEmail") String userManagementEmail,
			@RequestParam(required = false, name = "userManagementPesel") String userManagementPesel,
			@RequestParam(required = false, name = "userManagementStartResult") Integer userManagementStartResult) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "userManagementSelectedUserId", "userManagementFirstName",
				"userManagementLastName", "userManagementEmail", "userManagementPesel" };
		String[] searchParametersValue = { userManagementUserId, userManagementFirstName, userManagementLastName,
				userManagementEmail, userManagementPesel };

		String[] userSearchParameters = searchEngineUtils.prepareTableToSearch(session, searchParametersName,
				searchParametersValue);

		if (userManagementStartResult == null)
			userManagementStartResult = (Integer) session.getAttribute("userManagementStartResult");
		if (userManagementStartResult == null)
			userManagementStartResult = 0;
		session.setAttribute("userManagementStartResult", userManagementStartResult);

		boolean hasAnyParameters = searchEngineUtils.hasTableAnyParameters(userSearchParameters);
		List<User> usersList = hasAnyParameters
				? userService.getUserSearchResult(userSearchParameters, userManagementStartResult)
				: userService.getAllUsers(userManagementStartResult);
		long amountOfResults = hasAnyParameters ? userService.getAmountOfSearchResult(userSearchParameters)
				: userService.getAmountOfAllUsers();

		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit"));

		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(userManagementStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(userManagementStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(userManagementStartResult,
				searchResultLimit);

		theModel.addAttribute("userManagementStartResult", userManagementStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("usersList", usersList);

		return "user-management";
	}

	@RequestMapping("/user-details")
	public String userDetails(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		int userId = (Integer) session.getAttribute("userId");
		User theUser = userService.getUser(userId);
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-details";
	}

	@RequestMapping("/user-details-management")
	public String userDetailsManagement(@RequestParam(name = "userDetailsUserId") Integer userDetailsUserId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (!session.getAttribute("userId").equals(userDetailsUserId)) {
			if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
				return "redirect:/user/logout";
		}

		User theUser = userService.getUser(userDetailsUserId);
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-details-management";
	}

	@RequestMapping("/users-books")
	public String usersBooks(@RequestParam(required = false, name = "systemSuccessMessage") String systemSuccessMessage,
			HttpServletRequest request, Model theModel) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		int theUserId = (Integer) session.getAttribute("userId");
		List<Reservation> reservationList = reservationService.getUserReservations(theUserId);
		List<BorrowedBook> borrowedBookList = bookService.getUserBorrowedBookList(theUserId);

		theModel.addAttribute("systemSuccessMessage", systemSuccessMessage);
		theModel.addAttribute("reservationList", reservationList);
		theModel.addAttribute("borrowedBookList", borrowedBookList);

		return "users-books";
	}

	@RequestMapping("/clearUserSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isEmployee((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String[] searchParametersName = { "userManagementStartResult", "userManagementSelectedUserId",
				"userManagementFirstName", "userManagementLastName", "userManagementEmail", "userManagementPesel" };
		searchEngineUtils.clearSearchParameters(session, searchParametersName);

		return "redirect:/user/user-management";
	}

	@RequestMapping("/increase-access-level")
	public String increaseAccessLevel(@RequestParam("increaseAccessLevelUserId") int increaseAccessLevelUserId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isAdmin((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String systemMessage = userService.increaseUserAccessLevel(increaseAccessLevelUserId);
		redirectAttributes.addAttribute("systemMessage", systemMessage);

		return "redirect:/user/user-details";

	}

	@RequestMapping("/decrease-access-level")
	public String decreaseAccessLevel(@RequestParam("decreaseAccessLevelUserId") int decreaseAccessLevelUserId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isAdmin((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		String systemMessage = userService.decreaseUserAccessLevel(decreaseAccessLevelUserId);
		redirectAttributes.addAttribute("systemMessage", systemMessage);

		return "redirect:/user/user-details";
	}
	
	
}
