package com.recipe.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.dto.SearchDto;
import com.recipe.entity.Order;
import com.recipe.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MngService {
	
	private final OrderRepository orderRepository;
	
//	주문상품중 주문완료 << 배송처리 안된 배송건 가져오기
	@Transactional(readOnly = true)
	public Page<Order> getMngOrderPageList(Pageable pageable , SearchDto searchDto){
		Page<Order> getMngOrderPageList = orderRepository.findByAdminOrderList(pageable, searchDto);
		return getMngOrderPageList;
	}
	
	public Order orderDeliveryRequest(String orderNumber) {
			Order order = orderRepository.findByOrderNumber(orderNumber);
			return order;
		} 
		
	}
