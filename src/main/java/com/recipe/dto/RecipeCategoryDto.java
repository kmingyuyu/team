package com.recipe.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.CategoryEnum;
import com.recipe.constant.ImgMainOk;

import groovy.transform.builder.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeCategoryDto {

	/*
	 * Long getMemberId(); // 레시피 작성한 멤버 id
	 * 
	 * Long getRecipeId(); // 레시피 id
	 * 
	 * int getNum(); // 순차번호
	 * 
	 * int getBookmarkCount(); // 레시피에 북마크 수
	 * 
	 * int getCount(); // 레시피 조회수
	 * 
	 * String getDurTime(); //레시피 소요시간
	 * 
	 * String getLevel(); //레시피 난이도
	 * 
	 * String getTitle(); //레시피 제목
	 * 
	 * String getSubTitle(); //레시피 부제목
	 * 
	 * String getIntro(); //레시피 간단소개
	 * 
	 * String getImageUrl(); //레시피 메인사진
	 * 
	 * LocalDateTime getRegTime(); //레시피 작성시간
	 * 
	 * String getNickname(); // 레시피 등록한 멤버 닉네임
	 * 
	 * String getMemberImg(); //레시피 등록한 멤버 메인사진
	 * 
	 * String getImgMainOk(); //레시피 메인 여부 Y OR NONE
	 * 
	 * String getCategoryEnum(); // 레시피의 카테고리 명
	 * 
	 * int getReviewCount(); // 레시피에 등록된 리뷰 갯수
	 * 
	 * double getRetingAvg(); //레시피에 등록된 리뷰 평점
	 */

	private Long id;
	private int count;
	private String durTime;
	private String imageUrl;
	private String level;
	private String subTitle;
	private String title;
	private Long memberId;
	private LocalDateTime regTime;
	private String intro;
	private String nickname;
	private String imgUrl;
	private ImgMainOk imgMainOk;
	private CategoryEnum categoryEnum;
	private Long reviewCount;
	private double retingAvg;
	private Long bookmarkCount;
	
	@QueryProjection
	public RecipeCategoryDto(Long id, int count, String durTime, String imageUrl, String level, String subTitle,
			String title, Long memberId, LocalDateTime regTime, String intro, String nickname, String imgUrl,
			ImgMainOk imgMainOk, CategoryEnum categoryEnum , Long reviewCount, double retingAvg) {
		
		
		this.id = id;
		this.count = count;
		this.durTime = durTime;
		this.imageUrl = imageUrl;
		this.level = level;
		this.subTitle = subTitle;
		this.title = title;
		this.memberId = memberId;
		this.regTime = regTime;
		this.intro = intro;
		this.nickname = nickname;
		this.imgUrl = imgUrl;
		this.imgMainOk = imgMainOk;
		this.categoryEnum = categoryEnum;
		this.reviewCount = reviewCount;
		this.retingAvg = retingAvg;
	
	}
	

	
}
