package com.project.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.project.constant.BookStatus;
import com.project.entity.Book;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookFormDto {
	
	@NotBlank(message = "ISBN은 필수입력값입니다.")
	private String isbn;
	
	private String category;
	
	@NotBlank(message = "도서명은 필수입력값입니다.")
	private String title;
	
	private String author;
	
	private String publisher;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate publishedDate;
	
	private String bookDetail;
	
	private String place;
	
	private int count;
	
	private BookStatus bookStatus;
	
	//----------------------------------
	
	private List<BookImgDto> bookImgDtoList = new ArrayList<>();
	
	private List<Long> bookImgIds = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Book createBook() {
		return modelMapper.map(this, Book.class);
	}
	
	public static BookFormDto of(Book book) {
		return modelMapper.map(book, BookFormDto.class);
	}
	

}
