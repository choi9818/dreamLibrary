package com.project.service;

import javax.persistence.EntityNotFoundException;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.BookImg;
import com.project.repository.BookImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookImgService {
	
	@Value("${bookImgLocation}")
	private String bookImgLocation;
	
	private final BookImgRepository bookImgRepository;
	
	private final FileService fileService;
	
	/**
	 * 저장
	 */
	public void saveBookImg(BookImg bookImg, MultipartFile bookImgFile) throws Exception {
		String oriImgName = bookImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		// 파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(bookImgLocation, oriImgName, bookImgFile.getBytes());
			imgUrl = "/images/book/" + imgName;
		}
		/*
		 * 1. imgName
		 * uploadFile(이미지를 저장할 경로, 파일이름, 파일의 바이트배열)
		 * 	-> 로컬에 저장된 파일 이름을 imgName 변수에 저장
		 * 
		 * 2. imgUrl : 저장한 상품 이미지를 불러올 경로 설정
		 * - 외부 리소스를 불러오는 urlPattern으로 "/images/**"를 설정해두었음.
		 * - uploadPath 프로퍼티 경로인 "C:/Project/" 아래 images/uploadedBookImg 폴더에 이미지를 저장
		 * --> 상품이미지를 불러오는 경로 : /images/uploadedBookImg/ 을 붙여줌
		 * 
		 * */
		
		// 도서이미지 정보 저장
		bookImg.updateBookImg(oriImgName, imgName, imgUrl);
		bookImgRepository.save(bookImg);
	}
	
	/**
	 * 수정
	 * */
	public void updateBookImg(Long bookImgId, MultipartFile bookImgFile) throws Exception {
		if(!bookImgFile.isEmpty()) {
			BookImg savedBookImg = bookImgRepository.findById(bookImgId).orElseThrow(EntityNotFoundException::new);
			
			// 기존 이미지 파일 삭제
			if(!StringUtils.isEmpty(savedBookImg.getImgName())) {
				fileService.deleteFile(bookImgLocation + "/" + savedBookImg.getImgName());
			}
			
			String oriImgName = bookImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(bookImgLocation, oriImgName, bookImgFile.getBytes());
			String imgUrl = "/images/book/" + imgName;
			savedBookImg.updateBookImg(oriImgName, imgName, imgUrl);
		}
	}

}
