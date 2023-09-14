package com.recipe.dto;

import com.recipe.constant.ItemSellStatus;


public interface CartDto {
	
	 Long getCartId(); // 카트id
	 
	 Long getItemId(); // 아이템id
	
	 int getCount(); // 카트담은 수량

	 String getItemNm(); // 상품이름
	 
	 int getPrice(); // 가격
	
	 int getStockNumber(); //재고
	
	 int getSale(); // 할인율
	
	 ItemSellStatus getItemSellStatus(); //상품상태 sell , sold_out
	
	 String getImgUrl(); // 메인이미지 
	 
	 boolean getChecked(); // 체크박스 
	
	
}
