package com.example.bloghome.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	// 특정 ID의 포스트를 가져오는 메서드
	public Post getPostById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
		// Force initialization of tags collection
		post.getTags().size();
		return post;
	}


	@Transactional
	public void updatePost(Post post) {
		// 3. 새로운 태그를 설정
		Set<Tag> newTags = new HashSet<>(post.getTags()); // 새로운 태그 집합
		//1. 기존의 Post 엔티티를 데이터베이스에서 로드
		Post existingPost = postRepository.findById(post.getId())
			.orElseThrow(()->new IllegalArgumentException("Invalid post Id"));
		// 2. 기존 태그 연관 관계를 삭제 (즉, Post_Tags 테이블에서 해당 게시물의 태그 삭제)
		existingPost.getTags().clear();


		existingPost.setTags(newTags);

		// 4. 변경된 Post 엔터티를 저장
		// 변경된 Post 엔터티를 저장
		postRepository.save(post);
	}


}
