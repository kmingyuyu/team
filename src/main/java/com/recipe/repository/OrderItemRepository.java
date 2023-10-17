package com.recipe.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.constant.OrderStatus;
import com.recipe.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
	 Long countByOrderStatusAndOrder_Member_Id(OrderStatus orderStatus, Long memberId);
	
	
}
