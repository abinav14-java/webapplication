package com.abinav.webapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/register")
	public String showForm() {
		return "register"; // Loads templates/register.html
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login"; // this maps to templates/login.html
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard"; // Looks for dashboard.html in templates
	}
}
