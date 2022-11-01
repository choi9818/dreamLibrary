package com.project.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservBookDto {
	
	@NotBlank(message = "ISBN은 필수입력값입니다.")
	private String bookIsbn;

}
