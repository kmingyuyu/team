package com.recipe.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//인증되지 않은 사용자가 리소를 요청할 경우 어떻게 처리할지에 대한 클래스
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	  @Override
	    public void commence(HttpServletRequest request, HttpServletResponse response,
	                         AuthenticationException authException) throws IOException, ServletException {
		  
	        if (request.getRequestURI().contains("/myPage") || request.getRequestURI().contains("/cart") || request.getRequestURI().contains("/order")) {
	            response.sendRedirect("/members/login");  // 로그인 페이지로 리다이렉트
	        } else {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	        }
	    }
}