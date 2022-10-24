package com.project.dto;

import org.modelmapper.ModelMapper;

import com.project.entity.NoticeImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeImgDto {
	private Long id;
	private String imgName;			// 이미지 파일명	
	private String oriImgName;		// 원본 이미지 파일명
	private String imgUrl;			// 이미지 조회 경로
	//멤버 변수로 ModelMapper 객체를 추가
	private static ModelMapper modelMapper = new ModelMapper();
	//NoticeImg 엔티티객체를 파라미터로 받아서 NoticeImg 객체의 자료형과 멤버 변수의 이름이 같을 때 
	//NoticeImgDto로 값을 복사해서 반환, static 메소드로 선언해서 NoticeImgDto 객체를 생성하지 않아도 호출할 수 있도록
	public static NoticeImgDto of(NoticeImg noticeImg) {
		return modelMapper.map(noticeImg, NoticeImgDto.class);
	}
}
