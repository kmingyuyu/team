package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.dto.MyPageSerchDto;
import com.recipe.dto.ItemReviewHistoryDto;

public interface ItemReviewRepositoryCustom {
	
	Page<ItemReviewHistoryDto> findByMyReviewList(Long memberId , Pageable pageable , MyPageSerchDto myPageSerchDto);
	
}
