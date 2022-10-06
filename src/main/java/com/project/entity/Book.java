package com.project.entity;

import javax.persistence.Entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.project.constant.BookStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book")
@Getter @Setter @ToString
public class Book extends BaseTimeEntity {
	
	@Id
	private String isbn;				// ISBN
	
	private String category;			// 카테고리
	
	private String title;				// 제목
	
	private String author;				// 저자
	
	private String publisher;			// 출판사
	
	private String publishedDate;		// 출판일자
	
	@Lob
	private String bookDetail;			// 설명
	
	private String place;				// 열람실
	
	private int count;					// 대출예약가능횟수
	
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;		// 대출예약상태
	
	private String imageSrc;			// 이미지소스

}
