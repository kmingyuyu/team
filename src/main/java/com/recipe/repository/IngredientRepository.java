package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.RecipeIngre;

public interface IngredientRepository extends JpaRepository<RecipeIngre, Long>  {

	
	
}
