package com.project.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService {
	private final MemberRepository memberRepository;
	
	//로그인된 사용자 불러오는 메소드
	@Transactional(readOnly = true)
	public Optional<Member> getMember(String email) {
		Member member = memberRepository.findByEmail(email);
		Optional<Member> loginMember = memberRepository.findById(member.getId());
		return loginMember;
	}
		
	public Long updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {//수정한 정보 저장
		Member member = memberRepository.findById(memberFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		member.updateMember(memberFormDto, passwordEncoder);
		return member.getId();
	}
}
