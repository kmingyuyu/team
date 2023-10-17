package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.constant.ItemReviewStatus;
import com.recipe.entity.ItemReview;

public interface ItemReviewRepository extends JpaRepository<ItemReview, Long> , ItemReviewRepositoryCustom {
	
	 Optional<ItemReview> findById(Long id);
	 
	 ItemReview findByOrderItemId(Long orderItemId);
	 
	 Long countByItemReviewStatusAndMemberId(ItemReviewStatus itemReviewStatus,Long memberId);
	 
	 
}
