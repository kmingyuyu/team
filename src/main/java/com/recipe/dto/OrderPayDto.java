package com.recipe.dto;

import java.util.List;

import com.recipe.valid.ValidPointUse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPayDto {
	
	@NotNull(message = "상품 번호")
	private List<Long> itemIds; // 상품 번호
	
	@NotNull(message = "상품 수량")
	private List<Integer> counts; // 상품 수량
	
	@NotNull(message = "상품 주문금액")
	private List<Integer> itemOrderPrices; // 상품 주문금액
	
	@NotNull(message = "상품 할인금액")
	private List<Integer> itemSalePrices; // 상품 할인금액
	
	@NotNull(message = "상품 주문번호")
	private String orderNumber; // 주문번호
	
	@NotNull(message = "주문자 이메일")
	private String buyerEmail; // 주문자 이메일
	
	@NotNull(message = "주문자 성함")
	private String buyerName; // 주문자 성함
	
	@NotNull(message = "받는분 성함")
	private String takerName; // 받는분 성함
	
	@NotNull(message = "받는분 전화번호")
	private String takeTell; // 받는분 전화번호
	
	@NotNull(message = "우편번호")
	private String postCode; // 우편번호
	
	@NotNull(message = "주소")
	private String address; // 주소
	
	@NotNull(message = "상세주소")
	private String detailAddress; // 상세주소
	
	@NotNull(message = "배송 메모")
	private String deliveryMemo; // 배송메모
	
	private int totalPrice; // 총상품 금액
	
	private int deleveryPrice; // 배송비
	
	private int salePrice; // 할인받은 금액
	
	private int finalPrice; // 최종 결제금액
	
	@ValidPointUse
	private Integer usePoint; // 사용한 포인트
	
	private String imp_uid; // 사용자의 결제정보 (토큰발급받기위한)
	
	
	
	
	
	
	

}
