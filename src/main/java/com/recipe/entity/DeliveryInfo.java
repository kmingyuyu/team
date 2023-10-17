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
@Table(name = "delivery_info")
@Entity
@ToString
public class DeliveryInfo extends BaseTimeEntity {
	
	@Id
	@Column(name = "delivery_info_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	private String deliveryMessage; //배송메세지
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	
	public static DeliveryInfo messageUpdate(String message , Delivery delivery) {
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		deliveryInfo.setDeliveryMessage(message);
		deliveryInfo.setDelivery(delivery);
		return deliveryInfo;
	}
	
	
}
