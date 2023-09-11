package com.recipe.handler;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.recipe.dto.SocialMemberDto;
import com.recipe.entity.Member;
import com.recipe.oauth.PrincipalDetails;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
		@Autowired
	  private MemberRepository memberRepository;

		
	    //간편 로그인 성공시 정보를 sns전용 회원가입 페이지로 전달
	    @Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			
	    	   System.out.println("핸들러성공");

	    	    if (authentication instanceof OAuth2AuthenticationToken) {
	    	        OAuth2User oauthUser = ((OAuth2AuthenticationToken) authentication).getPrincipal();
	    	        if (oauthUser instanceof PrincipalDetails) {
	    	            PrincipalDetails userDetails = (PrincipalDetails) oauthUser;
	    	            String email = userDetails.getEmail(); // Use getEmail from PrincipalDetails
	    	            String password = userDetails.getPassword();
	    	            String provider = userDetails.getProvider();
	    	            String providerId = userDetails.getProviderId();
	    	            String name = userDetails.getUsername();
	    	            
	    	            Member member = memberRepository.findByEmail(email);
	    	            SocialMemberDto socialMemberDto = new SocialMemberDto();
	    	            //name password 추가
	    	            //dto에 provide 등 추가
	    	            socialMemberDto.setEmail(email);
	    	            //socialMemberDto.setPassword(password);
//	    	            socialMemberDto.setPasswordConfirm(password);
	    	            socialMemberDto.setProvider(provider);
	    	            socialMemberDto.setProviderId(providerId);
	    	            socialMemberDto.setName(name);
	                    
	    	            if (member == null) {
	    	                // 간편 로그인 성공시 추가 정보를 받기위해
	    	            	
	    	            	String redirectUrl = "/members/snsMember?email=" + URLEncoder.encode(socialMemberDto.getEmail(), "UTF-8") 
	    	            	+ "&provider=" + URLEncoder.encode(socialMemberDto.getProvider(), "UTF-8" )
	    	            	+ "&providerId=" + URLEncoder.encode(socialMemberDto.getProviderId(), "UTF-8")
	    	            	+ "&name=" + URLEncoder.encode(socialMemberDto.getName(), "UTF-8");
	    	            	//+ "&password=" + URLEncoder.encode(socialMemberDto.getPassword(), "UTF-8");
	    	            			
	    	            	response.sendRedirect(redirectUrl);
	    	            } else {
	    	                // 실패시
	    	                response.sendRedirect("/"); 
	    	            }
	    	        }

	    	    }
	    }
}