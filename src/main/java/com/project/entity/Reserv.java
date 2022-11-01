package com.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reserv")
@Getter @Setter @ToString
public class Reserv extends BaseTimeEntity {
	
	@Id
	@Column(name = "reserv_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
		/*
		 * 회원 한명당 한개의 예약목록을 가질 수 있음
		 * -> 예약목록 엔티티가 일방적으로 회원 엔티티를 참조
		 * 	-> 일대일 단방향 매핑
		 * 
		 * */
	
	//----------------------------------------------
	
	// 현재 로그인한 회원의 예약도서목록을 생성
	public static Reserv createReserv(Member member) {
		Reserv reserv = new Reserv();
		reserv.setMember(member);
		
		return reserv;
	}

}
