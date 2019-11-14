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
import com.example.jpa.model.CommentEntity;
import com.example.jpa.repository.CommentRepository;
import com.example.jpa.repository.PostRepository;

@RestController
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;

	@GetMapping("posts/{postId}/comments")
	public Page<CommentEntity> getAllComments(@PathVariable(value = "postId") Long postId, Pageable pageable) {
		return commentRepository.findByPostId(postId, pageable);
	}

	@PostMapping("posts/{postId}/comments")
	public CommentEntity createCommnet(@PathVariable(value = "postId") Long postId,
			@Valid @RequestBody CommentEntity comment) {
		return postRepository.findById(postId).map(post -> {
			comment.setPost(post);
			return commentRepository.save(comment);
		}).orElseThrow(() -> new ResourceNotFoundException(
				"Can not create Comment for Post with post ID: " + postId + ". Post not found."));
	}

	@PutMapping("posts/{postId}/comments/{commentId}")
	public CommentEntity updateComment(@PathVariable(value = "postId") Long postId,
			@PathVariable(value = "commentId") Long commentId, @Valid @RequestBody CommentEntity commentRequest) {
		if (postRepository.existsById(postId)) {
			throw new ResourceNotFoundException(
					"Can not create Comment for Post with post ID: " + postId + ". Post not found.");
		}

		return commentRepository.findById(commentId).map(comment -> {
			comment.setText(commentRequest.getText());
			return commentRepository.save(comment);
		}).orElseThrow(
				() -> new ResourceNotFoundException("Can not update Comment with ID: " + commentId + " not found."));
	}

	@DeleteMapping("posts/{postId}/comments/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "postId") Long postId,
			@PathVariable(value = "commentId") Long commentId) {

		return commentRepository.findByIdAndPostId(postId, commentId).map(comment -> {
			commentRepository.delete(comment);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException(
				"Can not delete comment with ID: " + commentId + ". Comment not found"));
	}

}
