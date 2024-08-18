package com.example.bloghome.controller;

import com.example.bloghome.dto.UserProfileDTO;
import com.example.bloghome.model.User;
import com.example.bloghome.repository.UserRepository;
import com.example.bloghome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

	private final UserRepository userRepository;
	private final UserService userService;
	@Autowired
	public UserProfileController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	@GetMapping
	public String showSignupForm() {
		return "profile";  // signup.html을 반환
	}

	@PostMapping
	public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userRepository.findByUsername(userDetails.getUsername());
		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		String profilePictureUrl = user.getProfilePictureUrl() != null
				? "/files/profile-picture/" + user.getProfilePictureUrl()
				: "/files/profile-picture/default-profile.png";

		UserProfileDTO userProfileDTO = new UserProfileDTO(user.getUsername(), user.getEmail(), profilePictureUrl);
		return ResponseEntity.ok(userProfileDTO);
	}
}