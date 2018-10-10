package pl.mazur.simpleabclibrary.controller;

import java.util.Date;
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

import pl.mazur.simpleabclibrary.entity.Message;
import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.MessageService;
import pl.mazur.simpleabclibrary.service.UserService;
import pl.mazur.simpleabclibrary.utils.LoginAndAccessLevelCheck;

@Controller
@Scope("session")
@RequestMapping("/message-module")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	LoginAndAccessLevelCheck loginAndAccessLevelCheck;

	@RequestMapping("/message-box-inbox")
	public String messageBoxInbox(HttpServletRequest request, Model theModel,
			@RequestParam(required = false, name = "systemMessage") String systemMessage,
			@RequestParam(required = false, name = "messageInboxStartResult") Integer messageInboxStartResult) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		int userId = (int) session.getAttribute("userId");

		if (messageInboxStartResult == null)
			messageInboxStartResult = (Integer) session.getAttribute("messageInboxStartResult");
		if (messageInboxStartResult == null)
			messageInboxStartResult = 0;

		long amountOfResults = messageService.getAmountOfAllInboxMessages(userId);

		String resultRange;
		long showMoreLinkValue = 0;
		if ((messageInboxStartResult + 20) > amountOfResults) {
			showMoreLinkValue = messageInboxStartResult;
			resultRange = "Wyniki od " + (messageInboxStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = messageInboxStartResult + 20;
			resultRange = "Wyniki od " + (messageInboxStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((messageInboxStartResult - 20) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = messageInboxStartResult - 20;
		}

		List<Message> userMessagesList = messageService.getAllUserMessages(userId, messageInboxStartResult);
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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		int userId = (int) session.getAttribute("userId");

		if (messageSentStartResult == null)
			messageSentStartResult = (Integer) session.getAttribute("messageSentStartResult");
		if (messageSentStartResult == null)
			messageSentStartResult = 0;

		session.setAttribute("messageSentStartResult", messageSentStartResult);

		long amountOfResults = messageService.getAmountOfAllSentMessages(userId);

		String resultRange;
		long showMoreLinkValue = 0;
		if ((messageSentStartResult + 10) > amountOfResults) {
			showMoreLinkValue = messageSentStartResult;
			resultRange = "Wyniki od " + (messageSentStartResult + 1) + " do " + amountOfResults;
		} else {
			showMoreLinkValue = messageSentStartResult + 10;
			resultRange = "Wyniki od " + (messageSentStartResult + 1) + " do " + showMoreLinkValue;
		}

		long showLessLinkValue = 0;
		if ((messageSentStartResult - 10) < 0) {
			showLessLinkValue = 0;
		} else {
			showLessLinkValue = messageSentStartResult - 10;
		}

		List<Message> userSentMessagesList = messageService.getAllUserSentMessages(userId, messageSentStartResult);

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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		messageService.deleteMessage(messageId, boxType);
		if (boxType.equals("sent")) {
			redirectAttributes.addAttribute("systemMessage", "Wiadomoœæ zosta³a usuniêta.");
			return "redirect:/message-module/message-box-sent";
		} else {
			redirectAttributes.addAttribute("systemMessage", "Wiadomoœæ zosta³a usuniêta.");
			return "redirect:/message-module/message-box-inbox";
		}

	}

	@RequestMapping("/readUnreadMessage")
	public String readUnreadMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		messageService.changeReadStatus(messageId, boxType);

		if (boxType.equals("sent")) {
			redirectAttributes.addAttribute("systemMessage", "Status wiadomoœci zosta³ zmieniony");
			return "redirect:/message-module/message-box-sent";
		} else {
			redirectAttributes.addAttribute("systemMessage", "Status wiadomoœci zosta³ zmieniony");
			return "redirect:/message-module/message-box-inbox";
		}

	}

	@RequestMapping("/openMessage")
	public String openMessage(@RequestParam("messageId") int messageId, @RequestParam("boxType") String boxType,
			Model theModel, @RequestParam(required = false, name = "systemMessage") String systemMessage,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
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
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		messageService.setReadStatusFalse(messageId, boxType);

		return "redirect:/message-module/message-box-inbox";
	}

	@RequestMapping("/create-new-message")
	public String createNewMessage(HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		return "create-new-message";

	}

	@RequestMapping("/reply-message")
	public String createReplyMessage(@RequestParam("messageId") int messageId, Model theModel,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		Message message = messageService.getMessage(messageId);
		
		theModel.addAttribute("message", message);

		return "create-new-message";
	}

	@PostMapping("/send-message")
	public String sendMessage(@RequestParam("recipient") String email, @RequestParam("subject") String subject,
			@RequestParam("textArea") String text, RedirectAttributes redirectAttributesa, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if (!loginAndAccessLevelCheck.loginCheck((Integer) session.getAttribute("userId"))
				|| !loginAndAccessLevelCheck.isCustomer((String) session.getAttribute("userAccessLevel")))
			return "redirect:/user/logout";

		try {
			User recipient = userService.getUser(email);
			User sender = userService.getUser((int) session.getAttribute("userId"));
			if (recipient == null) {
				redirectAttributesa.addAttribute("systemMessage", "Nieprawid³owy adres email odbiorcy");
				return "redirect:/message-module/message";
			}
			Message message = new Message();
			message.setRecipientIsActive(true);
			message.setRecipientIsRead(false);
			message.setSenderIsActive(true);
			message.setSenderIsRead(true);
			message.setRecipient(recipient);
			message.setSender(sender);
			message.setStartDate(new Date());
			message.setSubject(subject);
			message.setText(text);

			messageService.sendMessage(message);

			redirectAttributesa.addAttribute("systemMessage", "Wiadomoœæ zosta³a wys³ana.");
			return "redirect:/message-module/message-box-inbox";

		} catch (Exception e) {
			redirectAttributesa.addAttribute("systemMessage", "B³¹d wysy³ania wiadomoœci.");
			return "redirect:/message-module/message-box-inbox";
		}
	}
}
