package com.recipe.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "delivery_item")
@Entity
@ToString
public class DeliveryItem {
	
	@Id
	@Column(name = "delivery_item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String itemNm; //  상품 이름
	
	private int count; // 상품 수량
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	
}
