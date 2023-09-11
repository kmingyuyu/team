package com.recipe.entity;

import com.recipe.constant.AnswerOk;
import com.recipe.constant.ItemInqBoardEnum;
import com.recipe.constant.ItemInqEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="item_inq")
public class ItemInq extends BaseEntity {
	
	@Id
	@Column(name="item_inq_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title; // 제목
	
	private String content; // 내용
	
	@Enumerated(value = EnumType.STRING)
	private ItemInqBoardEnum itemInqBoardEnum = ItemInqBoardEnum.공개글 ; // 비밀글 여부 // 기본값:공개글
	
	@Enumerated(value = EnumType.STRING)
	private ItemInqEnum itemInqEnum; // 배송 재입고 상세문의 기타 선택 여부
	
	@Enumerated(value = EnumType.STRING)
	private AnswerOk answerOk = AnswerOk.답변대기;  // 답변여부 기본값:대기
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	
	
}
