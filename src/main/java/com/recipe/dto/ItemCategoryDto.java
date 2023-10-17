package com.recipe.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.ItemCategoryEnum;
import com.recipe.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCategoryDto {
	
	private Long id;
	
	private String itemNm;
	
	private String itemSubNm;
	
	private int price;
	
	private ItemSellStatus itemSellStatus;
	
	private String imgUrl;
	
	private ItemCategoryEnum itemCategoryEnum;
	
	private LocalDateTime regTime;
	
	private int sale;
	
	private int stockNumber;
	
	private double ratingAvg;
	
	private Long reviewCount;
	
	private Long orderCount;
	
	@QueryProjection
	public ItemCategoryDto (Long id ,String itemNm , String itemSubNm , 
			int price , ItemSellStatus itemSellStatus , String imgUrl,
			ItemCategoryEnum itemCategoryEnum ,LocalDateTime regTime , 
			int sale , int stockNumber ,double ratingAvg , Long reviewCount , Long orderCount) {
		this.id = id;
		this.itemNm = itemNm;
		this.itemSubNm = itemSubNm;
		this.price = price;
		this.itemSellStatus = itemSellStatus;
		this.imgUrl = imgUrl;
		this.itemCategoryEnum = itemCategoryEnum;
		this.regTime = regTime;
		this.sale = sale;
		this.stockNumber = stockNumber;
		this.ratingAvg = ratingAvg;
		this.reviewCount = reviewCount;
		this.orderCount = orderCount;
	}
	
	
}
