package com.recipe.entity;

import java.util.ArrayList;
import java.util.List;

import com.recipe.constant.OrderStatus;
import com.recipe.dto.OrderPayDto;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "orders")
@Entity
@ToString
public class Order extends BaseTimeEntity {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 주문 일련번호

	private String orderNumber; // 주문번호

	private String buyerEmail; // 주문자 이메일

	private String buyerName; // 주문자 성함

	private String takerName; // 받는분 성함

	private String takeTell; // 받는분 전화번호

	private String postCode; // 우편번호

	private String address; // 주소

	private String detailAddress; // 상세주소

	private int totalPrice; // 총상품 금액

	private int deleveryPrice; // 배송비

	private int salePrice; // 할인받은 금액

	private int usePoint; // 사용한 포인트

	private int finalPrice; // 최종 결제금액

	private String impUid; // 사용자의 결제정보 (토큰발급받기위한)
	
	private String deliveryMemo; //배송 메모

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "buy_info_id")
	private BuyInfo buyInfo;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_info_id")
	private CancelInfo cancelInfo;
	

	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		orderItem.setOrder(this);
	}

	// order 객체를 생성해준다.
	public static Order createOrder(Member member, List<OrderItem> orderItemList, OrderPayDto orderPayDto , BuyInfo buyInfo) {
		Order order = new Order();
		order.setMember(member);
		order.setBuyInfo(buyInfo);
		
		for (OrderItem orderItem : orderItemList) {
			order.addOrderItem(orderItem);
		}
		
		order.setOrderNumber(orderPayDto.getOrderNumber());
		order.setBuyerEmail(orderPayDto.getBuyerEmail());
		order.setBuyerName(orderPayDto.getBuyerName());
		order.setTakerName(orderPayDto.getTakerName());
		order.setTakeTell(orderPayDto.getTakeTell());
		order.setPostCode(orderPayDto.getPostCode());
		order.setAddress(orderPayDto.getAddress());
		order.setDetailAddress(orderPayDto.getDetailAddress());
		order.setTotalPrice(orderPayDto.getTotalPrice());
		order.setDeleveryPrice(orderPayDto.getDeleveryPrice());
		order.setSalePrice(orderPayDto.getSalePrice());
		order.setUsePoint(orderPayDto.getUsePoint());
		order.setFinalPrice(orderPayDto.getFinalPrice());
		order.setImpUid(orderPayDto.getImp_uid());
		order.setDeliveryMemo(orderPayDto.getDeliveryMemo());

		return order;
	}
	

}
