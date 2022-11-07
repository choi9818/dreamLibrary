package com.project.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.dto.NoticeFormDto;
import com.project.dto.SearchDto;
import com.project.entity.Notice;
import com.project.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {
	private final NoticeService noticeService;
	
	//공지 등록 페이지 접근 url
	@GetMapping(value = "/notice/new")
	public String noticeForm(Model model) {//noticeFormDto를 model 객체에 담아 뷰로 전달
		model.addAttribute("noticeFormDto", new NoticeFormDto());
		return "notice/noticeWrite";
	}
	
	//관리자-등록
	@PostMapping(value="/notice/new")
	public String noticeNew(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult,
			Model model, @RequestParam("noticeImgFile") MultipartFile multipartFile) {
		if(bindingResult.hasErrors()) {//공지 등록시 필수 값이 없다면 다시 공지 등록 페이지로 전환
			return "notice/noticeWrite";
		}
		try {//공지 저장 로직 호출, 매개 변수로 공지 내용과 공지 이미지를 넘겨줌
			noticeService.saveNotice(noticeFormDto, multipartFile);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "공지 등록 중 에러가 발생하였습니다.");
			return "notice/noticeWrite";				
		}
		return "redirect:/notice/list";//상품 정상적으로 등록 되었다면 다시 리스트로 이동
	}
	//관리자-수정 페이지 불러오기
	@GetMapping(value="/notice/{noticeId}")
	public String noticeGet(@PathVariable("noticeId") Long noticeId, Model model) {
		try {
			NoticeFormDto noticeFormDto = noticeService.getNotice(noticeId);
			model.addAttribute("noticeFormDto",noticeFormDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMsg", "존재하지 않는 공지입니다.");
			model.addAttribute("noticeFormDto",new NoticeFormDto());
			return "notice/noticeWrite";
		}
		return "notice/noticeWrite";
		
	}
	//관리자-수정 저장
	@PostMapping(value="/notice/{noticeId}")
	public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult,
			@RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList, Model model) throws Exception {
		if(bindingResult.hasErrors()) 
			return "notice/noticeWrite";
		try {
			noticeService.updateNotice(noticeFormDto, noticeImgFileList);//공지 수정 로직 호출
		} catch (IOException e) {
			model.addAttribute("errorMessage", "공지 등록 중 에러가 발생하였습니다.");
			return "notice/noticeWrite";
		}		
		return "redirect:/notice/list";
	}
	
	//공지 삭제 
//	@DeleteMapping(value = "/notice/{noticeId}")
//	public ResponseEntity<Long> deleteNotice(@PathVariable("noticeId") Long noticeId, Long noticeImgId) {
//		noticeService.deleteNotice(noticeId, noticeImgId);
//		return new ResponseEntity<Long>(noticeId, HttpStatus.OK);
//	}
	
	@DeleteMapping(value = "/notice/{noticeId}")
	public @ResponseBody ResponseEntity<Long> deleteNotice(@PathVariable("noticeId") Long noticeId) {
		noticeService.deleteNotide(noticeId);
		System.out.println("noticeId"+noticeId);
		return new ResponseEntity<Long>(noticeId, HttpStatus.OK);
	}
	
	//관리자-공지 리스트 조회
	@GetMapping(value= {"/notice/list", "/notice/list/{page}"})
	public String noticeList(SearchDto searchDto,
		//조회할 페이지 번호, 두 번째 한 번에 가지고 올 데이터 수
		@PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent()?page.get():0, 5);
		//조회 조건과 페이징 정보 파라미터로 넘겨서 Page<Notice> 객체를 반환
		Page<Notice> list = noticeService.getNoticePage(searchDto, pageable);
			model.addAttribute("list",list);//조회한 상품 데이터 및 페이징 정보 뷰에 전달
			model.addAttribute("searchDto", searchDto);//페이지 전환 시 기존 검색 조건 유지한체
			model.addAttribute("maxPage", 5);//페이지 하단에 보여줄 최대 개수
		return "notice/noticeList";
	}
	
	//회원-공지 리스트 조회 페이지
	@GetMapping(value= {"/notice/read", "/notice/read/{page}"})
	public String noticeRead(SearchDto searchDto,
		//조회할 페이지 번호, 두 번째 한 번에 가지고 올 데이터 수
		@PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent()?page.get():0, 5);
		//조회 조건과 페이징 정보 파라미터로 넘겨서 Page<Notice> 객체를 반환
		Page<Notice> readList = noticeService.getNoticePage(searchDto, pageable);
			model.addAttribute("readList",readList);//조회한 상품 데이터 및 페이징 정보 뷰에 전달
			model.addAttribute("readSearchDto", searchDto);//페이지 전환 시 기존 검색 조건 유지한체
			model.addAttribute("maxPage", 5);//페이지 하단에 보여줄 최대 개수
		return "notice/noticeReadList";
	}
	
	//회원-공지 상세 조회 페이지
	@GetMapping(value="/notice/view/{noticeId}")
	public String noticeGetRead(@PathVariable("noticeId") Long noticeId, Model model) {
		NoticeFormDto noticeFormDto = noticeService.getNotice(noticeId);
		model.addAttribute("noticeFormDto",noticeFormDto);		
		return "notice/noticeRead";		
	}
	
}
