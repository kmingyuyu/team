package com.recipe.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.recipe.entity.Member;
import com.recipe.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class HeaderController {

	private final MemberService memberService;

//		헤더에 들어갈 장바구니 담긴 횃수 / 회원 정보 : 전역변수
	@ModelAttribute
	public void headerMemberInfo(Model model, HttpSession session, HttpServletRequest request) {

//		   비로그인 상태면 리턴
		if (!isAuthenticated()) {
			return;
		}

		Long id = (Long) session.getAttribute("memberId");

		Member member = memberService.findMember(id);
		String email = member.getEmail();
		String nickname = member.getNickname();
		String imgUrl = member.getImgUrl();
		
		Long cartCount = memberService.cartCount(id);
		int point = memberService.findPoint(id);
		
		model.addAttribute("imgUrl", imgUrl);
		model.addAttribute("email", email);
		model.addAttribute("nickname", nickname);
		model.addAttribute("point", point);
		model.addAttribute("cartCount", cartCount);

	}

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return authentication.isAuthenticated();
	}
}
