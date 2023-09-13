package com.recipe.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.PostReplyStatus;
import com.recipe.entity.Post;
import com.recipe.entity.PostResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

	private Long id;

	private String content;

	private LocalDateTime regTime;

	private LocalDateTime updateTime;

	private String formattedRegTime;

	@QueryProjection
	public PostResponseDto(Long id, String content, LocalDateTime regTime, LocalDateTime updateTime) {
		this.id = id;
		this.content = content;
		this.regTime = regTime;
		this.updateTime = updateTime;

		// 등록날짜를 보기 좋은 형식으로 변환하여 formattedRegTime에 저장
		if (regTime != null) {
			this.regTime = regTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			this.formattedRegTime = regTime.format(formatter);
		}

	}

	// 답글 이미지 정보를 저장
	private List<PostImgDto> postImgDtoList = new ArrayList<>();

	// 답글 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도
	private List<Long> postImgIds = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	// dto -> entity로 바꿈
	public PostResponse createPostResponse() {
		return modelMapper.map(this, PostResponse.class);
	}

	// entity -> dto로 바꿈
	public static PostResponseDto of(PostResponse postResponse) {
		return modelMapper.map(postResponse, PostResponseDto.class);
	}

}
