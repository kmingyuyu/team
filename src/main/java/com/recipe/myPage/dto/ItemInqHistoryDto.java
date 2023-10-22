package com.recipe.myPage.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.AnswerOk;
import com.recipe.constant.ItemInqBoardEnum;
import com.recipe.constant.ItemInqEnum;
import com.recipe.entity.ItemInqAnwser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInqHistoryDto {
	
	private Long itemInqId;
	
	private Long itemId;
	
	private String title;
	
	private String content;
	
	private ItemInqBoardEnum itemInqBoardEnum ;
	
	private ItemInqEnum itemInqEnum;
	
	private AnswerOk answerOk;
	
	private LocalDateTime regTime;
	
	private String itemNm;
	
	private String imgUrl;
	
	private ItemInqAnwser itemInqAnwser;
	
	@QueryProjection
	public ItemInqHistoryDto(Long itemInqId,Long itemId, String title , String content , ItemInqBoardEnum itemInqBoardEnum , 
			ItemInqEnum itemInqEnum , AnswerOk answerOk , LocalDateTime regTime , String itemNm , String imgUrl) {
		
		this.itemInqId = itemInqId;
		this.itemId = itemId;
		this.title = title;
		this.content = content;
		this.itemInqBoardEnum = itemInqBoardEnum;
		this.itemInqEnum = itemInqEnum;
		this.answerOk = answerOk;
		this.regTime = regTime;
		this.itemNm = itemNm;
		this.imgUrl = imgUrl;
	}
}
