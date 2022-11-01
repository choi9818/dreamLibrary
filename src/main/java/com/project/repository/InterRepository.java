package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Inter;

public interface InterRepository extends JpaRepository<Inter, Long> {
	
	// 현재 로그인한 회원의 관심도서목록 조회
	Inter findByMemberId(Long memberId);
	

}
