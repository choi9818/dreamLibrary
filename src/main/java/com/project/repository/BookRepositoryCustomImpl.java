package com.project.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.project.dto.BookListDto;
import com.project.dto.BookSearchDto;
import com.project.entity.Book;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.step6.dto.QBookListDto;
import com.step6.entity.QBook;
import com.step6.entity.QBookImg;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public BookRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	// 키워드 검색 : ISBN, 카테고리, 도서명, 저자, 출판사
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		
		if(StringUtils.equals("isbn", searchBy)) {
			return QBook.book.isbn.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("category", searchBy)) {
			return QBook.book.category.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("title", searchBy)) {
			return QBook.book.title.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("author", searchBy)) {
			return QBook.book.author.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("publisher", searchBy)) {
			return QBook.book.publisher.like("%" + searchQuery + "%");
		} 
		
		return null;
	}

	/**
	 * 도서 관리
	 * 
	 * */
	@Override
	public Page<Book> getAdminBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		
		List<Book> content = queryFactory
				.selectFrom(QBook.book)
				.where(searchByLike(bookSearchDto.getSearchBy(), bookSearchDto.getSearchQuery()))
				.orderBy(QBook.book.isbn.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(QBook.book)
				.where(searchByLike(bookSearchDto.getSearchBy(), bookSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}
	
	
	/**
	 * 통합검색
	 * */
	@Override
	public Page<BookListDto> getSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		
		QBook book = QBook.book;
		QBookImg bookImg = QBookImg.bookImg;
		
		List<BookListDto> content = queryFactory
				.select(new QBookListDto(book.isbn, book.category, book.title, book.author, book.publisher, book.publishedDate, book.bookStatus, bookImg.imgUrl))
				.from(bookImg)
				.join(bookImg.book, book)
				.where(searchByLike(bookSearchDto.getSearchBy(), bookSearchDto.getSearchQuery()))
				.orderBy(book.isbn.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(bookImg)
				.join(bookImg.book, book)
				.where(searchByLike(bookSearchDto.getSearchBy(), bookSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression bookTitleLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QBook.book.title.like("%" + searchQuery + "%");
	}



	
}
