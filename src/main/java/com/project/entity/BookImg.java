package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_img")
@Getter	@Setter
public class BookImg extends BaseTimeEntity {
	
	@Id
	@Column(name = "book_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName;			// 이미지 파일명
	
	private String oriImgName;		// 원본 이미지 파일명
	
	private String imgUrl;			// 이미지 조회 경로
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isbn")
	private Book book;
		/*
		 * 한가지 책에 한개의 책이미지가 있음
		 * 	-> BookImg : Book = 일 : 일
		 * 	-> Many To One
		 * 
		 * */
	
	// 이미지 정보 업데이트 메소드
	public void updateBookImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
		/*
		 * 원본 이미지 파일명, 업데이트할 이미지 파일명, 이미지 경로를 파라미터로 입력받아 이미지 정보를 업데이트
		 * */

}
