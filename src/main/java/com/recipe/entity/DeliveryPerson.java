package com.recipe.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "delivery_person")
@Entity
@ToString
public class DeliveryPerson {
	
	@Id
	@Column(name = "delivery_person_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 배송 일련번호
	
	private String deliveryPerson; // 배송기사
	
	private String deliveryPersonPhoneNumber; // 배송기사 전화번호
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "deliveryPerson")
    private Delivery delivery;
	
	
}
