package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{
	
	List<ItemImg> findByItemId(Long itemId);
}
