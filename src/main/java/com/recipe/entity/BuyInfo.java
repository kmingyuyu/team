package com.recipe.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "buy_info")
@Entity
@ToString
public class BuyInfo {
	
	@Id
	@Column(name = "buy_info_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 결제 일련번호
	
	private String payProvider; //결제 유형 (payco ,naverPay 등)
	
	private String payMethod; // 결제방식 (card , point 등)
	
	private String cardName; // 카드사 정보 (신한카드 , 롯데카드 등)
	
	private String bankName; // 은행 정보 
	
	private String orderItemName; // 주문시 사용했던 상품명
	
	private String cardNumber; // 카드 번호
	
	private int amount ; // 실 결제금액
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "buyInfo")
	private Order order;
}
