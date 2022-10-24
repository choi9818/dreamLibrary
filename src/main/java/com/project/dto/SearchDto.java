package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchDto {
	private String searchDateType; //현재 시간과 등록일 비교해서 조회
	private String searchBy; // 어떤 유형으로 조회할지 선택
	private String searchQuery = "";// 조회할 검색어 저장 변수 searchBy 기준으로 달라짐
	
}
