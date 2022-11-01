package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.dto.InterListDto;
import com.project.entity.InterBook;

public interface InterBookRepository extends JpaRepository<InterBook, Long> {
	
	// 관심등록할 도서 조회, 저장
	InterBook findByInterIdAndBookIsbn(Long interId, String bookIsbn);

	// 현재 로그인한 회원의 관심도서목록 조회
	@Query(value = "select new com.project.dto.InterListDto(ib.id, b.isbn, b.title, b.author, b.publisher, i.imgUrl) "
				 + "from InterBook ib, BookImg i join ib.book b "
				 + "where i.book.isbn = ib.book.isbn "
				 + "and ib.inter.id = :interId "
				 + "order by ib.regTime desc",
		   countQuery = "select count(ib.id) "
		   			  + "from InterBook ib join ib.book b "
		   			  + "where ib.book.isbn = b.isbn "
		   			  + "and ib.inter.id = :interId")
	Page<InterListDto> findInterListDtoList(@Param("interId") Long interId, Pageable pageable);
	
	
	// 현재 로그인한 회원의 관심도서 갯수 조회
//	@Query("select count(ib) from InterBook ib join ib.book b where ib.book.isbn = b.isbn and ib.inter.id = :interId")
//	Long countInter(@Param("interId") Long interId);
}
