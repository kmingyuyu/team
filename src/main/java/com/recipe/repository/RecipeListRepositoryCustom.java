package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.dto.MngRecipeDto;
import com.recipe.dto.MngRecipeSearchDto;

public interface RecipeListRepositoryCustom {

	Page<MngRecipeDto> getAdminRecipePage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable);

}
