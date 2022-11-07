package com.project.controller;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.service.UpdateMemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {
	
	private final UpdateMemberService updateMemberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping(value="/mypage/member")//회원수정 페이지로 이동
	public String mypageMember(Model model, Principal principal) {
		Optional<Member> loginMember = updateMemberService.getMember(principal.getName());
		model.addAttribute("loginMember", loginMember);	
		return "/member/updateMember";
	}
	
	// 회원수정 
	@PostMapping(value="/mypage/member") 
	public String memberUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult) {
		 if(bindingResult.hasErrors()){//에러 있으면 회원 가입 페이지로 돌아간다.
			 return "/mypage/member";
	    }
		updateMemberService.updateMember(memberFormDto, passwordEncoder);
		return "redirect:/mypage/member";		
	}

}
