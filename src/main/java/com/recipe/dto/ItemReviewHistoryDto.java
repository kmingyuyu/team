package com.recipe.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.ItemReviewStatus;
import com.recipe.entity.ItemReviewAnswer;
import com.recipe.entity.ItemReviewImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReviewHistoryDto {
	
	private Long itemId;
	
	private Long itemReviewId;
	
	private double rating;
	
	private String content;
	
	private ItemReviewStatus itemReviewStatus;
	
	private String itemNm;
	
	private String imgUrl;
	
	private LocalDateTime regTime;
	
	private ItemReviewAnswer itemReviewAnswer;
	
	private List<ItemReviewImg> itemReviewImgList = new ArrayList<>();
	
	@QueryProjection
	public ItemReviewHistoryDto(Long itemId , Long itemReviewId , double rating , String content , 
			ItemReviewStatus itemReviewStatus , String itemNm , String imgUrl , LocalDateTime regTime) {
		
		this.itemId = itemId;
		this.itemReviewId = itemReviewId;
		this.rating = rating;
		this.content = content;
		this.itemReviewStatus = itemReviewStatus;
		this.itemNm = itemNm;
		this.imgUrl = imgUrl;
		this.regTime = regTime;
		
	}
	
	
}
