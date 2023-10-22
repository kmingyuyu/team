package com.recipe.myPage.global.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.recipe.service.CountService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice(basePackages = "com.recipe.myPage")
@RequiredArgsConstructor
public class MyPageGlobalController {
	
	private final CountService countService;
	
	@ModelAttribute("commonAttribute")
	public void findMyFollowAndBookMark(Model model , HttpSession session) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Long follower = countService.countToMember(memberId);
		
		Long following = countService.countFromMember(memberId);
		
		Long bookMarkCount = countService.bookMarkCount(memberId);
		
		model.addAttribute("follower",follower);
		model.addAttribute("following",following);
		model.addAttribute("bookMarkCount",bookMarkCount);
		
		
	}
	
	
}
