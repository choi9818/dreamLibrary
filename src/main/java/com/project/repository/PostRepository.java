package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.project.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>
	, QuerydslPredicateExecutor<Post>, PostRepositoryCustom{
	
	Optional<Post> findById(Long postId);
	
	@Query(value="select m.email from member m join post p on m.member_id = p.member_id "
			+ "where p.post_id = :post_id", nativeQuery = true)
	String getPostMemberEmail(@Param("post_id") Long postId);
}
