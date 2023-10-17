package com.recipe.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorControllerCustom implements ErrorController {
	
	
	@GetMapping("/error")
	public String errorHandller() {
		return "/error/errorPage";
	}
	
}
