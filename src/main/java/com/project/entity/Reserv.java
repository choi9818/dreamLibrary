package com.project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
	
	@OneToMany(mappedBy = "reserv", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReservBook> reservBooks = new ArrayList<>();
		/* 
		 * 한개의 예약도서목록에는 여러개의 예약도서(책)이 포함되어 있음
		 * 	-> Reserv : ReservBook = 일 : 다
		 * 	-> OneToMany
		 * 
		 * ReservBook 엔티티에서 이미 다대일 매핑을 했기 때문에 양방향 매핑이 됨
		 * 	-> 연관관계 주인을 설정해야 함
		 * 
		 * 외래키 : ReservBook 엔티티의 reserv_id 필드가 외래키임.
		 * 	-> 연관관계 주인 : ReservBook 엔티티
		 * 	-> Reserv 엔티티는 주인이 아니므로 mappedBy 속성을 이용해 주인을 표시해줌
		 * 
		 * mappedBy = "필드"
		 * 	주인이 아닌 엔티티에 달아줘야함
		 * 	여기서 reserv = ReservBook에 있는 Reserv에 의해 관리된다는 의미(연관관계의 주인의 필드)
		 * 
		 * 
		 * cascade = CascadeType.ALL
		 * 	-> 부모엔티티의 영속성 상태 변화를 자식엔티티에 모두 전이
		 * 
		 * orphanRemoval = true : 고아객체 제거
		 * 
		 * */

}
