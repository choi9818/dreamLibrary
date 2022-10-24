package com.project.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.constant.BookStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookListDto {
	
	private String isbn;
	
	private String category;
	
	private String title;
	
	private String author;
	
	private String publisher;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate publishedDate;
	
	private BookStatus bookStatus;
	
	private String imgUrl;

	@QueryProjection
	public BookListDto(String isbn, String category, String title, String author, String publisher,
			LocalDate publishedDate, BookStatus bookStatus, String imgUrl) {
		this.isbn = isbn;
		this.category = category;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publishedDate = publishedDate;
		this.bookStatus = bookStatus;
		this.imgUrl = imgUrl;
	}

}
