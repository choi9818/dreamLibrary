package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.project.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>
	, QuerydslPredicateExecutor<Notice>, PageRepositoryCustom{
	
	@Query(value="select * from notice n join notice_img ni on n.notice_id = ni.notice_id "
			+ "where n.notice_id = :notice_id", nativeQuery = true)
	Notice getNoticeIdAndNoticeImgId(@Param("notice_id") Long noticeId);
}
