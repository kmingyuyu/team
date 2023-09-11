package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.ItemInqAnwser;

public interface ItemInqAnswerRepository extends JpaRepository<ItemInqAnwser, Long> {
	 Optional<ItemInqAnwser> findById(Long id);
}
