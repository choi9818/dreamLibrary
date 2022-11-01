package com.project.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional//비즈니스 로직 담당하는 서비스 계층엔 에러 이전 데이터로 돌아가기 위한 트랜잭션 선언
@RequiredArgsConstructor//final이나 @Nonnull이 붙은 필드에 생성자를 생성해줌
public class MemberService implements UserDetailsService{
	private final MemberRepository memberRepository;
	
	public Member saveMember(Member member){//멤버 저장
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){//이미 가입된 회원의 경우 반환
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//로그인할 유저의 email을 파라미터로 전달 받는다.
		Member member = memberRepository.findByEmail(email);
		
		if(member==null) {
			throw new UsernameNotFoundException(email);
		}
		return User.builder()//User 객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role을 파라미터로 넘겨 준다.
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	
    /* UserDetailsService 인터페이스는 DB에서 회원 정보를 가져오는 역할을 담당
       loadUserByUsername() 메소드가 존재하며, 회원 정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환
     * 스프링 시큐리티에서 UserDetailsService를 구현하고 있는 클래스를 통해 로그인 기능을 구현
     
     * UserDetails 회원의 정보를 담기 위해 사용
       인터페이스를 직접 구현하거나 스프링 시큐리티에서 제공하는 User 클래스 사용*/
    }

}
