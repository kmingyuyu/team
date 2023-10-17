package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.constant.DeliveryStatus;
import com.recipe.entity.Delivery;


public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	
	Delivery findByInvoiceNumber(String invoiceNumber);
	
	Delivery findByOrderNumber(String orderNumber);
	
	@Query("SELECT d FROM Delivery d JOIN FETCH d.deliveryAddress WHERE d.deliveryStatus = :deliveryStatus")
	List<Delivery> findByDeliveryStatus(@Param("deliveryStatus") DeliveryStatus deliveryStatus);
	
	
}
