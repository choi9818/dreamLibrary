package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.BookListDto;
import com.project.dto.BookSearchDto;
import com.project.entity.Book;

public interface BookRepositoryCustom {
	
	// 도서 관리
	Page<Book> getAdminBookPage(BookSearchDto bookSearchDto, Pageable pageable);
	
	// 도서 검색
	Page<BookListDto> getSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable);
	

}
