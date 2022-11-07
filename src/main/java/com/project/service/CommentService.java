package com.project.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.CommentDto;
import com.project.dto.PostFormDto;
import com.project.entity.Comment;
import com.project.entity.Member;
import com.project.entity.Post;
import com.project.repository.CommentRepository;
import com.project.repository.MemberRepository;
import com.project.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
	private final MemberRepository memberRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	
	//댓글 등록
	public Long saveComment(CommentDto commentDto) {
		System.out.println("service****commentDto"+commentDto);
		System.out.println("**********#####*************");
		Comment comment = new Comment();
		comment.setContent(commentDto.getContent());
		Member member = memberRepository.findByEmail(commentDto.getEmail());
		Post post = postRepository.findById(commentDto.getPostId()).get();
		comment.setMember(member);
		comment.setPost(post);
		
		System.out.println("***********************");
		System.out.println("-----------"+comment);
		comment = commentRepository.save(comment);
		System.out.println(comment);
		return comment.getId();
	}

	public CommentDto of(Comment comment) {
		CommentDto commentDto = new CommentDto();
		commentDto.setContent(comment.getContent());
		commentDto.setCommentId(comment.getId());
		commentDto.setEmail(comment.getMember().getEmail());
		commentDto.setPostId(comment.getPost().getId());
		commentDto.setRegTime(comment.getRegTime());
		return commentDto;		
	}
	
	//삭제
	public void deletePostComment(Long postId) {
		Comment comment = commentRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
		commentRepository.delete(comment);
	}
}
