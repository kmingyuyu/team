package com.recipe.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.recipe.entity.Member;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private final MemberRepository memberRepository;
	 
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	        Authentication authentication) throws IOException, ServletException {
	    
	    HttpSession session = request.getSession();
	    
	    if (session != null) {
	        Object principal = authentication.getPrincipal();
	        
	        
	        if (principal instanceof org.springframework.security.core.userdetails.User) {  
	            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
	            
	            
	            Member member = memberRepository.findByEmail(username);
	            
	            if (member != null) {
	            	
	                Long memberId = member.getId();
	                String email = member.getEmail();
	                String role = member.getRole().toString();
	                
	                session.setAttribute("memberId", memberId);
	                session.setAttribute("email", email);
	                session.setAttribute("role", role);
	            }
	        }
	    }
	    
	    response.sendRedirect("/");
	}
	
	
	        
	}

	
	
	

