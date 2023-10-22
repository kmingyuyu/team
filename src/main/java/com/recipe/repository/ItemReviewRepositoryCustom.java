package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.myPage.dto.ItemReviewHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;


public interface ItemReviewRepositoryCustom {
	
	Page<ItemReviewHistoryDto> findByMyReviewList(Long memberId , Pageable pageable , MyPageSerchDto myPageSerchDto);
	
}
