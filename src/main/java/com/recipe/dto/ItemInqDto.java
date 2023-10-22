package com.recipe.dto;

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
public class ItemInqDto {
	
	private Long inqId;
	
	private String title;
	
	private String content;
	
	private ItemInqBoardEnum itemInqBoardEnum ;
	
	private ItemInqEnum itemInqEnum;
	
	private AnswerOk answerOk;
	
	private LocalDateTime regTime;
	
	private String email;
	
	private String nickname;
	
	private ItemInqAnwser itemInqAnwser;
	
	@QueryProjection
	public ItemInqDto(Long inqId,Long itemId, String title , String content , ItemInqBoardEnum itemInqBoardEnum , 
			ItemInqEnum itemInqEnum , AnswerOk answerOk , LocalDateTime regTime , String email , String nickname) {
		
		this.inqId = inqId;
		this.title = title;
		this.content = content;
		this.itemInqBoardEnum = itemInqBoardEnum;
		this.itemInqEnum = itemInqEnum;
		this.answerOk = answerOk;
		this.regTime = regTime;
		this.email = email;
		this.nickname = nickname;
	}
}
