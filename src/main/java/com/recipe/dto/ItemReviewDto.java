package com.recipe.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.entity.ItemReview;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReviewDto {
	
	private Long id;
	
	private double reting;
	
	private String content;
	
	private LocalDateTime regTime;
	
	private String nickname;
	
	private String imgUrl;
	
	private Long answerId;
	
	private String answerContent;
	
	private LocalDateTime answerRegTime;
	
	List<ItemReviewAnswerDto> itemReviewAnswerList = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
//	dto -> entity 변환
	public ItemReview createReview() {
		return modelMapper.map(this, ItemReview.class);
	}
	
//	entity -> dto 변환
	public static ItemReviewDto of(ItemReview itemReview) {
		return modelMapper.map(itemReview, ItemReviewDto.class);
	}
	
	@QueryProjection
	public ItemReviewDto(Long id ,double reting , String content ,LocalDateTime regTime , String nickname 
			, String imgUrl, Long answerId , String answerContent , LocalDateTime answerRegTime) {
		this.id = id;
		this.reting = reting;
		this.content = content;
		this.regTime = regTime;
		this.nickname = nickname;
		this.imgUrl = imgUrl;
		this.answerId = answerId;
		this.answerContent = answerContent;
		this.answerRegTime = answerRegTime;
		
	}
	
	
}
