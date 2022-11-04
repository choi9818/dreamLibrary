package com.project.dto;

import com.project.constant.Role;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearchDto {
	
	// 키워드 검색 : 이메일, 이름
	private String searchBy;
	private String searchQuery = "";
	
	// 권한으로 검색
	private Role searchRole;

}
