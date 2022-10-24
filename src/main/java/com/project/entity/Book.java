package com.project.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.project.constant.BookStatus;
import com.project.dto.BookFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book")
@Getter @Setter @ToString
public class Book extends BaseTimeEntity {
	
	@Id
	private String isbn;
	
	private String category;
	
	private String title;
	
	private String author;
	
	private String publisher;
	
	private LocalDate publishedDate;
	
	@Lob
	private String bookDetail;
	
	private String place;
	
	private int count;
	
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;
	
	public void updateBook(BookFormDto bookFormDto) {
		this.isbn = bookFormDto.getIsbn();
		this.category = bookFormDto.getCategory();
		this.title = bookFormDto.getTitle();
		this.author = bookFormDto.getAuthor();
		this.publisher = bookFormDto.getPublisher();
		this.publishedDate = bookFormDto.getPublishedDate();
		this.bookDetail = bookFormDto.getBookDetail();
		this.place = bookFormDto.getPlace();
		this.count = bookFormDto.getCount();
		this.bookStatus = bookFormDto.getBookStatus();
	}

}
