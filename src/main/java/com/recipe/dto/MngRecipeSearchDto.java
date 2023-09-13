package com.recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MngRecipeSearchDto {
	private String searchBy;
	private String searchQuery = "";
	private String sort;
	
}
