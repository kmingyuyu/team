package com.recipe.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MngMemberSearchDto {
	private String searchBy;
	private String searchQuery = "";
}
