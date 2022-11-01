package com.project.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.project.dto.PostFormDto;
import com.project.dto.PostImgDto;
import com.project.dto.PostSearchDto;
import com.project.entity.Member;
import com.project.entity.Post;
import com.project.entity.PostImg;
import com.project.repository.MemberRepository;
import com.project.repository.PostImgRepository;
import com.project.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final PostImgService postImgService;
	private final PostImgRepository postImgRepository;
	private final MemberRepository memberRepository;
	
	//게시물 저장
	public Long savePost(PostFormDto postFormDto, MultipartFile multipartFile) throws Exception {
		Post post = postFormDto.createPost();
		postRepository.save(post);
		
		PostImg postImg = new PostImg();
		postImg.setPost(post);
		postImgService.savePostImg(postImg, multipartFile);
		return post.getId();
	}
	//게시물 불러옴
	@Transactional(readOnly = true)
	public PostFormDto getPost(Long postId) {
		List<PostImg> postImgList = postImgRepository.findByPostIdOrderByIdAsc(postId);
		List<PostImgDto> postImgDtoList = new ArrayList<>();
		for(PostImg postImg:postImgList) {
			PostImgDto postImgDto = PostImgDto.of(postImg);
			postImgDtoList.add(postImgDto);
		}	
		Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
		PostFormDto postFormDto = PostFormDto.of(post);
		postFormDto.setPostImgDtoList(postImgDtoList);
		return postFormDto;		
	}
	
//	@Transactional(readOnly = true)
//	public PostFormDto validatePostWriter(Long postId, String email) {
//		//boolean isAuthToEdit = false;
//		Member postMember = memberRepository.findByEmail(email);//로그인한 회원 조회
//		Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
//		Member savedMember = post.getMember();
//		//String savedMember = post.getCreatedBy();
//		//getEmail().getName();//게시글 저정한 회원 조회
//		if(!StringUtils.equals(postMember.getEmail(),savedMember.getEmail())) {
//			//로그인 회원과 포스트 저장한 회원 다르면 false
//		}
//		List<PostImg> postImgList = postImgRepository.findByPostIdOrderByIdAsc(postId);
//		List<PostImgDto> postImgDtoList = new ArrayList<>();
//		for(PostImg postImg:postImgList) {
//			PostImgDto postImgDto = PostImgDto.of(postImg);
//			postImgDtoList.add(postImgDto);
//		}
//		Post postEx = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
//		PostFormDto postFormDto = PostFormDto.of(postEx);
//		postFormDto.setPostImgDtoList(postImgDtoList);
//		return postFormDto;		
//	}
//	
	@Transactional(readOnly = true)
	public Page<Post> getPostPage(PostSearchDto postSearchDto, Pageable Pageable) {
		return postRepository.getPostPage(postSearchDto, Pageable);
		
	}
	
}
