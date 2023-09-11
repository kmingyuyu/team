package com.recipe.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.AnswerOk;
import com.recipe.constant.ItemInqBoardEnum;
import com.recipe.constant.ItemInqEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInqDto {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private ItemInqBoardEnum itemInqBoardEnum ;
	
	private ItemInqEnum itemInqEnum;
	
	private AnswerOk answerOk;
	
	private LocalDateTime regTime;
	
	private String email;
	
	private String nickname;
	
	private Long answerId;
	
	private String answerContent;
	
	private LocalDateTime answerRegTime;
	
	@QueryProjection
	public ItemInqDto(Long id, String title , String content , ItemInqBoardEnum itemInqBoardEnum , 
			ItemInqEnum itemInqEnum , AnswerOk answerOk , LocalDateTime regTime , String email , String nickname ,
			Long answerId ,String answerContent , LocalDateTime answerRegTime ) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.itemInqBoardEnum = itemInqBoardEnum;
		this.itemInqEnum = itemInqEnum;
		this.answerOk = answerOk;
		this.regTime = regTime;
		this.email = email;
		this.nickname = nickname;
		this.answerId = answerId;
		this.answerContent = answerContent;
		this.answerRegTime = answerRegTime;
	}
	
}
