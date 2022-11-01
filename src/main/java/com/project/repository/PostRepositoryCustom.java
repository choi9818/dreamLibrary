package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.PostSearchDto;
import com.project.entity.Post;

public interface PostRepositoryCustom {
	Page<Post> getPostPage(PostSearchDto postSearchDto, Pageable pageable);
}
