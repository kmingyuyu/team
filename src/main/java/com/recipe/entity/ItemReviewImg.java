package com.recipe.entity;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Table(name="item_review_img")
@Entity
public class ItemReviewImg {
	
	@Id
	@Column(name="item_review_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgUrl;
	
	private String imgName;
	
	private String imgOriName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_review_id")
	private ItemReview itemReview;
}
