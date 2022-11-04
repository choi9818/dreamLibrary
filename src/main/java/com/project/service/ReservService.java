package com.project.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.project.dto.ReservBookDto;
import com.project.dto.ReservListDto;
import com.project.entity.Book;
import com.project.entity.Member;
import com.project.entity.Reserv;
import com.project.entity.ReservBook;
import com.project.exception.DuplicateReservException;
import com.project.repository.BookRepository;
import com.project.repository.MemberRepository;
import com.project.repository.ReservBookRepository;
import com.project.repository.ReservRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservService {
	
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final ReservRepository reservRepository;
	private final ReservBookRepository reservBookRepository;
	
	// 예약 신청
	public Long reserv(ReservBookDto reservBookDto, String email) {
		
		// 상품조회
		Book book = bookRepository.findById(reservBookDto.getBookIsbn()).orElseThrow(EntityNotFoundException::new);
		// 회원조회
		Member member = memberRepository.findByEmail(email);
		
		// 현재 로그인한 회원의 예약목록 엔티티 조회
		Reserv reserv = reservRepository.findByMemberId(member.getId());
		if(reserv == null) {
			reserv = Reserv.createReserv(member);
			reservRepository.save(reserv);
		}
		
		// 현재 도서가 예약목록에 이미 들어가있는지 조회
		ReservBook savedReservBook = reservBookRepository.findByReservIdAndBookIsbn(reserv.getId(), book.getIsbn());
		
		if(savedReservBook != null) {
			throw new DuplicateReservException("이미 예약신청된 도서입니다.");
		} else {
			ReservBook reservBook = ReservBook.createReservBook(reserv, book);
			reservBookRepository.save(reservBook);
			return reservBook.getId();
		}
	}
	
	// 예약목록	
	@Transactional(readOnly = true)
	public Page<ReservListDto> getReservList(String email, Pageable pageable) {
		
		Member member = memberRepository.findByEmail(email);
		Reserv reserv = reservRepository.findByMemberId(member.getId());
		Page<ReservListDto> reservBooks = reservBookRepository.findReservListDtoList(reserv.getId(), pageable);
		
		return reservBooks;
	}
	
	// 권한확인
	@Transactional(readOnly = true)
	public boolean validateReservBook(Long reservBookId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		ReservBook reservBook = reservBookRepository.findById(reservBookId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = reservBook.getReserv().getMember();
		
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		return true;
	}
	
	// 예약 취소
	public void deleteReservBook(Long reservBookId) {
		ReservBook reservBook = reservBookRepository.findById(reservBookId).orElseThrow(EntityNotFoundException::new);
		reservBook.cancel();
		reservBookRepository.delete(reservBook);
	}
	
	/**
	 * 회원관리 - 예약도서목록 보기
	 * 
	 * */
	@Transactional(readOnly = true)
	public Page<ReservListDto> getAdminReservList(Long id, Pageable pageable) {
		
		Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		Reserv reserv = reservRepository.findByMemberId(id);
		Page<ReservListDto> reservBooks = reservBookRepository.findReservListDtoList(reserv.getId(), pageable);
		
		return reservBooks;
	}
}
