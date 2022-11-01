package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inter_book")
@Getter @Setter
public class InterBook extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inter_book_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isbn")
	private Book book;
		/*
		 * 한가지 책은 여러번 관심도서가 될 수 있음
		 * 	-> InterBook : Book = 다 : 일
		 * 	-> Many To One
		 *  
		 * */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inter_id")
	private Inter inter;
		/*
		 * 한개의 관심도서목록에 여러개의 관심도서(책)이 있음
		 * 	-> InterBook : Inter = 다 : 일
		 * 	-> Many To One
		 *  
		 * */
	
	//--------------------------------------------
	// 관심도서목록 생성
	public static InterBook createInterBook(Inter inter, Book book) {
		InterBook interBook = new InterBook();
		interBook.setInter(inter);
		interBook.setBook(book);
		return interBook;
	}

}
