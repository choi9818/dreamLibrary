package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.project.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>
	, QuerydslPredicateExecutor<Post>, PostRepositoryCustom{
}
