package com.recipe.dto;

import java.util.Date;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.entity.Recipe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MngRecipeDto {
	private Long id;

	private String title;

	private String subTitle;

	private String intro;

	private Date durTime;

	private int level;

	private int count;

	private Long commentCount;

	private String nickname;

	private String imageUrl;
	
	private double reting; 

	private static ModelMapper modelMapper = new ModelMapper();

	// dto -> entity로 바꿈
	public Recipe createRecipe() {
		return modelMapper.map(this, Recipe.class);
	}

	// entity -> dto로 바꿈
	public static MngRecipeDto of(Recipe recipe) {
		return modelMapper.map(recipe, MngRecipeDto.class);
	}

	@QueryProjection
	public MngRecipeDto(Long id, String title ,String intro ,  String nickname, Long commentCount, String imageUrl, double reting) {
		this.id = id;
		this.title = title;
		this.intro = intro;
		this.nickname = nickname;
		this.commentCount = commentCount;
		this.imageUrl = imageUrl;
		this.reting = reting;
	}

}
