package com.recipe.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.recipe.constant.DeliveryStatus;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "delivery")
@Entity
@ToString
// 가상의 택배사 (:이젠 택배)
public class Delivery extends BaseTimeEntity  {
	
	@Id
	@Column(name = "delivery_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 배송 일련번호
	
	private String invoiceNumber; //송장 번호
	
	private String deliveryMemo; // 배송메모
	
	private String orderNumber; // 주문번호  // 택배사는 다른서버에 위치하므로 주문번호로 서로 연결시킴
	
	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus; // 배송 상태
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "Delivery_address_id")
	private DeliveryAddress deliveryAddress; //배송 주소
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_person_id")
	private DeliveryPerson deliveryPerson; //배송 기사
	
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryInfo> deliveryInfos = new ArrayList<>(); //배송 메세지
    
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryItem> deliveryItems = new ArrayList<>(); //배송 상품
  
//  정상주문일떄 : 사이트에서 주문요청시 생성
    public static Delivery createOrder(Order order) {
    	Delivery delivery = new Delivery();
    	delivery.setOrderNumber(order.getOrderNumber());
    	delivery.setDeliveryMemo(order.getDeliveryMemo());
    	delivery.setDeliveryStatus(DeliveryStatus.배송전);
    	delivery.createOrderDeliveryAddress(order);
    	delivery.addDeliveryItem(order);
    	
    	return delivery;
    }
    
//  정상주문일때 : 주문할 상품 이름/ 수량 넣기
    public void addDeliveryItem(Order order) {
	List<OrderItem> orderitems = order.getOrderItems();
    	for(OrderItem i : orderitems) {
    		DeliveryItem deliveryItem = new DeliveryItem();
    		deliveryItem.setItemNm(i.getItem().getItemNm());
    		deliveryItem.setCount(i.getCount());
    		deliveryItem.setDelivery(this);
    		this.deliveryItems.add(deliveryItem);
    	}
    }
    
//   정상주문일때 : 주소넣기
    public void createOrderDeliveryAddress(Order order) {
    	DeliveryAddress deliveryAddress = new DeliveryAddress();
    	deliveryAddress.setReceiveName(order.getTakerName());
    	deliveryAddress.setReceivePhoneNumber(order.getTakeTell());
    	deliveryAddress.setReceivePostCode(order.getPostCode());
    	deliveryAddress.setReceiveAddress(order.getAddress());
    	deliveryAddress.setReceiveDetailAddress(order.getDetailAddress());
    	deliveryAddress.setSendName("이젠맛있게");
    	deliveryAddress.setSendPhoneNumber("032-719-4074");
    	deliveryAddress.setSendPostCode("21558");
    	deliveryAddress.setSendAddress("인천광역시 남동구 인주대로 593");
    	deliveryAddress.setSendDetailAddress("엔타스빌딩 12층");
    	deliveryAddress.setDelivery(this);
    	this.setDeliveryAddress(deliveryAddress);
    }

//  정상주문일때 배송기사넣기
    public void createDeliveryPerson() {
    	DeliveryPerson deliveryPerson = new DeliveryPerson();
    	deliveryPerson.setDeliveryPerson("김택배");
    	deliveryPerson.setDeliveryPersonPhoneNumber("010-1111-1111");
    	deliveryPerson.setDelivery(this);
    	this.setDeliveryPerson(deliveryPerson);
    }

    
}
