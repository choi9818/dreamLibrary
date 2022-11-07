package com.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.NoticeFormDto;
import com.project.dto.NoticeImgDto;
import com.project.dto.SearchDto;
import com.project.entity.Notice;
import com.project.entity.NoticeImg;
import com.project.repository.NoticeImgRepository;
import com.project.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {//공지 등록하는 NoticeService 클래스
	private final NoticeRepository noticeRepository;
	private final NoticeImgService noticeImgService;
	private final NoticeImgRepository noticeImgRepository;
	
	public Long saveNotice(NoticeFormDto noticeFormDto, MultipartFile multipartFile) 
			throws Exception{
		//공지 등록
		Notice notice = noticeFormDto.createNotice();//공지 등록 폼으로부터 입력 받은 데이터 이용해서 notice 객체 생성
		noticeRepository.save(notice);//공지 데이터 저장
		
		//이미지 등록
		NoticeImg noticeImg = new NoticeImg();
		noticeImg.setNotice(notice);
		noticeImgService.saveNoticeImg(noticeImg, multipartFile);
		return notice.getId();
	}
	//등록된 공지 불러오는 메소드
	@Transactional(readOnly = true)
	public NoticeFormDto getNotice(Long noticeId) {
		List<NoticeImg> noticeImgList = noticeImgRepository.findByNoticeIdOrderByIdAsc(noticeId);//공지 이미지 조회
		List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();
		//조회한 noticeImg 엔티티를 noticeImgDto 객체로 만들어서 리스트에 추가
		for(NoticeImg noticeImg:noticeImgList) {
			NoticeImgDto noticeImgDto = NoticeImgDto.of(noticeImg);
			noticeImgDtoList.add(noticeImgDto);
		}		
		//공지 id를 통해 공지 조회 존재하지 않을 때 EntityNotFoundException 발생
		Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
		NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
		noticeFormDto.setNoticeImgDtoList(noticeImgDtoList);
		return noticeFormDto;
	}

	//공지 조회 조건과 페이지 정보를 파라미터로 받아서 공지 데이터를 조회하는 메소드 추가,
	//데이터 수정 잃어나지 않음으로 최적화 위해 readOnly = true 설정
	@Transactional(readOnly = true)
	public Page<Notice> getNoticePage(SearchDto searchDto, Pageable pageable){
		return noticeRepository.getNoticePage(searchDto, pageable);
	}
	
	public Long updateNotice(NoticeFormDto noticeFormDto, List<MultipartFile> noticeImgFileList) throws Exception {
		//공지 수정
		Notice notice = noticeRepository.findById(noticeFormDto.getId())//화면으로부터 전달받은 공지 아이디 이용하여 엔티티 조회
				.orElseThrow(EntityNotFoundException::new);
		notice.updateNotice(noticeFormDto);//공지 등록 화면으로 부터 전달 받은 noticeFormDto를 통해 상품 엔티티를 업뎃
		List<Long> noticeImgIds = noticeFormDto.getNoticeImgIds();//공지 이미지 아이디 리스트 조회
		//이미지 등록
		for(int i = 0; i < noticeImgFileList.size();i++) {
			//공지 이미지 업뎃 위해서 updateNoticeImg() 메소드에 공지 이미지 아이디와 공지 이미지 파일 정보를 파라미터로 전달
			noticeImgService.updateNoticeImg(noticeImgIds.get(i), noticeImgFileList.get(i));
		}
		return notice.getId();
	}	
	
	public void deleteNotide(Long noticeId) {
		Notice notice = noticeRepository.getNoticeIdAndNoticeImgId(noticeId);
		noticeRepository.delete(notice);
	}
	
//	public void deleteNotice(Long noticeId, Long noticeImgId) {
//		List<NoticeImg> notice = noticeImgRepository.findByNoticeIdAndNoticeImgId(noticeId, noticeImgId);
//		noticeImgRepository.deleteAll(notice);
//    }
	 
}
