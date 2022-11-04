package com.project.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.project.constant.Role;
import com.project.dto.MemberSearchDto;
import com.project.entity.Member;
import com.project.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
	
	private JPAQueryFactory queryFactory;
	
	public MemberRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	// 회원 권한으로 찾기
	private BooleanExpression searchRoleEq(Role searchRole) {
		
		if(StringUtils.equals("ADMIN", searchRole)) {
			return QMember.member.role.eq(searchRole);
		} else if(StringUtils.equals("USER", searchRole)) {
			return QMember.member.role.eq(searchRole);
		} else if(StringUtils.equals("all", searchRole)) {
			return null;
		}
		
		return null;
	}
	
	// 검색어로 찾기 : 이메일, 이름
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		
		if(StringUtils.equals("email", searchBy)) {
			return QMember.member.email.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("name", searchBy)) {
			return QMember.member.name.like("%" + searchQuery + "%");
		}
		
		return null;
	}
	
	/**
	 * 회원 관리 페이지
	 * 
	 * */

	@Override
	public Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {
		
		List<Member> content = queryFactory
				.selectFrom(QMember.member)
				.where(searchRoleEq(memberSearchDto.getSearchRole()),
						searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
				.orderBy(QMember.member.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(QMember.member)
				.where(searchRoleEq(memberSearchDto.getSearchRole()),
						searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}
	
	

}
