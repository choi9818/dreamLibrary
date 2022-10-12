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
import lombok.ToString;

@Entity
@Table(name = "post_img")
@Getter @Setter @ToString
public class PostImg extends BaseTimeEntity {
	
	@Id
	@Column(name = "post_img_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String imgName;			// 이미지 파일명
	
	private String oriImgName;		// 원본 이미지 파일명
	
	private String imgUrl;			// 이미지 조회 경로
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
	/*
	 * 한가지 게시물에 여러 이미지가 있음
	 * 	-> postImg : post = 다 : 일
	 * 	-> Many To One
	 * 
	 * */
	
	// 이미지 정보 업데이트 메소드
	public void updatePostImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
		/*
		 * 원본 이미지 파일명, 업데이트할 이미지 파일명, 이미지 경로를 파라미터로 입력받아 이미지 정보를 업데이트
		 * */


}
