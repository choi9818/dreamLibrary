package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.project.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>
	, QuerydslPredicateExecutor<Notice>, PageRepositoryCustom{

}
