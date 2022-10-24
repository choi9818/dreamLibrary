package com.project.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class})	// Auditing적용
@MappedSuperclass		
	// 공통 매핑 정보가 필요할 때 사용하는 어노테이션. 부모클래스를 상속받는 자식클래스에 매핑정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity {
	
	@CreatedDate		// 엔티티가 생성되어 저장될 때 시간을 자동으로 저장
	@Column(updatable = false)
	private LocalDateTime regTime;
	
	@LastModifiedDate	// 엔티티의 값을 변경할 때 시간을 자동으로 저장
	private LocalDateTime updateTime;

}
