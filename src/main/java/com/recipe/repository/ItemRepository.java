package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.Item;

public interface ItemRepository  extends JpaRepository<Item, Long> , ItemRepositoryCustom{
	
	 Optional<Item> findById(Long id);
	
	
}
