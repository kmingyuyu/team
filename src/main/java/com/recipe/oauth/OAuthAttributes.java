package com.recipe.oauth;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OAuthAttributes {
	private Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
	 private String nameAttributesKey;
	 private String name;
	 private String email;
	 
	 @Builder
	    public OAuthAttributes(Map<String, Object> attributes, String nameAttributesKey,
	                           String name, String email) {
	        this.attributes = attributes;
	        this.nameAttributesKey = nameAttributesKey;
	        this.name = name;
	        this.email = email;
	    }
	 
}