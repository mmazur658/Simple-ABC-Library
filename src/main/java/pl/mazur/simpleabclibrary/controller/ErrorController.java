package pl.mazur.simpleabclibrary.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("session")
public class ErrorController {

	@RequestMapping("/error")
	public String showErrorPage(HttpServletRequest request, Model theModel) {

		HttpSession session = request.getSession();

		int errorCode = getErrorCode(request);

		System.out.println("Error code: " + errorCode);

		String errorMessage = "";

		switch (errorCode) {
		case 400: {
			errorMessage = "Http Error Code: 400. Bad Request";
			break;
		}
		case 401: {
			errorMessage = "Http Error Code: 401. Unauthorized";
			break;
		}
		case 404: {
			errorMessage = "Http Error Code: 404. Resource not found";
		}
		case 500: {
			errorMessage = "Http Error Code: 500. Internal Server Error";
			break;
		}
		default: {
			errorMessage = "Nie dzia³a";
			break;
		}
		}

		session.invalidate();
		theModel.addAttribute("errorMessage", errorMessage);

		return "error-page";
	}

	private int getErrorCode(HttpServletRequest request) {

		int errorCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

		return errorCode;
	}

}
