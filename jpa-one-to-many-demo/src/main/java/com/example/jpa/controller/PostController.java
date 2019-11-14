package com.example.jpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.PostEntity;
import com.example.jpa.repository.PostRepository;

@RestController
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@GetMapping("/posts")
	public Page<PostEntity> getAllPosts(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

	@PostMapping("/posts")
	public PostEntity createPost(@Valid @RequestBody PostEntity post) {
		return postRepository.save(post);
	}

	@PutMapping("/posts/{postId}")
	public PostEntity updatePost(@PathVariable Long postId, @Valid @RequestBody PostEntity postRequest) {
		return postRepository.findById(postId).map(post -> {
			post.setTitle(postRequest.getTitle());
			post.setDescription(postRequest.getDescription());
			post.setContent(postRequest.getContent());
			return postRepository.save(post);
		}).orElseThrow(() -> new ResourceNotFoundException(
				"Can not update Post with post ID: " + postId + ". Post not found."));
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId) {
		return postRepository.findById(postId).map(post -> {
			postRepository.delete(post);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException(
				"Can not delete Post with post ID: " + postId + ". Post not found."));
	}

}
