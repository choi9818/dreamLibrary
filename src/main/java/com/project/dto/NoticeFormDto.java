package com.project.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.project.entity.Member;
import com.project.entity.Notice;
import com.project.entity.NoticeImg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeFormDto {
	private Long id;
	
	@NotBlank(message = "제목을 작성하세요.")
	private String title;			// 제목
	
	private String writer;
	
	@Lob
	@NotBlank(message = "내용을 작성하세요.")
	private String content;			// 내용
	
	//공지 저장 후 수정할 때 이미지 정보를 저장
	private List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();
	//공지 이미지 아이디를 저장하는 리스트, 공지 등록 시에는 아직 공지의 이미지를 저장하지 않았기 때문에
	//아무 값도 들어가 있지 않고 수정 시에 이미지 아이디를 담아둘 용도로 사용
    private List<Long> noticeImgIds = new ArrayList<>();
    //멤버 변수로 modelMapper 객체를 추가
    private static ModelMapper modelMapper = new ModelMapper();
    
    //modelMapper를 이용하여 엔티티 객체와 DTO 객체 간의 데이터를 복사하여 복사한 객체를 반환해주는 메소드   
    public Notice createNotice(){
    	return modelMapper.map(this, Notice.class);
    }
    
    public static NoticeFormDto of(Notice notice){
    	return modelMapper.map(notice, NoticeFormDto.class);
    }
    
}
