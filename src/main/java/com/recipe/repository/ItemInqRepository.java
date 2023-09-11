package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.ItemInq;

public interface ItemInqRepository extends JpaRepository<ItemInq, Long> {
	
	 Optional<ItemInq> findById(Long id);
	
}
