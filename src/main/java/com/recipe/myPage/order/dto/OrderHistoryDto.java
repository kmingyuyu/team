package com.recipe.myPage.order.dto;

import java.time.LocalDateTime;

public interface OrderHistoryDto {
	
	Long getItemId();
	
	Long getOrderItemId();
	
	String getItemNm();
	
	int getOrderPrice();
	
	int getSalePrice();
	
	int getOrderCount();
	
	String getOrderStatus();
	
	String getImgUrl();
	
	String getOrderNumber();
	
	String getInvoiceNumber();
	
	LocalDateTime getRegTime();
	
	boolean getReviewOk();
	
}
