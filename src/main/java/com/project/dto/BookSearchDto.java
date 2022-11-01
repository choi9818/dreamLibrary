package com.project.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookSearchDto {
	
	// 키워드 검색 (통합검색, 관리자페이지)
	private String searchBy;
	private String searchQuery = "";

	// 신착자료 검색
	private String searchDateType;
	
	// 상세검색 : 도서명, 저자, 출판사, 카테고리, 출간일자(기간)
	private String searchTitle;
	private String searchAuthor;
	private String searchPublisher;
	private String searchCategory;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

}
