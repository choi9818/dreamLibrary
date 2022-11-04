package com.project.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.dto.ReservBookDto;
import com.project.dto.ReservListDto;
import com.project.service.ReservService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservController {
	
	private final ReservService reservService;
	
	// 예약도서 신청
	@PostMapping(value = "/reserv")
	public @ResponseBody ResponseEntity reserv(@RequestBody @Valid ReservBookDto reservBookDto,
											   BindingResult bindingResult,
											   Principal principal) {
		
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		String email = principal.getName();
		Long reservBookId;
		
		try {
			reservBookId = reservService.reserv(reservBookDto, email);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(reservBookId, HttpStatus.OK);
	}
	
	// 예약도서 조회
	@GetMapping(value = {"/reserv", "/reserv/{page}"})
	public String reservList(@PathVariable("page") Optional<Integer> page,
							 Principal principal,
							 Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		Page<ReservListDto> reservBooks = reservService.getReservList(principal.getName(), pageable);
		
		model.addAttribute("reservBooks", reservBooks);
		model.addAttribute("page", pageable.getPageNumber());
		model.addAttribute("maxPage", 5);
		
		return "reserv/reservList";
	}
	
	// 예약 취소
	@PostMapping(value = "/reserv/{reservBookId}")
	public @ResponseBody ResponseEntity deleteReservBook(@PathVariable("reservBookId") Long reservBookId,
														 Principal principal) {
		
		if(!reservService.validateReservBook(reservBookId, principal.getName())) {
			return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		reservService.deleteReservBook(reservBookId);
		
		return new ResponseEntity<Long>(reservBookId, HttpStatus.OK);
	}
	
	/**
	 * 회원관리 - 예약도서목록 보기
	 * 
	 * */
	@GetMapping(value = {"/admin/member/reserv/{memberId}", "/admin/member/reserv/{memberId}/{page}"})
	public String reservManage(@PathVariable("memberId") Long memberId,
							   @PathVariable("page") Optional<Integer> page,
							   Model model) {
		try {
			
			Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
			Page<ReservListDto> reservBooks = reservService.getAdminReservList(memberId, pageable);
			
			model.addAttribute("reservBooks", reservBooks);
			model.addAttribute("page", pageable.getPageNumber());
			model.addAttribute("maxPage", 5);
			
		} catch(Exception e) {
			return "reserv/error";
		}
		
		
		return "reserv/adminReservList";
	}
	

}
