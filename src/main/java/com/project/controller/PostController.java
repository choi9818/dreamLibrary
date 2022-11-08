package com.project.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.CommentDto;
import com.project.dto.NoticeFormDto;
import com.project.dto.PostFormDto;
import com.project.dto.PostImgDto;
import com.project.dto.PostSearchDto;
import com.project.entity.Comment;
import com.project.entity.Post;
import com.project.repository.CommentRepository;
import com.project.repository.MemberRepository;
import com.project.repository.PostRepository;
import com.project.service.CommentService;
import com.project.service.NoticeService;
import com.project.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final CommentService commentService;
	private final CommentRepository commentRepository;
	
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
			model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
			return "post/postWrite";			
		}
		return "redirect:/post/list";
	}
	
	@GetMapping(value="/post/modify/{postId}")
	public String noticeModifyGet(@PathVariable("postId")Long postId, Model model) {
		try {
			PostFormDto postFormDto = postService.getPost(postId);
			if(postFormDto.getPostImgDtoList() == null) {
				System.out.println("postFormDto.getPostImgDtoList() null");
			}
			System.out.println("img######"+postFormDto.getPostImgDtoList());
			System.out.println("img###### "+ postFormDto.getPostImgDtoList().size());
			System.out.println("img######@@@"+postFormDto.getPostImgDtoList().get(0).getImgName());
			model.addAttribute("postFormDto",postFormDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMsg", "존재하지 않는 공지입니다.");
			model.addAttribute("postFormDto",new PostFormDto());
			return "post/postWrite";
		}
		return "post/postWrite";		
	}
	//게시물 수정
	@PostMapping(value="/post/modify/{postId}")//########### 
	public String noticeUpdate(@Valid PostFormDto postFormDto, BindingResult bindingResult,
			@RequestParam("postImgFile") List<MultipartFile> postImgFileList, Model model) throws Exception {
		if(bindingResult.hasErrors()) 
			return "post/postWrite";
		try {
			postService.updatePost(postFormDto, postImgFileList);//공지 수정 로직 호출
		} catch (IOException e) {
			model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
			return "post/postWrite";
		}		
		return "redirect:/post/list";
	}
	
	@GetMapping(value="/post/{postId}")
	public String postGet(@PathVariable("postId") Long postId, Model model, Principal principal) {
		boolean isAuthToEdit = false;
		System.out.println("postId@@"+postId);
		List<Comment> comments = commentRepository.getCommentsByPostId(postId); 
		System.out.println("comments=="+comments);
		List<CommentDto> commentDtos 
			= comments.stream().map(c->{return commentService.of(c);}).collect(Collectors.toList());
		CommentDto commentDto = new CommentDto();
		commentDto.setPostId(postId);		
		try {
			PostFormDto postFormDto = postService.getPost(postId);
			System.out.println("postId >>>>>>>> "+postId);			
			String postEmail =	postRepository.getPostMemberEmail(postId);
			postFormDto.setEmail(postEmail);
			commentDto.setEmail(postEmail);
			if(principal != null) {
				if(postFormDto.getEmail().equals(principal.getName()))
					isAuthToEdit = true;
				else isAuthToEdit = false;
			}
			System.out.println("postFormDto=="+postFormDto);
			System.out.println("isAuthToEdit >>>>>>>> "+isAuthToEdit);
			model.addAttribute("postFormDto",postFormDto);
			model.addAttribute("commentDtos",commentDtos);
			model.addAttribute("commentDto",commentDto);
			model.addAttribute("isAuthToEdit", isAuthToEdit);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMsg", "존재하지 않는 게시물입니다.");
			model.addAttribute("postFormDto",new PostFormDto());
			return "post/postRead";
		}
		
		return "post/postRead";
	}
	
	@PostMapping(value="/post/comment")
	public String postComment(Model model, Principal principal, @Valid CommentDto commentDto
			, BindingResult bindingResult) {
		boolean isAuthToEdit = false;
		//Long postId = commentRepository.getPostIdByCommnetId(commentDto.getCommentId());
		Long postId = commentDto.getPostId();
		System.out.println("commentDto%%%%%%"+commentDto);
		//commentDto.setPostId(postId);
		try {
			if(principal != null) {			
				System.out.println("isAuthToEdit $$$$$ "+isAuthToEdit);
				commentService.saveComment(commentDto);
				model.addAttribute("postId", postId);			
			}
			
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMsg", "존재하지 않는 게시물입니다.");
			return "redirect:/post/"+postId;
		}
		
		return "redirect:/post/"+postId;
	}
	
	@DeleteMapping(value="/post/comment/{commentId}")
	public @ResponseBody String deletePostComment(@PathVariable("commentId")Long commentId,
			@Valid CommentDto commentDto, Model model) {
		Long postId = commentDto.getPostId();
		model.addAttribute("postId", postId);	
		commentService.deletePostComment(commentId);		
		return "redirect:/post/"+postId;
	}
	
	@GetMapping(value= {"/post/list", "/post/list/{page}"})
	public String postList(PostSearchDto postSearchDto,
		//조회할 페이지 번호, 두 번째 한 번에 가지고 올 데이터 수
		@PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent()?page.get():0, 5);
		//조회 조건과 페이징 정보 파라미터로 넘겨서 Page<Notice> 객체를 반환
		Page<Post> list = postService.getPostPage(postSearchDto, pageable);
			model.addAttribute("list",list);//조회한 상품 데이터 및 페이징 정보 뷰에 전달
			model.addAttribute("postSearchDto", postSearchDto);//페이지 전환 시 기존 검색 조건 유지한체
			model.addAttribute("maxPage", 5);//페이지 하단에 보여줄 최대 개수
		return "post/postList";
	}
}
