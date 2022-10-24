package com.project.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.project.entity.NoticeImg;
import com.project.repository.NoticeImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeImgService {//이미지 정보 저장하는 NoticeImgService 클래스
	@Value("${imgLocation}")//프로퍼티스에 imgLocation 값 불러와서 imgLocation 변수에 넣음
	private String imgLocation;
	
	private final NoticeImgRepository noticeImgRepository;
	
	private final FileService fileService;
	
	public void saveNoticeImg(NoticeImg noticeImg, MultipartFile noticeImgFile) throws Exception{
		String oriImgName = noticeImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		//파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(imgLocation, oriImgName,
					  noticeImgFile.getBytes());
			imgUrl ="/images/notice/"+imgName; //WebMvcConfig 설정경로+프로퍼티스 uploadPath 아래 notice 폴더
		}		
		//공지 이미지 정보 저장
		//imgName 실제 로컬에 저장된 상품 이미지 파일 이름, oriImgName 업로드 했던 상품 이미지의 원래 이름
		//imgUrl 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
		noticeImg.updatePostImg(oriImgName, imgName, imgUrl);
		noticeImgRepository.save(noticeImg);
	}
	//상품 이미지 수정
	public void updateNoticeImg(Long noticeImgId, MultipartFile noticeImgFile) throws Exception{
		if(!noticeImgFile.isEmpty()) {//공지 이미지를 수정한 경우 공지 이미지를 업데이터
			NoticeImg saveNoticeImg = noticeImgRepository.findById(noticeImgId)//이미지 아이디 이용해서 기존 엔티티 조회
					.orElseThrow(EntityNotFoundException::new);
			//기존 이미지 파일 삭제
			if(!StringUtils.isEmpty(saveNoticeImg.getImgName())) {//기존에 등록된 공지 이미지 파일이 있을 경우 해당 파일 삭제
				fileService.deleteFile(imgLocation+"/"+ saveNoticeImg.getImgName());
			}
			String oriImgName = noticeImgFile.getOriginalFilename();
			//업데이트한 공지 이미지 파일을 업로드
			String imgName = fileService.uploadFile(imgLocation, oriImgName, noticeImgFile.getBytes());
			String imgUrl = "/images/notice/"+imgName; 
			//변경된 공지 이미지 정보 세팅
			saveNoticeImg.updatePostImg(oriImgName, imgName, imgUrl);	
		}
	}
}
