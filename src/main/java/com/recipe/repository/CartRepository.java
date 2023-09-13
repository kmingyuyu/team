package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	
	Long countByMemberId(Long memberId);
	
	Cart findByMemberIdAndItemIdAndCount(Long memberId , Long itemId , int count);

}
