package com.project.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.project.dto.InterBookDto;
import com.project.dto.InterListDto;
import com.project.entity.Book;
import com.project.entity.Inter;
import com.project.entity.InterBook;
import com.project.entity.Member;
import com.project.repository.BookRepository;
import com.project.repository.InterBookRepository;
import com.project.repository.InterRepository;
import com.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InterService {
	
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final InterRepository interRepository;
	private final InterBookRepository interBookRepository;
	
	// 관심 등록
	public Long addInter(InterBookDto interBookDto, String email) {
		
		// 도서조회
		Book book = bookRepository.findById(interBookDto.getBookIsbn()).orElseThrow(EntityNotFoundException::new);
		// 회원조희
		Member member = memberRepository.findByEmail(email);
		
		// 현재 로그인한 회원의 관심도서목록 엔티티 조회
		Inter inter = interRepository.findByMemberId(member.getId());
		if(inter == null) {
			inter = Inter.createInter(member);
			interRepository.save(inter);
		}
		
		// 현재 도서가 관심도서목록에 이미 들어가있는지 확인
//		InterBook savedInterBook = interBookRepository.findByInterIdAndBookIsbn(inter.getId(), book.getIsbn());
//		
//		if(savedInterBook != null) {
//			return savedInterBook.getId();
//		} else {
//			InterBook interBook = InterBook.createInterBook(inter, book);
//			interBookRepository.save(interBook);
//			return interBook.getId();
//		}
		
		InterBook interBook = InterBook.createInterBook(inter, book);
		interBookRepository.save(interBook);
		return interBook.getId();
		
	}
	
	// 관심도서목록 조회
	@Transactional(readOnly = true)
	public Page<InterListDto> getInterList(String email, Pageable pageable) {
		
		Member member = memberRepository.findByEmail(email);
		Inter inter = interRepository.findByMemberId(member.getId());
		Page<InterListDto> interBooks = interBookRepository.findInterListDtoList(inter.getId(), pageable);
		
		return interBooks;
	}
	
	// 검증
	@Transactional(readOnly = true)
	public boolean validateInterBook(Long interBookId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		InterBook interBook = interBookRepository.findById(interBookId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = interBook.getInter().getMember();
		
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		
		return true;
	}
	
	// 삭제
	public void deleteInterBook(Long interBookId) {
		InterBook interBook = interBookRepository.findById(interBookId).orElseThrow(EntityNotFoundException::new);
		interBookRepository.delete(interBook);
	}
	
	/**
	 * 회원관리 - 관심도서목록 보기
	 * 
	 * */
	@Transactional(readOnly = true)
	public Page<InterListDto> getAdminInterList(Long id, Pageable pageable) {
		
		Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		Inter inter = interRepository.findByMemberId(id);
		Page<InterListDto> interBooks = interBookRepository.findInterListDtoList(inter.getId(), pageable);
		
		return interBooks;
	}

}
