package com.recipe.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.constant.CategoryEnum;
import com.recipe.constant.WritingStatus;
import com.recipe.dto.RecipeNewDto;
import com.recipe.service.RecipeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecipeController {

	private final RecipeService recipeService;

	// 레시피 등록화면
	@GetMapping(value = "/myPage/recipe/new")
	public String recipe(Model model) {

		RecipeNewDto recipeNewDto = new RecipeNewDto();

		model.addAttribute("recipeNewDto", recipeNewDto);

		return "recipe/new";
	}

	// 레시피 등록기능
	@PostMapping(value = "/myPage/recipe/new")
	public String recipeNew(@RequestParam("recipeImgFile") MultipartFile recipeImgFile ,  @Valid RecipeNewDto recipeNewDto ,  
			BindingResult bindingResult , Model model, 
			@RequestParam("recipeingreName") List<String> recipeingreNameList ,
			@RequestParam("recipeingreMaterial") List<String> recipeingreMaterialList,
			@RequestParam("recipeOrderContent") List<String> recipeOrderContentList,
			@RequestParam("recipeOrderImgFile") List<MultipartFile> recipeOrderImgFile,
			@RequestParam("categoryEnum") String categoryEnumString,
			@RequestParam("writingStatus")String writingStatus, Principal principal) {
		
		CategoryEnum categoryEnum = CategoryEnum.fromString(categoryEnumString);
		recipeNewDto.setCategoryEnum(categoryEnum);
		
		WritingStatus status = WritingStatus.valueOf(writingStatus);

		if (status == WritingStatus.PUBLISHED) {
			recipeNewDto.setWritingStatus(status);
		} else if(status == WritingStatus.DRAFT) {
			recipeNewDto.setWritingStatus(status);
		}
		
		try { 
			recipeService.saveRecipe(recipeNewDto, recipeImgFile , recipeingreMaterialList, 
					recipeingreNameList , recipeOrderContentList , recipeOrderImgFile ,principal );
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
	
	//레시피 수정화면
	@GetMapping(value = "/myPage/recipe/modify/{recipeId}")
	public String recipeDtl(@PathVariable("recipeId") Long recipeId, Model model){	
		
		
		try {
			RecipeNewDto recipeNewDto = recipeService.getRecipeDtl(recipeId);
			model.addAttribute("recipeNewDto" , recipeNewDto);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("errorMessage", "레시피 정보를 가져오는중 에러가 발생했습니다");
			model.addAttribute("recipeNewDto", new RecipeNewDto());
			
			return "main";
		}
		
		
		
		return "recipe/modify";
	}
	
	//레시피 수정기능
	@PostMapping(value = "/myPage/recipe/modify/{recipeId}")
	public String recipeUpdate(@RequestParam("recipeImgFile") MultipartFile recipeImgFile ,  @Valid RecipeNewDto recipeNewDto ,  
			BindingResult bindingResult , Model model, 
			@RequestParam("recipeingreName") List<String> recipeingreNameList ,
			@RequestParam("recipeingreMaterial") List<String> recipeingreMaterialList,
			@RequestParam("recipeOrderContent") List<String> recipeOrderContentList,
			@RequestParam("recipeOrderImgFile") List<MultipartFile> recipeOrderImgFile,
			@RequestParam("categoryEnum") String categoryEnumString,
			@RequestParam("writingStatus")String writingStatus) {
		
			CategoryEnum categoryEnum = CategoryEnum.fromString(categoryEnumString);
			recipeNewDto.setCategoryEnum(categoryEnum);
			
			WritingStatus status = WritingStatus.valueOf(writingStatus);
	
			if (status == WritingStatus.PUBLISHED) {
				recipeNewDto.setWritingStatus(status);
			} else if(status == WritingStatus.DRAFT) {
				recipeNewDto.setWritingStatus(status);
			}
			
			
			try { 
				recipeService.updateRecipe(recipeNewDto, recipeImgFile , recipeingreMaterialList, 
						recipeingreNameList , recipeOrderContentList , recipeOrderImgFile);
			} catch (Exception e) {			
				e.printStackTrace();
			}
			return "redirect:/";
	}
	
	
}