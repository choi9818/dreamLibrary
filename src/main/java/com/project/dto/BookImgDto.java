package com.project.dto;

import org.modelmapper.ModelMapper;

import com.project.entity.BookImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookImgDto {
	
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static BookImgDto of(BookImg bookImg) {
		return modelMapper.map(bookImg, BookImgDto.class);
	}

}
