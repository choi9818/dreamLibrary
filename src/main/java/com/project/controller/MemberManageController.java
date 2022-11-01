package com.project.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.dto.MemberFormDto;
import com.project.dto.SearchDto;
import com.project.service.MemberManageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberManageController {
	private final MemberManageService memberManageService;
	
//	@GetMapping(value = "/admin/memberManage/{page}")
//	public String adminMemberManage(@PathVariable("page") Integer page,  Model model) {	
//			MemberFormDto member = member
//			System.out.println("회원확인 >>>>>>>>>>" +member);
//			model.addAttribute("member", member);
//		return "admin/memberManage"; 
//	}
	
	@GetMapping(value = {"/admin/memberManage","/admin/memberManage/{page}"})
	public String adminMemberManage(SearchDto searchDto
			, @PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3);
		Page<MemberFormDto> memberList = memberManageService.getManagePage(pageable);
		model.addAttribute("member", memberList);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("maxPage",5);
		return "admin/memberManage"; 
	}
	
}
