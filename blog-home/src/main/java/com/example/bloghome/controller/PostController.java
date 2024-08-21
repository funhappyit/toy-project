package com.example.bloghome.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bloghome.model.Category;
import com.example.bloghome.model.Post;
import com.example.bloghome.model.Tag;
import com.example.bloghome.model.User;
import com.example.bloghome.repository.CategoryRepository;
import com.example.bloghome.repository.TagRepository;
import com.example.bloghome.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	@Autowired
	private PostService postService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TagRepository tagRepository;

	@GetMapping("/posts/create")
	public String showCreatePostForm(Model model) {
		List<Category> categories = categoryRepository.findAll();
		List<Tag> tags = tagRepository.findAll();

		System.out.println("Categories: " + categories.get(0).getId()); // 디버깅 로그
		System.out.println("Tags: " + tags); // 디버깅 로그

		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		return "create-post";
	}

	@PostMapping("/posts/create")
	public String createPost(
		@RequestParam String title,
		@RequestParam String content,// 실제 프로젝트에서는 세션에서 가져옴
		@RequestParam Long categoryId,
		@RequestParam List<Long> tagIds,
		HttpSession session,
		Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");

		postService.createPost(title, content, loggedInUser.getId(), categoryId, tagIds);
		return "redirect:/posts";  // 게시물 목록 페이지로 리다이렉트
	}

	@GetMapping("/posts")
	public String listPosts(Model model) {
		List<Post> posts = postService.getAllPosts();
		model.addAttribute("posts", posts);
		return "post-list";
	}

}
