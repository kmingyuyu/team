package com.recipe.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.PostReplyStatus;
import com.recipe.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
	private Long id;

	private String nickname;

	private String email;

	private String title;

	private String content;

	private String imgUrl; // 이미지 조회 경로

	private LocalDateTime regTime; // 등록날짜

	private String formattedRegTime; // 보기 좋은 형식으로 변환된 등록날짜

	private LocalDateTime updateTime;

	private int count;

	private PostReplyStatus postReplyStatus;

	public PostDto() {
		// 초기화 작업 등을 수행할 수 있습니다.
	}

	private boolean hasAnswer;

	// 답변이 있는지 여부를 판단하여 hasAnswer 속성을 설정
	public void setHasAnswer(boolean hasAnswer) {
		this.hasAnswer = hasAnswer;
	}

	@QueryProjection
	public PostDto(Long id, String nickname, String email, String title, String content, LocalDateTime regTime,
			LocalDateTime updateTime, PostReplyStatus postReplyStatus) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.title = title;
		this.content = content;
		this.regTime = regTime;
		this.updateTime = updateTime;
		this.postReplyStatus = postReplyStatus;

		// 등록날짜를 보기 좋은 형식으로 변환하여 formattedRegTime에 저장
		if (regTime != null) {
			this.regTime = regTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			this.formattedRegTime = regTime.format(formatter);
		}

	}

	// 게시물 이미지 정보를 저장
	private List<PostImgDto> postImgDtoList = new ArrayList<>();

	// 게시물 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도
	private List<Long> postImgIds = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	// dto -> entity로 바꿈
	public Post createPost() {
		return modelMapper.map(this, Post.class);
	}

	// entity -> dto로 바꿈
	public static PostDto of(Post post) {
		return modelMapper.map(post, PostDto.class);
	}
}
