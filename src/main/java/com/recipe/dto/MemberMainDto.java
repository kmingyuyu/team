package com.recipe.dto;

public interface MemberMainDto {
	
	Long getId();
	
	int getFollowCount();
	
	int getFollowingCount();
	
	int getRecipeCount();
	
	int getReviewCount();
	
	double getRatingAvg();
	
	int getTotalCountCount();
	
	int getCommentCount();
	
	String getNickname();
	
	String getImgUrl();
	
}
