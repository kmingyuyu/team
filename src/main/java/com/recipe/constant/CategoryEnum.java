package com.recipe.constant;

public enum CategoryEnum {
	메인요리, 밑반찬, 간식, 간단요리, 초대요리;
	
	 public static CategoryEnum fromString(String value) {
	        for (CategoryEnum category : CategoryEnum.values()) {
	            if (category.toString().equals(value)) {
	                return category;
	            }
	        }
	        throw new IllegalArgumentException("에러");
	    }
}