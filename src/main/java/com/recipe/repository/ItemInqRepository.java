package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.constant.AnswerOk;
import com.recipe.entity.ItemInq;

public interface ItemInqRepository extends JpaRepository<ItemInq, Long> , ItemInqRepositoryCustom {
	
	 Optional<ItemInq> findById(Long id);
	 
	 Long countByAnswerOkAndMemberId(AnswerOk answerOk , Long memberId);
	 
	 Long countByMemberId(Long memberId);
	
}
