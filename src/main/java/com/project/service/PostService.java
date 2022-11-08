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
		System.out.println("postFormDto>>>>"+postFormDto);
		System.out.println("email===="+postFormDto.getEmail());
		System.out.println("post>>"+post);
		Member member = memberRepository.findByEmail(postFormDto.getEmail());
		System.out.println("member>>"+member);
		post.setMember(member);
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
	
	@Transactional(readOnly = true)
	public Page<Post> getPostPage(PostSearchDto postSearchDto, Pageable Pageable) {
		return postRepository.getPostPage(postSearchDto, Pageable);		
	}
	
	public Long updatePost(PostFormDto postFormDto, List<MultipartFile> postImgFileList) throws Exception {
		Post post = postRepository.findById(postFormDto.getId())
				.orElseThrow(EntityNotFoundException::new);
		post.updatePost(postFormDto);
		List<Long> postImgIds = postFormDto.getPostImgIds();
		for(int i = 0; i < postImgFileList.size();i++) {
			//공지 이미지 업뎃 위해서 updateNoticeImg() 메소드에 공지 이미지 아이디와 공지 이미지 파일 정보를 파라미터로 전달
			postImgService.updatePostImg(postImgIds.get(i), postImgFileList.get(i));
		}
		return post.getId();
	}
}
