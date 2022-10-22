package com.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${uploadPath}")
	String uploadPath;		/* file:///C:/Project/image/ */

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")
				.addResourceLocations(uploadPath);
	}
	
	/*
	 * addResourceHandler
	 * 	- 소스와 연결될 URL path를 지정
	 * 	- 클라이언트가 파일에 접근하기 위해 요청하는 URL
	 * 	- localhost:8080/images/**
	 * 
	 * addResourceLocations
	 * 	- 실제 리소스가 존재하는 외부 경로 지정
	 * 	- 경로의 마지막은 반드시 "/"로 끝나야 함
	 * 	- 로컬디스크 경로일 경우 file:/// 접두어를 꼭 붙여야 함
	 * 
	 * -->
	 * 클라이언트로부터 http://호스트주소:포트/images/testImage.jpg와 같은 요청이 들어왔을 때
	 * /Project/image/testImage.jpg 파일로 연결됨
	 * 
	 * 
	 * 
	 * */
	
	/*
	 * 1. uploadPath 프로퍼티 값을 읽어옴
	 * 
	 * 2. 웰브라우저에 입력하는 url에
	 * /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정
	 * 
	 * 3. 로컬컴퓨터에 저장된 파일을 읽어올 root 경로를 설정
	 * 
	 * */
	
	
	
}
