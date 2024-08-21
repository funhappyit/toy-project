package com.example.bloghome.controller;

import com.example.bloghome.dto.UserProfileDTO;
import com.example.bloghome.model.User;
import com.example.bloghome.repository.UserRepository;
import com.example.bloghome.service.UserProfileCacheService;
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
	private final UserProfileCacheService userProfileCacheService;



	@Autowired
	public UserProfileController(UserRepository userRepository, UserProfileCacheService userProfileCacheService) {
		this.userRepository = userRepository;
		this.userProfileCacheService = userProfileCacheService;
	}
	@GetMapping
	public String showSignupForm() {
		return "profile";  // signup.html을 반환
	}

	@PostMapping
	public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();

		// Redis 캐시에서 프로필 조회
		UserProfileDTO cachedProfile = userProfileCacheService.getCachedUserProfile(username);
		if (cachedProfile != null) {
			return ResponseEntity.ok(cachedProfile);
		}

		// 캐시가 없는 경우, 데이터베이스에서 조회
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		// 사용자 프로필 DTO 생성
		String profilePictureUrl = user.getProfilePictureUrl();
		UserProfileDTO userProfileDTO = new UserProfileDTO(user.getUsername(), user.getEmail(), profilePictureUrl);

		// Redis 캐시에 프로필 저장
		userProfileCacheService.cacheUserProfile(username, userProfileDTO);

		return ResponseEntity.ok(userProfileDTO);
	}
}