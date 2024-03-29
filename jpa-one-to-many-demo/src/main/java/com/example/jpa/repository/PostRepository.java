package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpa.model.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
