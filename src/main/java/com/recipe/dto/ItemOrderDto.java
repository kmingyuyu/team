package com.recipe.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrderDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
//	   장바구니페이지에서 받을 데이터
	   private List<Long> ids;
	   private List<Long> cartIds;
	   private List<Integer> counts;
	   
//	   아이템상세페이지에서 받을 데이터
	   private Long id;
	   private Integer count;
	   
}
