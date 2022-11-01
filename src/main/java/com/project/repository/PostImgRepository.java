package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.PostImg;

public interface PostImgRepository extends JpaRepository<PostImg, Long>{
	List<PostImg> findByPostIdOrderByIdAsc(Long postId);
}
