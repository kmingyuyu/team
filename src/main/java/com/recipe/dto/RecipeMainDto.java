package com.recipe.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

public interface RecipeMainDto {
	
	Long getId();
	
	String getTitle();
	
	String getSubTitle();
	
	String getIntro();
	
	String getDurTime();
	
    String getLevel();
    
    String getImageUrl();
	
    int getCount();
    
    String getNickname();
    
    String getImgUrl();
    
    Long getReviewCount();
    
    Long getBookCount();
    
    double getRetingAvg();
    
    Long getMemberId();
	
}
