package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
@Getter @Setter
@ToString(exclude = {"member", "post"})
public class Comment extends BaseEntity {
	
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;					// 댓글 작성자
		/*
		 * 회원 한명이 여러개의 댓글을 작성할 수 있음
		 * 	-> Comment : Member = 다 : 일
		 * 	-> Many To One
		 * 
		 * */
	
	private String content;					// 댓글 내용
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
		/*
		 * 한개의 게시물에는 여러개의 댓글이 달릴 수 있음
		 * 	-> Comment : Post = 다 : 일
		 * 	-> Many To One
		 * 
		 * */

}
