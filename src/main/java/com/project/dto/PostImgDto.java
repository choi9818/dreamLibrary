package com.project.dto;

import org.modelmapper.ModelMapper;

import com.project.entity.NoticeImg;
import com.project.entity.PostImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostImgDto {
	private Long id;
	private String imgName;			// 이미지 파일명	
	private String oriImgName;		// 원본 이미지 파일명
	private String imgUrl;			// 이미지 조회 경로
	//멤버 변수로 ModelMapper 객체를 추가
	private static ModelMapper modelMapper = new ModelMapper();

	public static PostImgDto of(PostImg postImg) {
		return modelMapper.map(postImg, PostImgDto.class);
	}
}
