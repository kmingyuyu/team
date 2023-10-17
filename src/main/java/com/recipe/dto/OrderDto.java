package com.recipe.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long cartId;
	
	private String itemNm;
	
	private int price;
	
	private int sale;
	
	private String imgUrl;
	
	private int count;
	
	
	
}
