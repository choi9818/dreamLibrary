package com.project.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping(value="/new")//회원가입 페이지로 이동
	public String register(Model model) {
		model.addAttribute("memberFormDto",new MemberFormDto());
		return "member/member";
	}
	
	@PostMapping(value = "/new")//회원가입 후 메인페이지로 이동
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
		//@Valid검증하려는 객체 앞에 선언, 파라미터로 bindingResult 객체 추가, 검사ㅎ 후 결과 bindingResult에 담음
        if(bindingResult.hasErrors()){//에러 있으면 회원 가입 페이지로 돌아간다.
            return "member/member";
        }

        try {
           Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());//중복 회원 가입 예외가 발생하면 에러 메시지를 뷰로 전달
            return "member/member";
        }

        return "redirect:/";
    }
	
	@GetMapping(value="/login")//로그인 페이지로 이동
	public String login() {
		return "/member/login";
	}
	
	@GetMapping(value="/login/error")//로그인 에러시
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
		return "/member/login";
	}

}
