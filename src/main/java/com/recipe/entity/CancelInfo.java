package com.recipe.entity;

import com.recipe.constant.CancelStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "cancel_info")
@Entity
@ToString
public class CancelInfo extends BaseTimeEntity {
	
	@Id
	@Column(name = "cancel_info_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int cancelPrice; // 환불금액
	
	private int cancelDeliveryPrice; // 배송비 차감액
	
	private int cancelPoint; // 포인트 반환액
	
	@Enumerated(EnumType.STRING)
	private CancelStatus cancelStatus; // 환불 정보
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "cancelInfo")
	private Order order;
	
	
	public static CancelInfo createCancelInfo(Order order , int cancelPrice , int cancelDeliveryPrice , CancelStatus cancelStatus,int cancelPoint) {
		
		CancelInfo cancelInfo = new CancelInfo();
		
		cancelInfo.setCancelPrice(cancelPrice);
		cancelInfo.setCancelDeliveryPrice(cancelDeliveryPrice);
		cancelInfo.setCancelStatus(cancelStatus);
		cancelInfo.setOrder(order);
		cancelInfo.setCancelPoint(cancelPoint);
		
		return cancelInfo;
		
	}
	
	
	
}
