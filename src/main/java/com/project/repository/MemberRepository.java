package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Member findByEmail(String email);//중복 회원 검사 위한 쿼리문
	
	Optional<Member> findById(Long memberId);
	
}
