package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.dto.NoticeImgDto;
import com.project.entity.NoticeImg;

public interface NoticeImgRepository extends JpaRepository<NoticeImg, Long>{
	List<NoticeImg> findByNoticeIdOrderByIdAsc(Long noticeId);
}
