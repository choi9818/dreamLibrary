package com.project.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.BookFormDto;
import com.project.dto.BookImgDto;
import com.project.dto.BookListDto;
import com.project.dto.BookSearchDto;
import com.project.entity.Book;
import com.project.entity.BookImg;
import com.project.repository.BookImgRepository;
import com.project.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
	
	private final BookRepository bookRepository;
	private final BookImgService bookImgService;
	private final BookImgRepository bookImgRepository;
	
	/**
	 * 등록
	 * 
	 * */
	public String saveBook(BookFormDto bookFormDto, List<MultipartFile> bookImgFileList) throws Exception {
		
		// 도서 등록
		Book book = bookFormDto.createBook();
		bookRepository.save(book);
		
		// 이미지 등록
		for(int i = 0; i < bookImgFileList.size(); i++) {
			BookImg bookImg = new BookImg();
			bookImg.setBook(book);
			bookImgService.saveBookImg(bookImg, bookImgFileList.get(i));
		}
		
		return book.getIsbn();
	}
	
	/**
	 * 수정
	 * 
	 * */
	// 도서 상세 조회
	@Transactional(readOnly = true)
	public BookFormDto getBookDtl(String bookIsbn) {
		
		List<BookImg> bookImgList = bookImgRepository.findByBookIsbnOrderByIdAsc(bookIsbn);
		List<BookImgDto> bookImgDtoList = new ArrayList<>();
		for (BookImg bookImg : bookImgList) {
			BookImgDto bookImgDto = BookImgDto.of(bookImg);
			bookImgDtoList.add(bookImgDto);
		}
		
		Book book = bookRepository.findById(bookIsbn).orElseThrow(EntityNotFoundException::new);
		BookFormDto bookFormDto = BookFormDto.of(book);
		bookFormDto.setBookImgDtoList(bookImgDtoList);
		
		return bookFormDto;
	}
	
	public String updateBook(BookFormDto bookFormDto, List<MultipartFile> bookImgFileList) throws Exception {
		
		// 도서 수정
		Book book = bookRepository.findById(bookFormDto.getIsbn()).orElseThrow(EntityNotFoundException::new);
		book.updateBook(bookFormDto);
		
		List<Long> bookImgIds = bookFormDto.getBookImgIds();
		
		// 이미지 등록
		for(int i = 0; i < bookImgFileList.size(); i++) {
			bookImgService.updateBookImg(bookImgIds.get(i), bookImgFileList.get(i));
		}
		
		return book.getIsbn();
	}
	
	/**
	 * 도서 관리
	 * */
	@Transactional(readOnly = true)
	public Page<Book> getAdminBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		return bookRepository.getAdminBookPage(bookSearchDto, pageable);
	}
	
	/**
	 * 통합 검색
	 * 
	 * */
	@Transactional(readOnly = true)
	public Page<BookListDto> getInteSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		return bookRepository.getInteSearchBookPage(bookSearchDto, pageable);
	}
	
	/**
	 * 신착자료 검색
	 * 
	 * */
	@Transactional(readOnly = true)
	public Page<BookListDto> getSearchRecentBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		return bookRepository.getSearchRecentBookPage(bookSearchDto, pageable);
	}
	
	/**
	 * 상세 검색
	 * 
	 * */
	@Transactional(readOnly = true)
	public Page<BookListDto> getDetailedSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		return bookRepository.getDetailedSearchBookPage(bookSearchDto, pageable);
	}

}
