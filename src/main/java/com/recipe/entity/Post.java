package com.recipe.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.recipe.constant.PostReplyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="post")
@Getter
@Setter
@ToString
public class Post extends BaseTimeEntity {

	@Id
	@Column(name="post_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;

	private String content;
	
	@Enumerated(EnumType.STRING) // enum의 이름을 DB의 저장
	private PostReplyStatus postReplyStatus; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
	
}
