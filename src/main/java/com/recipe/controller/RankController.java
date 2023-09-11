package com.recipe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.recipe.dto.MemberBestDto;
import com.recipe.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RankController {
	
	private final MemberService memberService;
	
	@GetMapping(value= {"/rank"})
	public String ranking(Model model) {
		
		List<MemberBestDto> rankMemberList = memberService.getRankMemberList();
		
		for(MemberBestDto t : rankMemberList) {
			System.out.println("id" + t.getId());
			System.out.println("팔로우수" + t.getFollowCount());
			System.out.println("닉넴" + t.getNickname());
			System.out.println("회원사진" + t.getImgUrl());
		}
		
		model.addAttribute("rankMemberList",rankMemberList);
		
		
		return "ranking";
	}
}
