package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.project.dto.NoticeFormDto;

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
@Table(name = "notice")
@Getter @Setter @ToString
public class Notice extends BaseEntity {
	
	@Id
	@Column(name = "notice_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;			// 제목
	
	@Lob
	private String content;			// 내용
	
//	@OneToOne(mappedBy = "notice", fetch = FetchType.LAZY)
//	@JoinColumn(name = "notice_img_id")
//	private NoticeImg noticeImg;
//	/*
//	 * 한가지 공지사항에 여러 이미지가 있음
//	 * 	-> postImg : post = 다 : 일
//	 * 	-> Many To One
//	 * 
//	 * */
	
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Member member;			// 작성자(member의 name
//		/*
//		 * 회원 한명이 여러 개의 공지사항을 작성할 수 있음
//		 * 	-> Notice : Member = 다 : 일
//		 * 	-> ManyToOne
//		 *  
//		 * */
//	
	//공지 업데이트 로직
	public void updateNotice(NoticeFormDto noticeFormDto) {
		this.title = noticeFormDto.getTitle();
		this.content = noticeFormDto.getContent();
	}

}
