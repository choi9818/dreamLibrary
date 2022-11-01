package com.project.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.project.dto.MemberFormDto;
import com.project.dto.SearchDto;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberManageService {
	private final MemberRepository memberRepository;
	
	//회원관리 페이징/검색시 사용
//	@Transactional(readOnly = true)
//	public Page<Member> getManagePage(SearchDto searchDto, Pageable pageable){
//		return memberRepository.getManagePage(searchDto, pageable);
//	}
	
	public Page<MemberFormDto> getManagePage(Pageable pageable){
		Page<Member>  members = memberRepository.getMembers(pageable);
		Page<MemberFormDto> dtoPage = members.map(new Function<Member, MemberFormDto>() {
		    public MemberFormDto apply(Member entity) {	

		        return MemberFormDto.of(entity);
		    }
		});	
		
		return dtoPage;
		
		
		
	}
	
	//가입한 회원 리스트 조회
	@Transactional(readOnly = true)
	public MemberFormDto getMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
		MemberFormDto memberFormDto = MemberFormDto.of(member);
		return memberFormDto;
	}




}
