package com.example.jpablog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpablog.model.User;
import com.example.jpablog.repository.UserRepository;
import com.example.jpablog.service.UserService;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/profile")
public class UserProfileController {

	private final UserRepository userRepository;

	@Autowired
	public UserProfileController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping
	public String getProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		User user = userRepository.findByUsername(userDetails.getUsername());
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		model.addAttribute("user", user);
		return "profile";  // profile.html 파일을 반환
	}
}