package pl.mazur.simpleabclibrary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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

@Controller
@Scope("session")
@RequestMapping("/message-module")
public class MessageController {

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	@Autowired
	AccessLevelControl accessLevelControl;

	@Autowired
	SearchEngineUtils searchEngineUtils;

	@RequestMapping("/message-box-inbox")
	public String messageBoxInbox(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "messageInboxStartResult") Integer messageInboxStartResult) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (messageInboxStartResult == null)
			messageInboxStartResult = (Integer) session.getAttribute("messageInboxStartResult");
		if (messageInboxStartResult == null)
			messageInboxStartResult = 0;
		session.setAttribute("messageInboxStartResult", messageInboxStartResult);

		int userId = (int) session.getAttribute("userId");
		List<Message> userMessagesList = messageService.getAllUserMessages(userId, messageInboxStartResult);
		long amountOfResults = messageService.getAmountOfAllInboxMessages(userId);
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(messageInboxStartResult, amountOfResults,
				20);
		String resultRange = searchEngineUtils.generateResultRange(messageInboxStartResult, amountOfResults,
				showMoreLinkValue, 20);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(messageInboxStartResult, 20);

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

	@RequestMapping("/message-box-sent")
	public String messageBoxSent(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "messageStartResult") Integer messageSentStartResult) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (messageSentStartResult == null)
			messageSentStartResult = (Integer) session.getAttribute("messageSentStartResult");
		if (messageSentStartResult == null)
			messageSentStartResult = 0;
		session.setAttribute("messageSentStartResult", messageSentStartResult);

		int userId = (int) session.getAttribute("userId");
		List<Message> userSentMessagesList = messageService.getAllUserSentMessages(userId, messageSentStartResult);
		long amountOfResults = messageService.getAmountOfAllSentMessages(userId);
		long showMoreLinkValue = searchEngineUtils.generateShowMoreLinkValue(messageSentStartResult, amountOfResults,
				20);
		String resultRange = searchEngineUtils.generateResultRange(messageSentStartResult, amountOfResults,
				showMoreLinkValue, 20);
		long showLessLinkValue = searchEngineUtils.generateShowLessLinkValue(messageSentStartResult, 20);

		theModel.addAttribute("messageSentStartResult", messageSentStartResult);
		theModel.addAttribute("amountOfResults", amountOfResults);
		theModel.addAttribute("showMoreLinkValue", showMoreLinkValue);
		theModel.addAttribute("resultRange", resultRange);
		theModel.addAttribute("showLessLinkValue", showLessLinkValue);
		theModel.addAttribute("userSentMessagesList", userSentMessagesList);
		theModel.addAttribute("systemMessage", systemMessage);

		return "message-box-sent";
	}

	@RequestMapping("/deleteMessage")
	public String deleteMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		messageService.deleteMessage(messageId, boxType);
		redirectAttributes.addAttribute("systemMessage", "Wiadomoœæ zosta³a usuniêta.");

		if (boxType.equals("sent"))
			return "redirect:/message-module/message-box-sent";
		else
			return "redirect:/message-module/message-box-inbox";
	}

	@RequestMapping("/readUnreadMessage")
	public String readUnreadMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		messageService.changeReadStatus(messageId, boxType);
		redirectAttributes.addAttribute("systemMessage", "Status wiadomoœci zosta³ zmieniony");

		if (boxType.equals("sent"))
			return "redirect:/message-module/message-box-sent";
		else
			return "redirect:/message-module/message-box-inbox";

	}

	@RequestMapping("/openMessage")
	public String openMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			Model theModel, @RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Message message = messageService.getMessage(messageId);
		messageService.setReadStatusTrue(messageId, boxType);
		theModel.addAttribute("message", message);
		theModel.addAttribute("systemMessage", systemMessage);
		theModel.addAttribute("modelBoxType", boxType);

		return "message";
	}

	@RequestMapping("/close-message-and-set-status")
	public String closeMessageAndSetStatusUnread(@RequestParam("messageId") int messageId,
			@RequestParam("boxType") String boxType, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		messageService.setReadStatusFalse(messageId, boxType);

		return "redirect:/message-module/message-box-inbox";
	}

	@RequestMapping("/create-new-message")
	public String createNewMessage(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		return "create-new-message";

	}

	@RequestMapping("/reply-message")
	public String createReplyMessage(@RequestParam("messageId") int messageId, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		Message message = messageService.getMessage(messageId);
		theModel.addAttribute("message", message);

		return "create-new-message";
	}

	@PostMapping("/send-message")
	public String sendMessage(@RequestParam("recipient") String email, @RequestParam("subject") String subject,
			@RequestParam("textArea") String text, RedirectAttributes redirectAttributesa, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!accessLevelControl.isCustomer((LoggedInUser) session.getAttribute("loggedInUser")))
			return "redirect:/user/logout";

		if (!userService.checkEmailIsExist(email)) {
			redirectAttributesa.addAttribute("systemMessage", "Nieprawid³owy adres email odbiorcy");
			return "redirect:/message-module/message";
		}

		messageService.sendMessage((int) session.getAttribute("userId"), email, subject, text);
		redirectAttributesa.addAttribute("systemMessage", "Wiadomoœæ zosta³a wys³ana.");

		return "redirect:/message-module/message-box-inbox";

	}
}
