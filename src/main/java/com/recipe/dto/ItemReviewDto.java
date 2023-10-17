package com.recipe.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import com.querydsl.core.annotations.QueryProjection;
import com.recipe.entity.ItemReviewAnswer;
import com.recipe.entity.ItemReviewImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReviewDto {
	
	private Long itemReviewId;
	
	private double rating;
	
	private String content;
	
	private LocalDateTime regTime;
	
	private String nickname;
	
	private String imgUrl;
	
	private ItemReviewAnswer itemReviewAnswer;
	
	private List<ItemReviewImg> itemReviewImgList = new ArrayList<>();
	
	@QueryProjection
	public ItemReviewDto(Long itemReviewId ,double rating , String content ,LocalDateTime regTime , String nickname , String imgUrl) {
		
		this.itemReviewId = itemReviewId;
		this.rating = rating;
		this.content = content;
		this.regTime = regTime;
		this.nickname = nickname;
		this.imgUrl = imgUrl;
		
	}
	
	
}
