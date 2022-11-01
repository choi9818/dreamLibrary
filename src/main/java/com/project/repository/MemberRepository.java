package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long>
	, QuerydslPredicateExecutor<Member>, PageRepositoryCustom{
	Member findByEmail(String email);//중복 회원 검사 위한 쿼리문	
	
	//회원관리 페이지에 리스트 뽑아내는 쿼리
	@Query(value="Select m From Member m Order by m.id"
		 , countQuery = "Select count(*) from Member m" )
	Page<Member> getMembers(Pageable pageable);
	
//	@Query(value="Select  com.project.dto.MemberFormDto(m.id, m.name, m.email, m.role) From Member m Order by m.mberm"
//			 , countQuery = "Select count(*) from Member m" , nativeQuery = true)
//	List<MemberFormDto> getMembers2(Pageable pageable);
	//@Query(value="Select  m From Member m Order by m.id")
	//List<Member> getMembers2();
}
