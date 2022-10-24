package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	//http 요청에 대한 보안 설정, 권한 설정 추가
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);//스프링 시큐리티가 항상 세션 생성
        http.formLogin()
                .loginPage("/member/login")//로그인 페이지 url 설정
                .defaultSuccessUrl("/")//성공시 이동할 url
                .usernameParameter("email")//로그인시 사용할 파라미터 이름으로 email 지정
                .failureUrl("/member/login/error")//로그인 실패시 이동할 url을 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))//로그아웃 url
                .logoutSuccessUrl("/");
        http.authorizeHttpRequests()//시큐리티 처리에 authorizeHttpRequests 이용한다는 것 의미
        	.mvcMatchers("/","/member/**","/notice/**","/post/**").permitAll()//모든 사용자가 인증(로그인) 없이 해당 경로에 접근할 수 있도록 설정
        	.mvcMatchers("/assets/**", "/fonts/**", "/js/**", "/img/**",
   				 "/libs/**", "/scss/**", "/tasks/**").permitAll()
        	//.mvcMatchers("/mypage/**").hasRole("USER")
        	.mvcMatchers("/admin/**").hasRole("ADMIN")
        	.anyRequest().authenticated();//위에서 지정해준 경로를 제외한 나머지 경로들은 모두 인증(로그인)을 요구
//        http.exceptionHandling()//에러날까봐 걱정하는 문장
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {//비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

}
