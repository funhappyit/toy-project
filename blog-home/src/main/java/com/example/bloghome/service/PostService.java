package com.example.bloghome.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bloghome.model.Category;
import com.example.bloghome.model.Post;
import com.example.bloghome.model.Tag;
import com.example.bloghome.model.User;
import com.example.bloghome.repository.CategoryRepository;
import com.example.bloghome.repository.PostRepository;
import com.example.bloghome.repository.TagRepository;
import com.example.bloghome.repository.UserRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TagRepository tagRepository;

	public Post createPost(String title, String content, Long userId, Long categoryId, List<Long> tagIds){
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
		Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));

		Post post = new Post();
		post.setTitle(title);
		post.setContent(content);
		post.setUser(user);
		post.setCategory(category);
		post.setTags(tags);

		return postRepository.save(post);
	}
	// 모든 포스트를 조회하는 메서드
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}


}
