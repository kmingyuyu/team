package com.recipe.oauth;

import java.util.Map;

public class KaKaoUserInfo implements Oauth2UserInfo{
	
	private Map<String, Object> attributes;
    private Map<String, Object> attributesAccount;
    private Map<String, Object> attributesProfile;
    
    public KaKaoUserInfo(Map<String, Object> attributes) {
        
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	} 

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getEmail() {
    	
    	String email = attributesAccount.get("email").toString();
    	
    	if(attributesAccount.get("email")==null) {
    		email = "email_disagree";
    	}
    	
        return email;
    }

    @Override
    public String getName() {
        return attributesProfile.get("nickname").toString();
    }
    
}