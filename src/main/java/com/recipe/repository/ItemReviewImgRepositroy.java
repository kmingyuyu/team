package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.dto.ItemReviewImgDto;
import com.recipe.entity.ItemReviewImg;

public interface ItemReviewImgRepositroy extends JpaRepository<ItemReviewImg, Long> {
	
	@Query(value = "SELECT i.item_review_img_id id, i.img_url imgUrl, i.item_review_id itemReviewId  "
			+ "FROM item_review_img i "
			+ "WHERE i.item_review_id = :id", nativeQuery = true)
	List<ItemReviewImgDto> getItemReviewImgList(@Param("id") Long id);
	
}
