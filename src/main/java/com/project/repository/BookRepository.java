package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.project.entity.Book;

public interface BookRepository extends JpaRepository<Book, String>,
										QuerydslPredicateExecutor<Book>,
										BookRepositoryCustom {

}
