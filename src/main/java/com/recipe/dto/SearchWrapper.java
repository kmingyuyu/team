package com.recipe.dto;

public class SearchWrapper {
	private MngMemberSearchDto mngMemberSearchDto;
	private MngRecipeSearchDto mngRecipeSearchDto;
	private String searchQuery;
	
	public MngMemberSearchDto getMemberSearchDto() {
        return mngMemberSearchDto;
    }

    public MngRecipeSearchDto getRecipeSearchDto() {
        return mngRecipeSearchDto;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
