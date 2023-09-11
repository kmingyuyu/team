package com.recipe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.dto.ItemReviewAnswerDto;
import com.recipe.entity.ItemReviewAnswer;

public interface ItemReviewAnswerRepository extends JpaRepository<ItemReviewAnswer, Long> {

	List<ItemReviewAnswerDto> findByItemReviewId(Long reviewId);
	
	Optional<ItemReviewAnswer> findById(Long id);
	
}
