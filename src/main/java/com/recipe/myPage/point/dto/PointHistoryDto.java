package com.recipe.myPage.point.dto;

import java.time.LocalDateTime;

import com.recipe.constant.PointEnum;

public interface PointHistoryDto {
	
	Long getPointId();
	
	int getPoint();
	
	PointEnum getPointEnum();
	
	String getPointInfo();
	
	String getPointDetailInfo();
	
	LocalDateTime getRegTime();
	
}
