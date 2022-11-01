package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.BookListDto;
import com.project.dto.BookSearchDto;
import com.project.entity.Book;

public interface BookRepositoryCustom {
	
	// 도서 관리
	Page<Book> getAdminBookPage(BookSearchDto bookSearchDto, Pageable pageable);
	
	// 통합 검색
	Page<BookListDto> getInteSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable);
	
	// 신착자료 검색
	Page<BookListDto> getSearchRecentBookPage(BookSearchDto bookSearchDto, Pageable pageable);
	
	// 상세 검색
	Page<BookListDto> getDetailedSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable);

}
