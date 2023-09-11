package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipe.entity.Recipe;



public interface RecipeListRepository extends JpaRepository<Recipe, Long> {
	

	
	boolean existsByTitle(String title);
}
