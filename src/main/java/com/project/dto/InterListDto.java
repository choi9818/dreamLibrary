package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InterListDto {
	
	private Long interBookId;
	
	private String isbn;
	
	private String title;
	
	private String author;
	
	private String publisher;
	
	private String imgUrl;

	public InterListDto(Long interBookId, String isbn, String title, String author, String publisher, String imgUrl) {
		this.interBookId = interBookId;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.imgUrl = imgUrl;
	}
	
	

}
