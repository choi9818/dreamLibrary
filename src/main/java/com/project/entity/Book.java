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
import com.project.exception.OutOfCountException;

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
	
	//-----------------------------------------------------
	
	// 도서 수정
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
	
	// 대출예약가능횟수(count) 감소 로직
	public void minusCount() {
		// 예약가능횟수가 1번 줄어들고 남은 가능횟수를 계산
		int restCount = this.count - 1;
	
		if(restCount == 0) {
			this.bookStatus = BookStatus.IMPOSSIBLE;
		} else if(restCount < 0) {
			throw new OutOfCountException("현재 예약이 불가능한 도서입니다.");
		}
		
		// 남은 가능횟수를 현재 가능횟수로 할당
		this.count = restCount;
	}
	
	// 대출예약가능횟수(count) 증가
	public void plusCount() {
		int restCount = this.count + 1;
		
		this.bookStatus = BookStatus.POSSIBLE;
		this.count = restCount;
	}

}
