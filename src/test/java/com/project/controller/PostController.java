package com.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.dto.CommentDto;
import com.project.entity.Comment;
import com.project.repository.CommentRepository;
import com.project.service.CommentService;

@SpringBootTest
public class PostController {
	@Autowired CommentRepository commentRepository;

//	List<Comment> comments = commentRepository.getCommentsByPostId(postId); 
//	System.out.println("comments=="+comments);
//	List<CommentDto> commentDtos 
//		= comments.stream().map(c->{return CommentService.of(c);}).collect(Collectors.toList());
}
