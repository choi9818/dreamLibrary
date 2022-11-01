package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.dto.ReservListDto;
import com.project.entity.ReservBook;

public interface ReservBookRepository extends JpaRepository<ReservBook, Long> {
	
	// 예약신청할 도서를 조회, 저장
	ReservBook findByReservIdAndBookIsbn(Long reservId, String bookIsbn);
	
	// 현재 로그인한 회원의 예약 데이터 조희	
	@Query(value = "select new com.project.dto.ReservListDto(rb.id, b.isbn, b.title, b.author, b.publisher, i.imgUrl, rb.reservDate, b.bookStatus) "
				 + "from ReservBook rb, BookImg i join rb.book b "
				 + "where i.book.isbn = rb.book.isbn "
				 + "and rb.reserv.id = :reservId "
				 + "order by rb.regTime desc", 
		   countQuery = "select count(rb.id) "
		   			  + "from ReservBook rb join rb.book b "
		   			  + "where rb.book.isbn = b.isbn "
		   			  + "and rb.reserv.id = :reservId")
	Page<ReservListDto> findReservListDtoList(@Param("reservId") Long reservId, Pageable pageable);
	
	
	// 현재 로그인한 회원의 예약 도서 갯수 조회
//	@Query("select count(rb.id) from ReservBook rb join rb.book b where rb.book.isbn = b.isbn and rb.reserv.id = :reservId")
//	Long countReserv(@Param("reservId") Long reservId);
}
