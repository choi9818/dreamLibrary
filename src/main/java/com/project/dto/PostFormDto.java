package com.project.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.project.entity.Notice;
import com.project.entity.Post;

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
public class PostFormDto {
	private Long id;
	
	private String email;//글쓴 사람 정도
	
	@NotBlank(message = "제목을 작성하세요.")
	private String title;			// 제목
	
	@Lob
	@NotBlank(message = "내용을 작성하세요.")
	private String content;			// 내용
	
	private List<PostImgDto> postImgDtoList = new ArrayList<>();
	private List<Long> postImgIds = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	public Post createPost() {
		return modelMapper.map(this, Post.class);
	}
	public static PostFormDto of(Post post) {
		return modelMapper.map(post, PostFormDto.class);
	}
}
