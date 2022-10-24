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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_img")
@Getter @Setter
public class BookImg extends BaseTimeEntity {
	
	@Id
	@Column(name = "book_img_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String imgName;			// 이미지 파일명
	
	private String oriImgName;		// 원본이미지 파일명
	
	private String imgUrl;			// 이미지 조회 경로
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isbn")
	private Book book;
	
	public void updateBookImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}

}
