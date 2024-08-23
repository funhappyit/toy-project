package com.example.bloghome.controller;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bloghome.model.Category;
import com.example.bloghome.model.Post;
import com.example.bloghome.model.Tag;
import com.example.bloghome.model.User;
import com.example.bloghome.repository.CategoryRepository;
import com.example.bloghome.repository.TagRepository;
import com.example.bloghome.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@GetMapping("/posts/{id}")
	public String viewPost(@PathVariable Long id, Model model,	HttpSession session) {
		Post post = postService.getPostById(id);
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		model.addAttribute("post", post);
		model.addAttribute( "currentUser", loggedInUser);
		return "post-detail";
	}

	// 수정 페이지로 이동하는 메서드
	@GetMapping("posts/edit/{id}")
	public String editPost(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) throws
		JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Category> categories = categoryRepository.findAll();
		List<Tag> tags = tagRepository.findAll();
		Post post = postService.getPostById(id);

		// 현재 사용자가 게시물의 작성자인지 확인
		if (!post.getUser().getId().equals(user.getId())) {
			return "error/403"; // 403 에러 페이지로 리다이렉트
		}
		// Java 객체를 JSON 문자열로 변환
		// String tagsJson = objectMapper.writeValueAsString(tags);
		// String postTagsJson = objectMapper.writeValueAsString(post.getTags());
		model.addAttribute("post", post);
		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		return "post-edit";
	}

	@PostMapping("/posts/update/{id}")
	public String updatePost(@PathVariable Long id, @AuthenticationPrincipal User user,
		@ModelAttribute("post") Post postForm, Model model) {
		Post existingPost = postService.getPostById(id);

		// 현재 사용자가 게시물의 작성자인지 확인
		if (!existingPost.getUser().getId().equals(user.getId())) {
			return "error/403"; // 403 에러 페이지로 리다이렉트
		}

		// 게시물의 제목과 내용을 업데이트
		existingPost.setTitle(postForm.getTitle());
		existingPost.setContent(postForm.getContent());

		// 카테고리 업데이트
		Category category = categoryRepository.findById(postForm.getCategory().getId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
		existingPost.setCategory(category);

		// 태그 업데이트
		List<Tag> tags = tagRepository.findAllById(postForm.getTags().stream()
			.map(Tag::getId)
			.collect(Collectors.toList()));
		existingPost.setTags(new HashSet<>(tags));

		// 변경된 게시물 저장
		postService.updatePost(existingPost);

		return "redirect:/posts/" + id; // 업데이트 후 게시물 상세 페이지로 리다이렉트
	}








}
