package com.project.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class AuditConfig {
	@Bean //로그인 된 사람을 인지함
	public AuditorAware<String> auditorProvider(){
		return new AuditorAware<String>() {//user가 있어야 create 가능

			@Override
			public Optional<String> getCurrentAuditor() {
				Authentication authentication = //시큐리티 객체에 있는 인증자 데리고 옴
						SecurityContextHolder.getContext().getAuthentication();
				String userId = "";
				//인증 됐을 때
				if(authentication != null) {//인증 된 사람의 데이터 가지고 옴
					userId = authentication.getName();
				}
				return Optional.of(userId);//없으면 nullpointExec 일어남
			}
		};
	}
}
