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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.mazur.simpleabclibrary.entity.LoggedInUser;
import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.AccessLevelControl;
import pl.mazur.simpleabclibrary.utils.SearchEngineUtils;

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"message-box-inbox"</li>
 * <li>"message-box-sent"</li>
 * <li>"message"</li>
 * <li>"create-new-message"</li>
 * </ul>
 * 
 * <br>
 * <br>
 * 
 * This controller also perform the actions on the messages.
 * 
 * @author Marcin Mazur
 *
 */
@Controller
@Scope("session")
@RequestMapping("/message-module")
@PropertySource("classpath:systemMessages.properties")
@PropertySource("classpath:library-configuration.properties")
public class MessageController {

	/**
	 * The MessageService interface
	 */
	private MessageService messageService;

	/**
	 * The UserService interface
	 */
	private UserService userService;

	/**
	 * The Environment interface
	 */
	private Environment env;

	/**
	 * The AccessLevelControl interface
	 */
	private AccessLevelControl accessLevelControl;

	/**
	 * The SearchEngineUtils interface
	 */
	private SearchEngineUtils searchEngineUtils;

	/**
	 * Constructs a MessageController with the MessageService, UserService,
	 * Environment, AccessLevelControl and SearchEngineUtils.
	 * 
	 * @param messageService
	 *            The MessageService interface
	 * @param userService
	 *            The UserService interface
	 * @param env
	 *            The Environment interface
	 * @param accessLevelControl
	 *            The AccessLevelControl interface
	 * @param searchEngineUtils
	 *            The SearchEngineUtils interface
	 */
	@Autowired
	public MessageController(MessageService messageService, UserService userService, Environment env,
			AccessLevelControl accessLevelControl, SearchEngineUtils searchEngineUtils) {

		this.messageService = messageService;
		this.userService = userService;
		this.env = env;
		this.accessLevelControl = accessLevelControl;
		this.searchEngineUtils = searchEngineUtils;

	}

	/**
	 * Returns the view of "message-box-inbox" with model attributes:<br>
	 * <ul>
	 * <li>messageInboxStartResult - The value of first index of the results</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * <li>userMessagesList - The list of Message objects</li>
	 * </ul>
	 * 
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param systemMessage
	 *            The String containing the system message
	 * @param messageInboxStartResult
	 *            The Integer containing the first index of the results
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/message-box-inbox")
	public String showMessageBoxInbox(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "messageInboxStartResult") Integer messageInboxStartResult) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the messageSentStartResult from the session. If session doesn't contain
		// that attribute set default value
		if (messageInboxStartResult == null)
			messageInboxStartResult = (Integer) session.getAttribute("messageInboxStartResult");
		if (messageInboxStartResult == null)
			messageInboxStartResult = 0;
		session.setAttribute("messageInboxStartResult", messageInboxStartResult);

		// Get the user id from the session
		int userId = (int) session.getAttribute("userId");

		// Get the list of all messages with the given user id
		List<Message> userMessagesList = messageService.getListOfInboxMessagesByUserId(userId, messageInboxStartResult);
		long amountOfResults = messageService.getNumberOfAllMessages(userId, "inbox");

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit.messages"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(messageInboxStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(messageInboxStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(messageInboxStartResult,
				searchResultLimit);

		// Set model and session attributes
		session.setAttribute("messageInboxStartResult", messageInboxStartResult);
		theModel.addAttribute("messageInboxStartResult", messageInboxStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("userMessagesList", userMessagesList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "message-box-inbox";
	}

	/**
	 * Returns the view of "message-box-sent" with model attributes:<br>
	 * <ul>
	 * <li>messageSentStartResult - The value of first index of the results</li>
	 * <li>amountOfResults - The number of total results</li>
	 * <li>showMoreLinkValue - The value of "showMoreLink"</li>
	 * <li>resultRange - The description of pagination</li>
	 * <li>showLessLinkValue - The value of "showLessLink"</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * <li>userSentMessagesList - The list of Message objects</li>
	 * </ul>
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param systemMessage
	 *            The String containing the system message
	 * @param messageSentStartResult
	 *            The Integer containing the first index of the results
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/message-box-sent")
	public String showMessageBoxSent(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "messageStartResult") Integer messageSentStartResult) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get the messageSentStartResult from the session. If session doesn't contain
		// that attribute set default value
		if (messageSentStartResult == null)
			messageSentStartResult = (Integer) session.getAttribute("messageSentStartResult");
		if (messageSentStartResult == null)
			messageSentStartResult = 0;
		session.setAttribute("messageSentStartResult", messageSentStartResult);

		// Get the user id from the session
		int userId = (int) session.getAttribute("userId");

		// Get the list of all messages with the given user id
		List<Message> userSentMessagesList = messageService.getListOfSentMessagesByUserId(userId,
				messageSentStartResult);
		long amountOfResults = messageService.getNumberOfAllMessages(userId, "sent");

		// Get showMoreLinkValue, resultRange and showLessLinkValue
		int searchResultLimit = Integer.valueOf(env.getProperty("search.result.limit.messages"));
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(messageSentStartResult, amountOfResults,
				searchResultLimit);
		String resultRange = searchEngineUtils.generateResultRange(messageSentStartResult, amountOfResults,
				showMoreLinkValue, searchResultLimit);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(messageSentStartResult, searchResultLimit);

		// Set model and session attributes
		theModel.addAttribute("messageSentStartResult", messageSentStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("userSentMessagesList", userSentMessagesList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "message-box-sent";
	}

	/**
	 * Deletes the message and redirects to the view of "message-box-sent" or
	 * "message-box-inbox" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            The String containing the name of the box (inbox - sent)
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/deleteMessage")
	public String deleteMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Delete the message with given id
		messageService.deleteMessage(messageId, boxType);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty(locale.getLanguage() + ".controller.MessageController.deleteMessage.success.1"));

		// Returned view depends on boxType
		if (boxType.equals("sent"))
			return "redirect:/message-module/message-box-sent";
		else
			return "redirect:/message-module/message-box-inbox";
	}

	/**
	 * Changes the status of the message and redirects to the view of
	 * "message-box-sent" or "message-box-inbox" with redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage - The one of the system messages</li>
	 * </ul>
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            The String containing the name of the box (inbox - sent)
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param locale
	 *            The Locale containing the user's locale
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/readUnreadMessage")
	public String readUnreadMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Locale locale) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Change IsRead status of the message
		messageService.changeReadStatus(messageId, boxType);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty(locale.getLanguage() + ".controller.MessageController.readUnreadMessage.success.1"));

		// Returned view depends on boxType
		if (boxType.equals("sent"))
			return "redirect:/message-module/message-box-sent";
		else
			return "redirect:/message-module/message-box-inbox";

	}

	/**
	 * Returns the view of "message" with model attributes:<br>
	 * <ul>
	 * <li>message - The Message object</li>
	 * <li>systemMessage - The one of the system messages</li>
	 * <li>modelBoxType - The name of the box(inbox - sent)</li>
	 * </ul>
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            The String containing the name of the box (inbox - sent)
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param systemMessage
	 *            The String containing the system message
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/openMessage")
	public String showMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			Model theModel, @RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get message by the given id and change IsRead to TRUE
		Message message = messageService.getMessageById(messageId);
		messageService.setIsReadStatusToTrue(messageId, boxType);

		// Set model and session attributes
		theModel.addAttribute("message", message);
		theModel.addAttribute("systemMessage", systemMessage);
		theModel.addAttribute("modelBoxType", boxType);

		return "message";
	}

	/**
	 * Changes the status of the message and redirects to the view of
	 * "message-box-inbox".
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param boxType
	 *            The String containing the name of the box (inbox - sent)
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/close-message-and-set-status")
	public String closeMessageAndSetStatusUnread(@RequestParam("messageId") int messageId,
			@RequestParam("boxType") String boxType, HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Change isRead status of the message
		messageService.setIsReadStatusToFalse(messageId, boxType);

		return "redirect:/message-module/message-box-inbox";
	}

	/**
	 * Returns the view of "create-new-message".
	 * 
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/create-new-message")
	public String createNewMessage(HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		return "create-new-message";

	}

	/**
	 * Returns the view of "create-new-message" with model attributes:<br>
	 * <ul>
	 * <li>message - The Message object</li>
	 * </ul>
	 * 
	 * @param messageId
	 *            The int containing the id of the message
	 * @param theModel
	 *            The Model containing the data passed to the view
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/reply-message")
	public String showReplyMessage(@RequestParam("messageId") int messageId, Model theModel,
			HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Get message by given id
		Message message = messageService.getMessageById(messageId);

		// Set model and session attributes
		theModel.addAttribute("message", message);

		return "create-new-message";
	}

	/**
	 * Send the message and redirects to the view of "message-box-inbox" with
	 * redirect attributes:<br>
	 * <ul>
	 * <li>systemMessage- The one of the system messages</li>
	 * </ul>
	 * 
	 * @param email
	 *            The String containing the email of the recipient
	 * @param subject
	 *            The String containing the subject of the message
	 * @param locale
	 *            The Locale containing the user's locale
	 * @param text
	 *            The String containing the text of the message
	 * @param redirectAttributes
	 *            The RedirectAttributes containing the system messages
	 * @param request
	 *            The HttpServletRequest containing the HttpSession
	 * @return The String representing the name of the view
	 */
	@PostMapping("/send-message")
	public String sendMessage(@RequestParam("recipient") String email, @RequestParam("subject") String subject,
			Locale locale, @RequestParam("textArea") String text, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		// Get the user's session and check whether the user is permitted to see this
		// view
		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		// Check whether the email address exists in the database
		if (!userService.isEmailCorrect(email)) {
			redirectAttributes.addAttribute("systemMessage",
					env.getProperty(locale.getLanguage() + ".controller.MessageController.sendMessage.error.1"));
			return "redirect:/message-module/message";
		}

		// Send message
		messageService.sendMessage((int) session.getAttribute("userId"), email, subject, text);
		redirectAttributes.addAttribute("systemMessage",
				env.getProperty(locale.getLanguage() + ".controller.MessageController.sendMessage.success.1"));

		return "redirect:/message-module/message-box-inbox";

	}
}
