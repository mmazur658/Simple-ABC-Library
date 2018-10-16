package pl.mazur.simpleabclibrary.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("session")
@RequestMapping("/adminstrator")
public class AdminController {

	@RequestMapping("/errors-and-problems")
	public String errorsAndProblems() {

		return "errors-and-problems";
	}

	@RequestMapping("/configuration")
	public String configuration() {

		return "configuration";
	}

	@RequestMapping("test-page")
	public String testPage() {

		return "test-page";
	}

}
