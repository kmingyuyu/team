package com.recipe.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.entity.ItemReviewAnswer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReviewAnswerDto {

	private Long id;

	private String content;

	private static ModelMapper modelMapper = new ModelMapper();

	public ItemReviewAnswer createReviewAnswer() {
		return modelMapper.map(this, ItemReviewAnswer.class);
	}
}
