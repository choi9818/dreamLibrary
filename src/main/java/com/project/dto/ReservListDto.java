package com.project.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.constant.BookStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservListDto {
	
	private Long reservBookId;
	
	private String isbn;
	
	private String title;
	
	private String author;
	
	private String publisher;
	
	private String imgUrl;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate reservDate;
	
	private BookStatus bookStatus;

	//---------------------------------------------------
	public ReservListDto(Long reservBookId, String isbn, String title, String author, String publisher, String imgUrl,
			LocalDate reservDate, BookStatus bookStatus) {
		this.reservBookId = reservBookId;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.imgUrl = imgUrl;
		this.reservDate = reservDate;
		this.bookStatus = bookStatus;
	}

	
	
	
	
//	public ReservListDto(ReservBook reservBook, String imgUrl) {
//		this.reservBookId = reservBook.getId();
//		this.isbn = reservBook.getBook().getIsbn();
//		this.title = reservBook.getBook().getTitle();
//		this.author = reservBook.getBook().getAuthor();
//		this.publisher = reservBook.getBook().getPublisher();
//		this.imgUrl = imgUrl;
//		this.reservDate = reservBook.getReserv().getReservDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//		this.bookStatus = reservBook.getBook().getBookStatus();
//	}

}
