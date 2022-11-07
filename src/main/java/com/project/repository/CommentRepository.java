package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	@Query(value="select post_id from comment where comment_id = :comment_id", nativeQuery = true)
	Long getPostIdByCommnetId(@Param("comment_id")Long commentId);
	
	@Query(value="select c from Comment c join Post p on c.post.id = p.id where c.post.id = :postid")
	List<Comment> getCommentsByPostId(@Param("postid")Long postId);
}
