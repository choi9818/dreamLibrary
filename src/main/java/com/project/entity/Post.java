package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "post")
@Getter @Setter @ToString(exclude = {"member"})
public class Post extends BaseEntity {
	
	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;			// 제목
	
	@Lob
	private String content;			// 내용
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;			// 작성자
		/*
		 * 회원 한명이 여러 개의 게시물을 작성할 수 있음
		 * 	-> Post : Member = 다 : 일
		 * 	-> ManyToOne
		 *  
		 * */


}
