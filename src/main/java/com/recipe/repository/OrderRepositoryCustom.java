package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.dto.SearchDto;
import com.recipe.entity.Order;

public interface OrderRepositoryCustom {
	
	Page<Order> findByAdminOrderList(Pageable pageable ,  SearchDto searchDto);
	
}
