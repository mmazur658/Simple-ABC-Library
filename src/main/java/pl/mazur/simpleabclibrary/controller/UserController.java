package pl.mazur.simpleabclibrary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.BookBorrowing;
import pl.mazur.simpleabclibrary.entity.Reservation;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.BookService;
import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.PdfService;
import pl.mazur.simpleabclibrary.service.ReservationService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.LoginAndAccessLevelCheck;
import pl.mazur.simpleabclibrary.utils.PasswordValidator;

@Controller
@Scope("session")
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	PdfService pdfService;

	@Autowired
	LoginAndAccessLevelCheck loginAndAccessLevelCheck;

	@Autowired
	ReservationService reservationService;

	@Autowired
	BookService bookService;

	@Autowired
	MessageService messageService;
	
	@Autowired
	PasswordValidator passwordValidator;

	@RequestMapping("/login-page")
	public String loginPage(
			@RequestParam(required = false, name = "incorrectPasswordMessage") String incorrectPasswordRequestMessage,
			Model theModel, HttpServletRequest request) {

		HttpSession session = request.getSession();
		theModel.addAttribute("incorrectPasswordMessage", incorrectPasswordRequestMessage);

		if (session.getAttribute("userFirstName") != null && session.getAttribute("userLastName") != null) {
			return "redirect:/user/main";
		} else {
			return "login-page";
		}
	}

	@PostMapping("/login")
	public String loginVerifying(@RequestParam("email") String email,
			@RequestParam("password") String thePasswordFromForm, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		boolean isLoginOK = false;

		try {
			isLoginOK = userService.verificationAndAuthentication(email, thePasswordFromForm);
		} catch (Exception e) {
			redirectAttributes.addAttribute("incorrectPasswordMessage", "Nieprawid³owe dane logowania!!");
			return "redirect:/user/login-page";
		}

		if (isLoginOK) {
			User tempUser = userService.getUser(email);
			session.setAttribute("userFirstName", tempUser.getFirstName());
			session.setAttribute("userLastName", tempUser.getLastName());
			session.setAttribute("userId", tempUser.getId());
			session.setAttribute("userEmail", tempUser.getEmail());

			if (tempUser.isAdmin())
				session.setAttribute("userAccessLevel", "Administrator");
			else if (tempUser.isAdmin() == false && tempUser.isEmployee() == true)
				session.setAttribute("userAccessLevel", "Employee");
			else
				session.setAttribute("userAccessLevel", "Customer");

			return "redirect:/user/main";

		} else {
			redirectAttributes.addAttribute("incorrectPasswordMessage", "Nieprawid³owe dane logowania");
			
			return "redirect:/user/login-page";
		}
	}

	@RequestMapping("/main")
	public String showMainPage(@RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request, Model theModel, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		int userId = (int) session.getAttribute("userId");
		User theUser = userService.getUser(userId);
		String userAccessLevel = (String) session.getAttribute("userAccessLevel");

		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "main-page";

	}

	@RequestMapping("/add-user")
	public String addUserForm(@RequestParam(required=false, name="systemMessage")  String systemMessage,
			Model theModel, HttpServletRequest request) {

		User theUser = new User();
		theModel.addAttribute(theUser);
		theModel.addAttribute("systemMessage", systemMessage);

		return "create-user-form";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute("user") User theUser, Model theModel, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		boolean isExist = userService.checkEmailIsExist(theUser.getEmail());
		
		if(!passwordValidator.validate(theUser.getPassword())) {
			
			redirectAttributes.addAttribute("systemMessage", "Has³o nie spe³nia wymagañ");
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
			@RequestParam(required = false, name = "userUpdateUserId") Integer userUpdateUserId, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		if (userUpdateUserId != null) {
			session.setAttribute("userUpdateUserId", userUpdateUserId);
		} else if (userUpdateUserId == null && session.getAttribute("userUpdateUserId") != null) {
			userUpdateUserId = (Integer) session.getAttribute("userUpdateUserId");
		}
		
		User theUser = userService.getUser(userUpdateUserId);
		
		session.setAttribute("userUpdateUserId", userUpdateUserId);
		session.setAttribute("oldUserEmail", theUser.getEmail());
		theModel.addAttribute("user", theUser);		
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-update";
	}

	@PostMapping("/update-user")
	public String updateUser(@ModelAttribute("user") User theUser, HttpServletRequest request, Model theModel,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		boolean isExist = false;
		if (!session.getAttribute("oldUserEmail").equals(theUser.getEmail()))
			isExist = userService.checkEmailIsExist(theUser.getEmail());
		if (isExist) {
			redirectAttributes.addAttribute("systemMessage", "Istnieje ju¿ konto dla tego adresu Email");
			return "redirect:/user/user-update-form";
		}

		boolean isPeselCorrect;
		if (theUser.getPesel() == null || theUser.getPesel().equals("")) {
		} else {
			isPeselCorrect = userService.validatePesel(theUser.getPesel());
			if (!isPeselCorrect) {
				redirectAttributes.addAttribute("systemMessage", "Nieprawid³owy PESEL!!");
				return "redirect:/user/user-update-form";
			}
		}
		userService.updateUser(theUser);
		
		session.setAttribute("oldUserEmail", null);		
		redirectAttributes.addAttribute("systemMessage", "Konto zosta³o zaktualizowane");
		
		return "redirect:/user/user-details";

	}

	@RequestMapping("/change-password-form")
	public String changePasswordForm(
			@RequestParam(required = false, name = "changePasswordUserId") Integer changePasswordUserId,
			@RequestParam(required = false, name = "systemMessage") String systemMessage, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		if (changePasswordUserId != null)
			session.setAttribute("changePasswordUserId", changePasswordUserId);
		else if (changePasswordUserId == null)
			session.getAttribute("changePasswordUserId");

		theModel.addAttribute("changePasswordUserId", changePasswordUserId);
		theModel.addAttribute("systemMessage", systemMessage);
		
		return "change-password-form";

	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("old-password") String oldPassword,
			@RequestParam("password") String newPassword,
			@RequestParam("changePasswordFormUserId") Integer changePasswordUserId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		if (oldPassword.trim().equals(newPassword.trim())) {
			redirectAttributes.addAttribute("systemMessage", "Nowe has³o nie jest nowe.");
			return "redirect:/user/change-password-form";
		}

		String email = (String) session.getAttribute("userEmail");
		int userId = changePasswordUserId;
		boolean isOldPasswordCorrect = userService.verificationAndAuthentication(email, oldPassword.trim());

		if (isOldPasswordCorrect) {
			userService.changePassword(userId, newPassword.trim());
			redirectAttributes.addAttribute("systemMessage", "Has³o zosta³o zmienione!");
			return "redirect:/user/user-details";
		} else {
			redirectAttributes.addAttribute("systemMessage", "Nieprawid³owe stare has³o");
			return "redirect:/user/change-password-form";
		}
	}

	@RequestMapping("/employee-panel")
	public String employeePanel(HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		return "emp-panel";

	}

	@RequestMapping("/admin-panel")
	public String administratorPanel(HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isAdmin((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

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

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		if (!(userManagementUserId == null))
			session.setAttribute("userManagementUserId", userManagementUserId);
		if (!(userManagementFirstName == null))
			session.setAttribute("userManagementFirstName", userManagementFirstName);
		if (!(userManagementLastName == null))
			session.setAttribute("userManagementLastName", userManagementLastName);
		if (!(userManagementEmail == null))
			session.setAttribute("userManagementEmail", userManagementEmail);
		if (!(userManagementPesel == null))
			session.setAttribute("userManagementPesel", userManagementPesel);

		if ((userManagementUserId == null) && !(session.getAttribute("userManagementUserId") == null))
			userManagementUserId = String.valueOf(session.getAttribute("userManagementUserId"));
		if ((userManagementFirstName == null) && !(session.getAttribute("userManagementFirstName") == null))
			userManagementFirstName = String.valueOf(session.getAttribute("userManagementFirstName"));
		if ((userManagementLastName == null) && !(session.getAttribute("userManagementLastName") == null))
			userManagementLastName = String.valueOf(session.getAttribute("userManagementLastName"));
		if ((userManagementEmail == null) && !(session.getAttribute("userManagementEmail") == null))
			userManagementEmail = String.valueOf(session.getAttribute("userManagementEmail"));
		if ((userManagementPesel == null) && !(session.getAttribute("userManagementPesel") == null))
			userManagementPesel = String.valueOf(session.getAttribute("userManagementPesel"));

		String[] userSearchParameters = { "", "", "", "", "", "", "", "" };
		userSearchParameters[0] = (userManagementUserId == null) ? "" : userManagementUserId.trim();
		userSearchParameters[1] = (userManagementFirstName == null) ? "" : userManagementFirstName.trim();
		userSearchParameters[2] = (userManagementLastName == null) ? "" : userManagementLastName.trim();
		userSearchParameters[3] = (userManagementEmail == null) ? "" : userManagementEmail.trim();
		userSearchParameters[4] = (userManagementPesel == null) ? "" : userManagementPesel.trim();

		if (userManagementStartResult == null)
			userManagementStartResult = (Integer) session.getAttribute("userManagementStartResult");
		if (userManagementStartResult == null)
			userManagementStartResult = 0;

		List<User> usersList;
		long amountOfResults;
		
		if (!userSearchParameters[0].equals("") || !userSearchParameters[1].equals("")
				|| !userSearchParameters[2].equals("") || !userSearchParameters[3].equals("")
				|| !userSearchParameters[4].equals("")) {
			usersList = userService.getUserSearchResult(userSearchParameters, userManagementStartResult);
			amountOfResults = userService.getAmountOfSearchResult(userSearchParameters);
		} else {
			usersList = userService.getAllUsers(userManagementStartResult);
			amountOfResults = userService.getAmountOfAllBooks();
		}

		String resultRange;
		long showMoreLinkValue = 0;
		if ((userManagementStartResult + 10) > amountOfResults) {
			showMoreLinkValue = userManagementStartResult;
			resultRange = "Wyniki od " + (userManagementStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = userManagementStartResult + 10;
			resultRange = "Wyniki od " + (userManagementStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((userManagementStartResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = userManagementStartResult - 10;
		}

		session.setAttribute("returnBookStartResult", userManagementStartResult);
		theModel.addAttribute("userManagementStartResult", userManagementStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("usersList", usersList);

		return "user-management";
	}

	@RequestMapping("/user-details")
	public String userDetails(@RequestParam(required = false, name = "userDetailsUserId") Integer userDetailsUserId,
			@RequestParam(required = false, name = "userDetailsWayBack") String userDetailsWayBack,
			@RequestParam(required = false, name = "systemMessage") String systemMessage, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (userDetailsUserId != null)
			session.setAttribute("userDetailsUserId", userDetailsUserId);
		else if (userDetailsUserId == null && session.getAttribute("userDetailsUserId") != null)
			userDetailsUserId = (Integer) session.getAttribute("userDetailsUserId");
		else
			userDetailsUserId = (Integer) session.getAttribute("userId");

		User theUser = userService.getUser(userDetailsUserId);
		List<Reservation> reservationList = reservationService.getUserReservations(userDetailsUserId);
		List<BookBorrowing> bookBorrowingList = bookService.getUserBookBorrowing(userDetailsUserId);

		String userAccessLevel = null;
		;
		if (!theUser.isAdmin() && !theUser.isEmployee())
			userAccessLevel = "Klient";
		else if (!theUser.isAdmin() && theUser.isEmployee())
			userAccessLevel = "Pracownik";
		else if (theUser.isAdmin() && !theUser.isEmployee())
			userAccessLevel = "Administrator";

		String loginUserAccessLevel = (String) session.getAttribute("userAccessLevel");

		theModel.addAttribute("loginUserAccessLevel", loginUserAccessLevel);
		theModel.addAttribute("userDetailsWayBack", userDetailsWayBack);
		theModel.addAttribute("theUser", theUser);
		theModel.addAttribute("userAccessLevel", userAccessLevel);
		theModel.addAttribute("reservationList", reservationList);
		theModel.addAttribute("bookBorrowingList", bookBorrowingList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "user-details";
	}

	@RequestMapping("/clearUserSearchParameters")
	public String clearReservationSearchParameters(HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isEmployee((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		session.setAttribute("returnBookStartResult", null);
		session.setAttribute("userManagementUserId", null);
		session.setAttribute("userManagementFirstName", null);
		session.setAttribute("userManagementLastName", null);
		session.setAttribute("userManagementEmail", null);
		session.setAttribute("userManagementPesel", null);

		return "redirect:/user/user-management";
	}

	@RequestMapping("/increase-access-level")
	public String increaseAccessLevel(@RequestParam("increaseAccessLevelUserId") int increaseAccessLevelUserId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isAdmin((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		User theUser = userService.getUser(increaseAccessLevelUserId);

		if (!theUser.isAdmin() && !theUser.isEmployee()) {
			theUser.setEmployee(true);
			redirectAttributes.addAttribute("systemMessage", "Zwiêkszono uprawnienia do poziomu: Pracownik");
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(true);
			redirectAttributes.addAttribute("systemMessage", "Zwiêkszono uprawnienia do poziomu: Administrator");
		} else {
			redirectAttributes.addAttribute("systemMessage",
					"Nie mo¿na zwiêkszyæ uprawnieñ, osi¹gniêto maksymalny poziom");
		}

		userService.increaseUserAccessLevel(theUser);

		return "redirect:/user/user-details";

	}

	@RequestMapping("/decrease-access-level")
	public String decreaseAccessLevel(@RequestParam("decreaseAccessLevelUserId") int decreaseAccessLevelUserId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (!loginAndAccessLevelCheck.loginCheck((String) session.getAttribute("userFirstName"),
				(String) session.getAttribute("userLastName"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}
		if (!loginAndAccessLevelCheck.isAdmin((String) session.getAttribute("userAccessLevel"))) {
			session.invalidate();
			return "redirect:/user/login-page";
		}

		User theUser = userService.getUser(decreaseAccessLevelUserId);

		if (theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setAdmin(false);
			redirectAttributes.addAttribute("systemMessage", "Zmniejszono uprawnienia do poziomu: Pracownik");
		} else if (!theUser.isAdmin() && theUser.isEmployee()) {
			theUser.setEmployee(false);
			redirectAttributes.addAttribute("systemMessage", "Zmniejszono uprawnienia do poziomu: Klient");
		} else {
			redirectAttributes.addAttribute("systemMessage",
					"Nie mo¿na zmniejszyæ uprawnieñ, osi¹gniêto minimalny poziom");
		}

		userService.decreaseUserAccessLevel(theUser);

		return "redirect:/user/user-details";

	}

}
