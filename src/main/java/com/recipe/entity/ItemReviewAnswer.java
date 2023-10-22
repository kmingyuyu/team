package com.recipe.entity;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="item_review_answer")
public class ItemReviewAnswer extends BaseEntity {
	
	@Id
	@Column(name="item_review_answer_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Lob
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "itemReviewAnswer")
	private ItemReview itemReview;
	
	public static ItemReviewAnswer createItemReviewAnswer(Member member, ItemReview itemReview,String content) {
		
		ItemReviewAnswer itemReviewAnswer = new ItemReviewAnswer();
		
		itemReviewAnswer.setMember(member);
		itemReviewAnswer.setItemReview(itemReview);
		itemReviewAnswer.setContent(content);
		
		return itemReviewAnswer;
	}
	
	
}
