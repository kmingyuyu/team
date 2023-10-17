package com.recipe.entity;


import org.springframework.web.multipart.MultipartFile;

import com.recipe.constant.ItemReviewStatus;

import groovy.transform.ToString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="item_review")
@Entity
public class ItemReview extends BaseTimeEntity {
	
	@Id
	@Column(name="item_review_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private double rating;
	
	@Lob
	private String content;

	private Long orderItemId;
	
	private String orderNumber;
	
	@Enumerated(EnumType.STRING)
	private ItemReviewStatus itemReviewStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "item_review_answer_id")
	private ItemReviewAnswer itemReviewAnswer;
	
	
	public static ItemReview createItemReview(Member member,Item item, OrderItem orderItem, String content,double star, MultipartFile[]files) {
		
		ItemReview itemReview = new ItemReview();
		
		itemReview.setMember(member);
		itemReview.setItem(item);
		itemReview.setContent(content);
		itemReview.setRating(star);
		itemReview.setOrderItemId(orderItem.getId());
		itemReview.setOrderNumber(orderItem.getOrder().getOrderNumber());
		
		if(files == null) {
			itemReview.setItemReviewStatus(ItemReviewStatus.일반);	
		}
		else {
			itemReview.setItemReviewStatus(ItemReviewStatus.포토);	
		}
		
		
		return itemReview;
	}
	
}
