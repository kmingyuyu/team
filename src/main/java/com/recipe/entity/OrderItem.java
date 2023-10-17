package com.recipe.entity;



import com.recipe.constant.OrderStatus;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="order_item")
@Setter
@Getter
@ToString
public class OrderItem extends BaseTimeEntity {
	
	@Id
	@Column(name="order_item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	private int count; // 주문 수량
	
	private int orderPrice; //주문 금액
	
	private int salePrice; //할인받은 금액

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 주문상태
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	
	
	public static OrderItem createOrderItem(Item item , Integer count , Integer orderPrice, Integer salePrice) {
		
		OrderItem orderItem = new OrderItem();
		orderItem.setCount(count);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setSalePrice(salePrice);
		orderItem.setOrderStatus(OrderStatus.주문완료);
		orderItem.setItem(item);
		
		return orderItem;
	}
	
	
}
