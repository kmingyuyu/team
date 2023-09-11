package com.recipe.entity;


import com.recipe.constant.ItemCategoryEnum;
import com.recipe.constant.ItemSellStatus;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Item")
@ToString
public class Item extends BaseEntity {
	
	@Id
	@Column(name="item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // id
	
	private String itemNm; // 상품이름
	
	private String itemSubNm; // 상품 간단소개이름
	
	private int price; // 가격
	
	private int stockNumber; //재고
	
	private int sale; // 할인율
	
	@Enumerated(EnumType.STRING)
	private ItemCategoryEnum itemCategoryEnum; //카테고리
	
	@Enumerated(EnumType.STRING) //상품상태 sell , sold_out
	private ItemSellStatus itemSellStatus;
	
	
	
	
	
}
