package com.project.entity;

import java.time.LocalDateTime;

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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notice")
@Getter @Setter @ToString
public class Notice extends BaseTimeEntity {
	
	@Id
	@Column(name = "notice_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;			// 제목
	
	@Lob
	private String content;			// 내용
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer")
	private Member member;			// 작성자
		/*
		 * 회원 한명이 여러 개의 공지사항을 작성할 수 있음
		 * 	-> Notice : Member = 다 : 일
		 * 	-> ManyToOne
		 *  
		 * */
	
	private LocalDateTime regDate;	// 등록일자
	
	private LocalDateTime editDate;	// 수정일자
	
	private int viewCount;			// 조회수
	
	private String file;			// 첨부파일

}
