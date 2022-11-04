package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.MemberSearchDto;
import com.project.entity.Member;

public interface MemberRepositoryCustom {
	
	// 회원관리 페이지
	Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable);

}
