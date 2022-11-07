package com.project.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.project.entity.NoticeImg;
import com.project.entity.PostImg;
import com.project.repository.PostImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostImgService {
	@Value("${postImgLocation}")//프로퍼티스에 imgLocation 값 불러와서 imgLocation 변수에 넣음
	private String postImgLocation;	
	private final PostImgRepository postImgRepository;
	private final FileService fileService;
	
	public void savePostImg(PostImg postImg, MultipartFile multipartFile) throws Exception{
		String oriImgName = multipartFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		//파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(postImgLocation, oriImgName,
					multipartFile.getBytes());
			imgUrl ="/images/post/"+imgName; //WebMvcConfig 설정경로+프로퍼티스 uploadPath 아래 notice 폴더
		}		
		//게시글 이미지 정보 저장
		//imgName 실제 로컬에 저장된 상품 이미지 파일 이름, oriImgName 업로드 했던 상품 이미지의 원래 이름
		//imgUrl 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
		postImg.updatePostImg(oriImgName, imgName, imgUrl);
		postImgRepository.save(postImg);
	}
	public void updatePostImg(Long postImgId, MultipartFile postImgFile) throws Exception {
		if(!postImgFile.isEmpty()) {
			PostImg savePostImg = postImgRepository.findById(postImgId)
					.orElseThrow(EntityNotFoundException::new);
			//기존 이미지 파일 삭제
			if(!StringUtils.isEmpty(savePostImg.getImgName())) {//기존에 등록된 이미지 파일이 있을 경우 해당 파일 삭제
				fileService.deleteFile(postImgLocation+"/"+ savePostImg.getImgName());
			}
			String oriImgName = postImgFile.getOriginalFilename();
			//업데이트한 이미지 파일을 업로드
			String imgName = fileService.uploadFile(postImgLocation, oriImgName, postImgFile.getBytes());
			String imgUrl = "/images/notice/"+imgName; 
			//변경된 이미지 정보 세팅
			savePostImg.updatePostImg(oriImgName, imgName, imgUrl);	
		}
	}
}
