package com.project.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.constant.Role;
import com.project.dto.MemberFormDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter @Setter @ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member{
	
	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	
	@Column(nullable = false)
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	//멤버 만드는 거, serivce에 선언함
	public static Member createMember(MemberFormDto memberFormDto, 
			PasswordEncoder passwordEncoder) {
		Member member = new  Member();
		member.setEmail(memberFormDto.getEmail());
		member.setName(memberFormDto.getName());
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password);
		member.setBirthday(memberFormDto.getBirthday());
		member.setPhone(memberFormDto.getPhone());
		member.setRole(Role.USER);
		return member;
	}

	public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		this.password = password;
		this.name = memberFormDto.getName();
		this.phone = memberFormDto.getPhone();
	}

}
