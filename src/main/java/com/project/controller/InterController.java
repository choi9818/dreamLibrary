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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.dto.InterBookDto;
import com.project.dto.InterListDto;
import com.project.service.InterService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InterController {
	
	private final InterService interService;
	
	// 관심도서등록
	@PostMapping(value = "/inter")
	public @ResponseBody ResponseEntity reserv(@RequestBody @Valid InterBookDto interBookDto,
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
		Long interBookId;
		
		try {
			interBookId = interService.addInter(interBookDto, email);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(interBookId, HttpStatus.OK);
	}
	
	// 관심도서목록 조회
	@GetMapping(value = {"/inter", "/inter/{page}"})
	public String interList(@PathVariable("page") Optional<Integer> page,
							Principal principal,
							Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		Page<InterListDto> interBooks = interService.getInterList(principal.getName(), pageable);
		
		model.addAttribute("interBooks", interBooks);
		model.addAttribute("page", pageable.getPageNumber());
		model.addAttribute("maxPage", 5);
		
		return "inter/interList";
	}
	
	// 삭제
	@DeleteMapping(value = "/inter/{interBookId}")
	public @ResponseBody ResponseEntity deleteInterBook(@PathVariable("interBookId") Long interBookId,
														Principal principal) {
		
		if(!interService.validateInterBook(interBookId, principal.getName())) {
			return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		interService.deleteInterBook(interBookId);
		
		return new ResponseEntity<Long>(interBookId, HttpStatus.OK);
	}
	
	/**
	 * 회원관리 - 관심도서목록 보기
	 * 
	 * */
	@GetMapping(value = {"/admin/member/inter/{memberId}", "/admin/member/inter/{memberId}/{page}"})
	public String interManage(@PathVariable("memberId") Long memberId,
							  @PathVariable("page") Optional<Integer> page,
							  Model model) {
		try {
			
			Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
			Page<InterListDto> interBooks = interService.getAdminInterList(memberId, pageable);
			
			model.addAttribute("interBooks", interBooks);
			model.addAttribute("page", pageable.getPageNumber());
			model.addAttribute("maxPage", 5);
			
		} catch(Exception e) {
			return "inter/error";
		}
		
		return "inter/adminInterList";
	}

}
