package com.recipe.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDetailImgDto {
	
		private Long id;
		
		private String imgUrl;
		
		private String imgName;
		
		private String imgOriName;
		
		@QueryProjection
		public ItemDetailImgDto (Long id ,String imgUrl,String imgName,String imgOriName ) {
			this.id = id;
			this.imgUrl = imgUrl;
			this.imgName = imgName;
			this.imgOriName = imgOriName;
		}
		
}
