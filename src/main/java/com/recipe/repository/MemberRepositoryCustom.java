package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.recipe.dto.MemberDto;
import com.recipe.dto.MngMemberDto;
import com.recipe.dto.MngRecipeSearchDto;

public interface MemberRepositoryCustom {

	Page<MngMemberDto> getAdminMemberPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable);

}
