package com.recipe.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.dto.ItemCategoryDto;
import com.recipe.dto.ItemDetailDto;
import com.recipe.dto.ItemInqDto;
import com.recipe.dto.ItemReviewAnswerDto;
import com.recipe.dto.ItemReviewDto;
import com.recipe.dto.ItemSearchDto;

public interface ItemRepositoryCustom {
	
	Page<ItemCategoryDto> getItemCategoryList(Pageable pageable , ItemSearchDto itemSearchDto);
	
	ItemDetailDto getItemDetailList(Long itemId);
	
	Page<ItemReviewDto> getItemReviewList(Pageable pageable , Long itemId);
	
	Page<ItemInqDto> getItemInqList(Pageable pageable , Long itemId);
	
}
