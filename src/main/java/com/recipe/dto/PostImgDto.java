package com.recipe.dto;

import org.modelmapper.ModelMapper;

import com.recipe.entity.PostImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostImgDto {

	private Long id;

	private String imgName;

	private String imgUrl;

	private static ModelMapper modelMapper = new ModelMapper();

	// entity -> dto로 변환
	public static PostImgDto of(PostImg attFile) {
		return modelMapper.map(attFile, PostImgDto.class);
	}
}
