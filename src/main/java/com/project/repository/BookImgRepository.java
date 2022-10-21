package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.BookImg;

public interface BookImgRepository extends JpaRepository<BookImg, Long> {
	
	List<BookImg> findByBookIsbnOrderByIdAsc(String bookIsbn);

}
