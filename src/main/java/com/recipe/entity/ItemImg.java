package com.recipe.entity;


import com.recipe.constant.ImgMainOk;

import groovy.transform.ToString;
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

@Getter
@Setter
@ToString
@Entity
@Table(name="item_Img")
public class ItemImg {
	
	@Id
	@Column(name="item_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgUrl;
	
	private String imgName;
	
	private String imgOriName;
	
	@Enumerated(EnumType.STRING)
	private ImgMainOk imgMainOk; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	public void updateItemImg(String imgOriName, String imgName, String imgUrl) {
		this.imgOriName = imgOriName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
	
	
}
