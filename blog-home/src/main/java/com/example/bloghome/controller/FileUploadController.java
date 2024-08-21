package com.example.bloghome.controller;

import com.example.bloghome.model.User;
import com.example.bloghome.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    // MacBook의 Desktop/study/uploads 경로
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/study/uploads/";
    private final UserRepository userRepository;
    public FileUploadController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    @PostMapping("/profile/upload")
    public RedirectView uploadProfilePicture(@RequestParam("username") String username,@RequestParam("file") MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username);
        // 파일 저장 경로
        Path uploadPath =  Paths.get("C:/workspace(2022Intellj)/toy-project3/src/main/java/resources/static/uploads/");


        // 디렉토리 존재 여부 확인 및 생성
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        user.setProfilePictureUrl("/uploads/" + fileName);
        userRepository.save(user);
        // 리다이렉트 URL (파일이 업로드된 후 프로필 페이지로 돌아가게 함)
        return new RedirectView("/profile");
    }

}
