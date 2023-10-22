package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.myPage.dto.ItemInqHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;


public interface ItemInqRepositoryCustom {
	
	Page<ItemInqHistoryDto> findByMyItemInqList(Pageable pageable,Long memberId , MyPageSerchDto myPageSerchDto);
	
}
