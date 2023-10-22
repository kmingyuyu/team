package com.recipe.myPage.point.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.recipe.entity.Point;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.myPage.dto.PointHistoryDto;
import com.recipe.service.CountService;
import com.recipe.service.MyPagePointService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPagePointController {
	
	private final CountService countService;
	
	private final MyPagePointService myPagePointService;
	
	// 마이페이지 보여주기 (주문내역)
		@GetMapping(value = {"/myPage/point" , "/myPage/point/{page}" })
		public String myPagePoint(@PathVariable("page") Optional<Integer> page ,HttpSession session, Model model , MyPageSerchDto myPageSerchDto) {
			
			Long memberId = (Long) session.getAttribute("memberId");
			
			Pageable Pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
			
			Page<PointHistoryDto> pointList = myPagePointService.findByMyPointList(memberId, Pageable, myPageSerchDto);
		
			Map<String, Long> countMap = countService.myPagePointEnumCount(memberId);
			
			model.addAttribute("pointList", pointList);
			model.addAttribute("myPageSerchDto", myPageSerchDto);
			model.addAttribute("countMap", countMap);
			model.addAttribute("maxPage", 5);
			
			return "myPage/benefit/MyPoint";
		}

}
