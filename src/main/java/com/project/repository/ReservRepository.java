package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Reserv;

public interface ReservRepository extends JpaRepository<Reserv, Long> {
	
	// 현재 로그인한 회원의 예약도서목록 찾기
	Reserv findByMemberId(Long memberId);

}
