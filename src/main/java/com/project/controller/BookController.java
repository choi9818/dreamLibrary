package com.project.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.BookFormDto;
import com.project.dto.BookListDto;
import com.project.dto.BookSearchDto;
import com.project.entity.Book;
import com.project.service.BookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {
	
	private final BookService bookService;
	
	/**
	 * 도서 등록
	 * 
	 * */
	@GetMapping(value = "/admin/book/new") // /localhost:8080/admin/book/new
	public String bookForm(Model model) {
		model.addAttribute("bookFormDto", new BookFormDto());
		return "/book/bookForm";
	}
	
	@PostMapping(value = "/admin/book/new")
	public String bookNew(@Valid BookFormDto bookFormDto,
						  BindingResult bindingResult,
						  Model model,
						  @RequestParam("bookImgFile") List<MultipartFile> bookImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "book/bookForm";
		}
		
		try {
			bookService.saveBook(bookFormDto, bookImgFileList);
		} catch(Exception e) {
			model.addAttribute("errorMessage", "도서등록 중 에러가 발생했습니다.");
			return "book/bookForm";
		}
		
		return "redirect:/book/bookMng";
	}
	
	/**
	 * 수정
	 * 
	 * */
	@GetMapping(value = "/admin/book/{bookIsbn}")
	public String bookDtl(@PathVariable("bookIsbn") String bookIsbn, Model model) {
		try {
			BookFormDto bookFormDto = bookService.getBookDtl(bookIsbn);
			model.addAttribute("bookFormDto", bookFormDto);
		} catch(EntityNotFoundException e) {
			model.addAttribute("errorMessage", "존재하지 않는 도서입니다.");
			model.addAttribute("bookFormDto", new BookFormDto());
			return "book/bookForm";
		}
		
		return "book/bookForm";
	}
	
	@PostMapping(value = "/admin/book/{bookIsbn}")
	public String bookUpdate(@Valid BookFormDto bookFormDto,
							 BindingResult bindingResult,
							 @RequestParam("bookImgFile") List<MultipartFile> bookImgFileList,
							 Model model) {
		
		if(bindingResult.hasErrors()) {
			return "book/bookForm";
		}
		
		try {
			bookService.updateBook(bookFormDto, bookImgFileList);
		} catch(Exception e) {
			model.addAttribute("errorMessage", "도서수정 중 에러가 발생했습니다.");
			return "book/bookForm";
		}
		
		return "redirect:/book/bookMng";
	}
	
	/**
	 * 도서 관리
	 * */
	@GetMapping(value = {"/admin/books", "/admin/books/{page}"})
	public String bookManage(BookSearchDto bookSearchDto,
							 @PathVariable("page") Optional<Integer> page,
							 Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		Page<Book> books = bookService.getAdminBookPage(bookSearchDto, pageable);
		
		model.addAttribute("books", books);
		model.addAttribute("bookSearchDto", bookSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "book/bookMng";
	}
	
	/**
	 * 검색 - 통합 검색 페이지
	 * */
	@GetMapping(value = {"/search/integrated", "/search/integrated/{page}"})
	public String bookSearch(BookSearchDto bookSearchDto,
							 @PathVariable("page") Optional<Integer> page,
							 Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
		Page<BookListDto> books = bookService.getSearchBookPage(bookSearchDto, pageable);
		
		model.addAttribute("books", books);
		model.addAttribute("bookSearchDto", bookSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "book/bookList";
	}
	
	/**
	 * 도서 상세 페이지
	 * 
	 * */
	@GetMapping(value = "/search/bookdtl/{bookIsbn}")
	public String bookDtl(Model model, @PathVariable("bookIsbn") String bookIsbn) {
		BookFormDto bookFormDto = bookService.getBookDtl(bookIsbn);
		model.addAttribute("book", bookFormDto);
		return "book/bookDtl";
	}
}
