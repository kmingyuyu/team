package com.recipe.dto;

import com.recipe.constant.CategoryEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeSearchDto {
	
	private CategoryEnum mainCategory;
	
	private String type;
	
	private int dataNum;
	
	private String searchBy;
	
	private String searchQuery = "";
	
	
}
