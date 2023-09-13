package com.recipe.dto;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.entity.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

	private Long id;

	private String nickname;

	private String email;

	private String title;

	private String writer;

	private String commentContent;

	@QueryProjection
	public CommentDto(Long id, String nickname, String email, String title, String writer, String commentContent) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.title = title;
		this.writer = writer;
		this.commentContent = commentContent;
	}

	private static ModelMapper modelMapper = new ModelMapper();

	// dto -> entity로 바꿈
	public Comment createComment() {
		return modelMapper.map(this, Comment.class);
	}

	// entity -> dto로 바꿈
	public static CommentDto of(Comment comment) {
		return modelMapper.map(comment, CommentDto.class);
	}
}
