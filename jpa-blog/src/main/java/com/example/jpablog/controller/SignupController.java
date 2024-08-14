package com.example.jpablog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.jpablog.service.UserService;

@Controller
public class SignupController {
	private final UserService userService;

	public SignupController(UserService userService){
		this.userService = userService;
	}

	@GetMapping("/signup")
	public String showSignupForm() {
		return "signup";  // signup.html을 반환
	}
	@PostMapping("/signup")
	public ModelAndView signup(  @RequestParam("username") String username,
		@RequestParam("password") String password,
		@RequestParam("email") String email) {
		userService.registerUser(username, password,email);
		return new ModelAndView("redirect:/login");
	}

}
