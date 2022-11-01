package com.project.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class ReservBook extends BaseTimeEntity {
	
	@Id
	@Column(name = "reserv_book_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isbn")
	private Book book;
		/*
		 * 한개의 책은 여러번 예약될 수 있음
		 * -> ReservBook : Book = 다 : 일
		 * -> Many To One
		 * 
		 * */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reserv_id")
	private Reserv reserv;
		/*
		 * 한개의 예약목록에 여러개의 예약도서(책)이 있음
		 * 	-> ReservBook : Reserv = 다 : 일
		 * 	-> Many To One
		 *  
		 * */
	
	private LocalDate reservDate;		// 예약일
	
	//--------------------------------------------------
	
	// 예약목록 생성
	public static ReservBook createReservBook(Reserv reserv, Book book) {
		
		ReservBook reservBook = new ReservBook();
		
		reservBook.setReserv(reserv);
		reservBook.setReservDate(LocalDate.now());
		book.minusCount();	// 예약가능횟수 -1 처리
		reservBook.setBook(book);
		
		return reservBook;
	}
	
	// 예약 취소
	public void cancel() {
		this.getBook().plusCount();
	}

}
