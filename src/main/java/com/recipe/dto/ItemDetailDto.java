package com.recipe.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.ItemCategoryEnum;
import com.recipe.constant.ItemSellStatus;
import com.recipe.entity.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDetailDto {
	
	private Long id;
	
	private String itemNm;

	private String itemSubNm;
	
	private int price;
	
	private ItemSellStatus itemSellStatus;
	
	private ItemCategoryEnum itemCategoryEnum;
	
	private int sale;
	
	private int stockNumber;
	
	private double retingAvg;
	
	private Long reviewCount;
	
	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
	
	private List<ItemDetailImgDto> itemDetailImgDtoList = new ArrayList<>();
	
	@QueryProjection
	public ItemDetailDto (Long id ,String itemNm , String itemSubNm , 
			int price , ItemSellStatus itemSellStatus , ItemCategoryEnum itemCategoryEnum  , 
			int sale ,int stockNumber , double retingAvg , Long reviewCount) {
		this.id = id;
		this.itemNm = itemNm;
		this.itemSubNm = itemSubNm;
		this.price = price;
		this.itemSellStatus = itemSellStatus;
		this.itemCategoryEnum = itemCategoryEnum;
		this.sale = sale;
		this.stockNumber = stockNumber;
		this.retingAvg = retingAvg;
		this.reviewCount = reviewCount;
	}
	
	
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}
	
	public static ItemDetailDto of (Item item) {
		return modelMapper.map(item, ItemDetailDto.class);
	}
	
}
