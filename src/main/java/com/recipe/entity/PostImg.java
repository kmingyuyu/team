package com.recipe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "post_img")
@Getter
@Setter
@ToString
public class PostImg extends BaseEntity {

	@Id
	@Column(name = "post_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String imgName;

	private String oriImgName; // 원본 이미지 파일명

	private String imgUrl; // 이미지 조회 경로

	private String repimgYn; // 대표 이미지 여부

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post")
	private Post post;

	// 이미지에 대한 정보를 업데이트 하는 메소드
	public void updateAttFile(String oriImgName, String imgName, String imgUrl) {
		this.imgName = imgName;
		this.oriImgName = oriImgName;
		this.imgUrl = imgUrl;
	}
}
