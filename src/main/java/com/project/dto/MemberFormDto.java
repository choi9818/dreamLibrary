package com.project.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.project.constant.Role;
import com.project.entity.Member;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberFormDto {
	private Long id;
	
	@NotBlank(message="이름은 필수 입력 값입니다.")
	private String name;
	
	@NotEmpty(message="이메일은 필수 입력 값입니다.")
	@Email
	private String email;
	
	@NotEmpty(message="패스워드는 필수 입력 값입니다.")
	@Length(min=4,max=16, message = "비밀번호는 4자 이상, 16자 이하로 입력하세요")
	private String password;
	
	@NotEmpty(message="전화번호는 필수 입력 값입니다.")
	//@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$"
	//		, message = "10 ~ 11 자리의 숫자만을 입력 가능합니다.")
	private String phone;
		
	private Role role;
	@NotNull(message="생년월일은 필수 입력 값입니다.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static MemberFormDto of(Member member) {
		return modelMapper.map(member, MemberFormDto.class);
	}

	public MemberFormDto(Long id, String name, String email, Role role) {		
		this.id = id;
		this.name = name;
		this.email = email;	
		this.role = role;
	}
	
}
