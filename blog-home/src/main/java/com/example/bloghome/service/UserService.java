package com.example.bloghome.service;

import com.example.bloghome.model.User;
import com.example.bloghome.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	// MacBook의 Desktop/study/uploads 경로
	private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/study/uploads/";

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void registerUser(String username, String password,String email) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setEmail(email);
		user.setRole("USER");
		userRepository.save(user);
	}

	public void updateProfilePicture(String username, MultipartFile file) throws IOException {
		User user = userRepository.findByUsername(username);
		String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path uploadPath = Paths.get(UPLOAD_DIR);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		// 파일 저장 경로 설정
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path path = uploadPath.resolve(filename);
		Files.write(path, file.getBytes());

		// 파일 URL을 DB에 저장
		user.setProfilePictureUrl("/uploads/" + fileName);
		userRepository.save(user);
	}
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
