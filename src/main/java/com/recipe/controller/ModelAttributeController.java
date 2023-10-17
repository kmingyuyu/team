package com.recipe.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.recipe.entity.Member;
import com.recipe.service.CountService;
import com.recipe.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ModelAttributeController {

	private final MemberService memberService;
	
	private final CountService countService;
	

//	회원 정보/팔로우/팔로잉/찜/포인트 : 전역변수
	@ModelAttribute
	public void headerMemberInfo(Model model, HttpSession session) {

//		   비로그인 상태면 리턴
		if (!isAuthenticated()) {
			return;
		}

		Long id = (Long) session.getAttribute("memberId");

		Member member = memberService.findMember(id);
		
		Long cartCount = countService.cartCount(id);
		Long follower = countService.countFromMember(id);
		Long following = countService.countToMember(id);
		
		model.addAttribute("member", member);
		
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("follower", follower);
		model.addAttribute("following", following);
		

	}
	
	

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return authentication.isAuthenticated();
	}
}
