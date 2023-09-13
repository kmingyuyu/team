package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.PostDto;

public interface PostRepositoryCustom {

	Page<PostDto> getAdminPostListPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable);

	Page<PostDto> getPostListPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable);
}
