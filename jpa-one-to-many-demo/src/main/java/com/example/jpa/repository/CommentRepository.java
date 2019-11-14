package com.example.jpa.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpa.model.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	
	Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
	
	Optional<CommentEntity> findByIdAndPostId(Long id, Long postId);
	
	

}
