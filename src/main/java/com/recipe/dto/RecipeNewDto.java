package com.recipe.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.recipe.constant.CategoryEnum;
import com.recipe.constant.WritingStatus;
import com.recipe.entity.Recipe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeNewDto {
	
	private Long id;
	
	private String title; // 제목

	private String subTitle; // 부제목
	
	private String intro; //레시피 소개

	private String durTime; //소요 시간

	private String level;//난이도
	
	private CategoryEnum categoryEnum; //카테고리

	private String imageUrl; //메인이미지

	private String serving; //몇인분
	
	private WritingStatus writingStatus;
	
	private int count=0; //조회수
	
//	재료 정보 저장
	private List<RecipeIngreDto> recipeIngreDtoList = new ArrayList<>();
	
//	조리 순서 저장
	private List<RecipeOrderDto> recipeOrderDtoList = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
//	dto -> entity 변환
	public Recipe createRecipe() {
		return modelMapper.map(this, Recipe.class);
	}
	
//	entity -> dto 변환
	public static RecipeNewDto of(Recipe recipe) {
		return modelMapper.map(recipe, RecipeNewDto.class);
	}

	
}