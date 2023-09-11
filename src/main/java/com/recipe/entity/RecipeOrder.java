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
@Table(name="recipe_order")
@Getter
@Setter
@ToString
public class RecipeOrder {
	
	@Id
	@Column(name="recipe_order_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; //아이디
	
	private int orderNumber; //순서번호
	
	private String content; //상세내용
	
	private String imgUrl; //이미지 url
	
	private String ImgName; //이미지 이름 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id")
	private Recipe recipe;
	
	
	public void updateRecipeOrderImg(String imgUrl , String ImgName) {
			this.imgUrl = imgUrl;
			this.ImgName = ImgName;
	}
	
	
}
