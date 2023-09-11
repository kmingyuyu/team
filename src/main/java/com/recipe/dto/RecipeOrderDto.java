package com.recipe.dto;

import org.modelmapper.ModelMapper;

import com.recipe.entity.RecipeOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeOrderDto {
	private Long id;

	private int orderNumber;
	
	private String content;
	
	private String imgUrl;
	
	private String ImgName;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//엔티티를 dto로 변환
	public static RecipeOrderDto of(RecipeOrder recipeOrder) {
		return modelMapper.map(recipeOrder, RecipeOrderDto.class);
		
	}
}