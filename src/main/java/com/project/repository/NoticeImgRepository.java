package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.dto.NoticeImgDto;
import com.project.entity.Notice;
import com.project.entity.NoticeImg;

public interface NoticeImgRepository extends JpaRepository<NoticeImg, Long>{
	List<NoticeImg> findByNoticeIdOrderByIdAsc(Long noticeId);
	
	//List<NoticeImg> findByNoticeIdAndNoticeImgId(Long noticeId, Long noticeImgId);
	
}
