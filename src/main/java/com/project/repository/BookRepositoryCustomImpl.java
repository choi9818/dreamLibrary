package com.project.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.project.dto.BookListDto;
import com.project.dto.BookSearchDto;
import com.project.dto.QBookListDto;
import com.project.entity.Book;
import com.project.entity.QBook;
import com.project.entity.QBookImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public BookRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	/**
	 * 검색조건 설정
	 * 
	 * */
	
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
	
	// 신착자료 검색
	private BooleanExpression pubDatesAfter(String searchDateType) {
		LocalDate date = LocalDate.now();
		
		if(StringUtils.equals("1w", searchDateType)) {
			date = date.minusWeeks(1);
		} else if(StringUtils.equals("1m", searchDateType)) {
			date = date.minusMonths(1);
		} else if(StringUtils.equals("6m", searchDateType)) {
			date = date.minusMonths(6);
		}
		
		return QBook.book.publishedDate.after(date);
	}
	
	// 상세 검색
	// 도서명
	private BooleanExpression searchTitleLike(String searchTitle) {
		return searchTitle == null ? null : QBook.book.title.like("%" + searchTitle + "%");
	}
	// 저자
	private BooleanExpression searchAuthorLike(String searchAuthor) {
		return searchAuthor == null ? null : QBook.book.author.like("%" + searchAuthor + "%");
	}
	// 출판사
	private BooleanExpression searchPublisherLike(String searchPublisher) {
		return searchPublisher == null ? null : QBook.book.publisher.like("%" + searchPublisher + "%");
	}
	// 카테고리
	private BooleanExpression searchCategoryEq(String searchCategory) {
		
		String[] categories = {"소설", "시/에세이", "인문", "경제/경영", "가정/육아", "요리", "건강", 
							   "취미/실용/스포츠", "자기계발", "컴퓨터/IT", "정치/사회", "역사/문화", "종교", 
							   "예술/대중문화", "기술/공학", "과학", "청소년", "유아", "어린이"};
		
		if(StringUtils.equals("전체", searchCategory)) {
			return null;
		} else {
			
			for(int i = 0; i < categories.length - 1; i++) {
				if(StringUtils.equals(categories[i], searchCategory)) {
					return QBook.book.category.eq(searchCategory);
				}
			}
		}
	
		
		return null;
	}
	// 기간
	private BooleanExpression searchPubDateBetween(LocalDate startDate, LocalDate endDate) {
		if(startDate == null) {
			startDate = LocalDate.of(1800, 1, 1);
		} else if(endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		return QBook.book.publishedDate.between(startDate, endDate);
	}

	//--------------------------------------------------------------------------

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
	public Page<BookListDto> getInteSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		
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

	/**
	 * 신착자료 검색
	 * */
	@Override
	public Page<BookListDto> getSearchRecentBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		
		QBook book = QBook.book;
		QBookImg bookImg = QBookImg.bookImg;
		
		List<BookListDto> content = queryFactory
				.select(new QBookListDto(book.isbn, book.category, book.title, book.author, book.publisher, book.publishedDate, book.bookStatus, bookImg.imgUrl))
				.from(bookImg)
				.join(bookImg.book, book)
				.where(pubDatesAfter(bookSearchDto.getSearchDateType()))
				.orderBy(book.publishedDate.asc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(bookImg)
				.join(bookImg.book, book)
				.where(pubDatesAfter(bookSearchDto.getSearchDateType()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}

	
	/**
	 * 상세 검색
	 * 
	 * */
	@Override
	public Page<BookListDto> getDetailedSearchBookPage(BookSearchDto bookSearchDto, Pageable pageable) {
		
		QBook book = QBook.book;
		QBookImg bookImg = QBookImg.bookImg;
		
		List<BookListDto> content = queryFactory
				.select(new QBookListDto(book.isbn, book.category, book.title, book.author, book.publisher, book.publishedDate, book.bookStatus, bookImg.imgUrl))
				.from(bookImg)
				.join(bookImg.book, book)
				.where(searchTitleLike(bookSearchDto.getSearchTitle()),
					   searchAuthorLike(bookSearchDto.getSearchAuthor()),
					   searchPublisherLike(bookSearchDto.getSearchPublisher()),
					   searchCategoryEq(bookSearchDto.getSearchCategory()),
					   searchPubDateBetween(bookSearchDto.getStartDate(), bookSearchDto.getEndDate()))
				.orderBy(book.publishedDate.asc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(bookImg)
				.join(bookImg.book, book)
				.where(searchTitleLike(bookSearchDto.getSearchTitle()),
					   searchAuthorLike(bookSearchDto.getSearchAuthor()),
					   searchPublisherLike(bookSearchDto.getSearchPublisher()),
					   searchCategoryEq(bookSearchDto.getSearchCategory()),
					   searchPubDateBetween(bookSearchDto.getStartDate(), bookSearchDto.getEndDate()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}



	
}
