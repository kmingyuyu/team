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
@Table(name = "Delivery_address")
@Entity
@ToString
public class DeliveryAddress {
	
	@Id
	@Column(name = "Delivery_address_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String sendName; //발송자 성함
	
	private String sendPhoneNumber; //발송자 전화번호
	
	private String sendPostCode; // 발송자 우편번호
	
	private String sendAddress; // 발송자 주소
	
	private String sendDetailAddress; // 발송자 상세주소
	
	private String receiveName; // 받는쪽 성함
	
	private String receivePhoneNumber; //받는쪽 전화번호
	
	private String receivePostCode; // 받는쪽 우편번호
	
	private String receiveAddress; // 받는쪽 주소
	
	private String receiveDetailAddress; // 받는쪽 상세주소
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "deliveryAddress")
    private Delivery delivery;
}
