package com.project.controller;

import java.security.Principal;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.PostFormDto;
import com.project.dto.PostSearchDto;
import com.project.entity.Post;
import com.project.service.NoticeService;
import com.project.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	
	//게시글 쓰기
	@GetMapping(value="/post/new")
	public String postWrite(Model model) {
		model.addAttribute("postFormDto", new PostFormDto());
		return "post/postWrite";
	}
	//게시물 등록 
	@PostMapping(value="/post/new")
	public String PostNew(@Valid PostFormDto postFormDto, BindingResult bindingResult,
			Model model, @RequestParam("postImgFile") MultipartFile multipartFile) {
		if(bindingResult.hasErrors()) {
			return "post/postWrite";
		}
		try {
			postService.savePost(postFormDto, multipartFile);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "공지 등록 중 에러가 발생하였습니다.");
			return "post/postWrite";			
		}
		return "redirect:/post/list";
	}
	
	@GetMapping(value="/post/{postId}")
	public String postGet(@PathVariable("postId") Long postId, @PathVariable("email") String email, 
			Model model, Principal principal) {
		boolean isAuthToEdit = false;
		try {
			PostFormDto postFormDto = postService.getPost(postId);
			System.out.println("postId >>>>>>>> "+postId);
			model.addAttribute("postFormDto",postFormDto);
			model.addAttribute("isAuthToEdit", isAuthToEdit);
			if(postFormDto.getEmail().equals(principal.getName()))
				isAuthToEdit = true;
			else isAuthToEdit = false;
			System.out.println("isAuthToEdit >>>>>>>> "+isAuthToEdit);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMsg", "존재하지 않는 게시물입니다.");
			model.addAttribute("postFormDto",new PostFormDto());
			return "post/postRead";
		}
		return "post/postRead";
	}
	//게시물 수정
	
	
	
	@GetMapping(value= {"/post/list", "/post/list/{page}"})
	public String postList(PostSearchDto postSearchDto,
		//조회할 페이지 번호, 두 번째 한 번에 가지고 올 데이터 수
		@PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent()?page.get():0, 3);
		//조회 조건과 페이징 정보 파라미터로 넘겨서 Page<Notice> 객체를 반환
		Page<Post> list = postService.getPostPage(postSearchDto, pageable);
			model.addAttribute("list",list);//조회한 상품 데이터 및 페이징 정보 뷰에 전달
			model.addAttribute("postSearchDto", postSearchDto);//페이지 전환 시 기존 검색 조건 유지한체
			model.addAttribute("maxPage", 5);//페이지 하단에 보여줄 최대 개수
		return "post/postList";
	}
}
