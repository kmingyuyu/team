package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.ItemReview;

public interface ItemReviewRepository extends JpaRepository<ItemReview, Long> {
	
	 Optional<ItemReview> findById(Long id);
}
