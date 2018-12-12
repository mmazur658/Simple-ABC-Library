package pl.mazur.simpleabclibrary.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The controller class is used to return the view depending on the user
 * request. This controller contains the views of: <br>
 * <ul>
 * <li>"errors-and-problems"</li>
 * <li>"configuration"</li>
 * <li>"test-page"</li>
 * </ul>
 * 
 * @author Marcin Mazur
 *
 */
@Controller
@Scope("session")
@RequestMapping("/adminstrator")
public class AdminController {

	/**
	 * Returns the view of "errors-and-problems".
	 * 
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/errors-and-problems")
	public String showRrrorsAndProblems() {

		return "errors-and-problems";
	}

	/**
	 * Returns the view of "configuration"
	 * 
	 * @return The String representing the name of the view
	 */
	@RequestMapping("/configuration")
	public String showConfiguration() {

		return "configuration";
	}

	/**
	 * Returns the view of "test-page"
	 * 
	 * @return The String representing the name of the view
	 */
	@RequestMapping("test-page")
	public String showTestPage() {

		return "test-page";
	}

}
