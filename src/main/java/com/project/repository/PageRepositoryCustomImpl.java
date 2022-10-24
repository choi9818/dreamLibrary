package com.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.project.dto.SearchDto;
import com.project.entity.Notice;
import com.project.entity.QNotice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class PageRepositoryCustomImpl implements PageRepositoryCustom{
	//동적 쿼리 생성 위해
	private JPAQueryFactory queryFactory;
	
	//JPAQueryFactory 생성자로 EntityManager 객체 넣음
	public PageRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	//searchDateType의 값에 따라서 현재 시간으로 부터 특정 기간 동안의 상품 조회
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();
		if(StringUtils.equals("all", searchDateType) || searchDateType == null){
			return null;
		} else if(StringUtils.equals("1d", searchDateType)) {
			dateTime = dateTime.minusDays(1);
		}else if(StringUtils.equals("1w", searchDateType)) {
			dateTime = dateTime.minusWeeks(1);
		}else if(StringUtils.equals("1m", searchDateType)) {
			dateTime = dateTime.minusMonths(1);
		}else if(StringUtils.equals("6m", searchDateType)) {
			dateTime = dateTime.minusMonths(6);
		}
		return QNotice.notice.regTime.after(dateTime);
	}
	
	//searchBy에 따라 제목과 유저로 검색
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("title", searchBy)) {
			return QNotice.notice.title.like("%"+searchQuery+"%");
		} else if (StringUtils.equals("createdBy", searchBy)) {
			return QNotice.notice.createdBy.like("%"+searchQuery+"%");
		}
		return null;
	}

	@Override
	public Page<Notice> getAdminNoticePage(SearchDto searchDto, Pageable pageable) {
		//공지 데이터를 조회하기 위해서 지정
		long total = queryFactory.select(Wildcard.count).from(QNotice.notice)
				//BooleanExpression을 반환하는 조건문들
				.where(regDtsAfter(searchDto.getSearchDateType()), 
						searchByLike(searchDto.getSearchBy(),
								searchDto.getSearchQuery())).fetchOne();      
		List<Notice> noticeList = queryFactory.selectFrom(QNotice.notice)
				.where(regDtsAfter(searchDto.getSearchDateType()), 
						searchByLike(searchDto.getSearchBy(),
								searchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                //데이터를 가지고 올 시작 인덱스 지정
                .offset(pageable.getOffset())
                //한 번에 가지고 올 최대 개수 지정
                .limit(pageable.getPageSize()).fetch();         

        //조회한 데이터를 page 클래스의 구현제인 PageImpl 객체로 반환
        return new PageImpl<>(noticeList, pageable, total);
        
        
    
	}

}
