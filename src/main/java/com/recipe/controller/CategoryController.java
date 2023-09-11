package com.recipe.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.recipe.dto.RecipeCategoryDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController {
	
	private final RecipeService recipeService;
	
	@GetMapping(value = {"/category" , "/category/{page}"})
	public String category(RecipeSearchDto recipeSearchDto ,
			@PathVariable("page") Optional<Integer> page , Model model) {
		int pageNm = (recipeSearchDto.getDataNum() == 0) ? 12 : recipeSearchDto.getDataNum();
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 12);
		
		Page<RecipeCategoryDto> category = recipeService.getRecipeCategoryReviewBestList(pageable, recipeSearchDto);
		int currentPage = page.isPresent() ? page.get() : 0;
		model.addAttribute("currentPage" , currentPage);
		
		model.addAttribute("category" , category);
		model.addAttribute("recipeSearchDto" , recipeSearchDto);
		model.addAttribute("maxPage" , 5);
		model.addAttribute("page" , page);
	
		
		return "category";
	}
	
	
}
	
	

