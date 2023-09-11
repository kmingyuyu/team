package com.recipe.dto;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.constant.ImgMainOk;
import com.recipe.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {
	
	private Long id;
	
	private String imgUrl;
	
	private String imgName;
	
	private String imgOriName;
	
	private ImgMainOk imgMainOk;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ItemImgDto of(ItemImg itemImg) {
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
	
	@QueryProjection
	public ItemImgDto(Long id, String imgUrl , String imgName , String imgOriName , ImgMainOk imgMainOk) {
		this.id = id;
		this.imgUrl = imgUrl;
		this.imgName = imgName;
		this.imgOriName = imgOriName;
		this.imgMainOk = imgMainOk;
	}
	
	
}
