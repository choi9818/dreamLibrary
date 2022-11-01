package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.SearchDto;
import com.project.entity.Notice;

public interface PageRepositoryCustom {
	//공지사항
	Page<Notice> getNoticePage(SearchDto searchDto, Pageable pageable);
	//공지 조회 조건을 담고 있는 searchDto 객체와 페이징 정보를 담고 있는 pageable 객체를
	//파라미터로 받는 getNoticePage메소드를 정의 반환 데이터로 Page<Notice> 객체를 반환
	
	//회원관리
//	Page<Member> getManagePage(SearchDto searchDto, Pageable pageable);

	
}
