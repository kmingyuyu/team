package com.recipe.myPage.follow.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.FollowHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.service.MyPageFollowService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageFollowController {
	
	private final MyPageFollowService myPageFollowService;
	
// 	마이페이지 보여주기 (팔로워)
	@GetMapping(value = {"/myPage/follow" , "/myPage/follow/{page}" })
	public String myPageFollower(@PathVariable("page") Optional<Integer> page ,HttpSession session, Model model , MyPageSerchDto myPageSerchDto) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Pageable Pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20);
		
		
		Page<FollowHistoryDto> followList = myPageFollowService.findByMyFollowList(memberId, Pageable, myPageSerchDto);
			
		
		model.addAttribute("followList", followList);
		model.addAttribute("myPageSerchDto", myPageSerchDto);
		model.addAttribute("maxPage", 5); 
		
		return "myPage/MemberMng/myFollow";
	}
	
	@PostMapping("/myPage/follow/followReg")
	public @ResponseBody ResponseEntity<String> followReg(@RequestBody Map<String, Object> requestBody,HttpSession session){
		
		try {
			myPageFollowService.followReg(requestBody, session);
			
		} catch (CustomException e) {
			log.error("팔로우 등록 에러",e);
			return new ResponseEntity<>("팔로우 등록에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>("팔로우 등록 되었습니다. ", HttpStatus.OK);
	}
	
	
	@DeleteMapping("/myPage/follow/followDelete")
	public @ResponseBody ResponseEntity<String> followDelete(@RequestBody Map<String, Object> requestBody,HttpSession session){
		
		try {
			myPageFollowService.followDelete(requestBody, session);
			
		} catch (CustomException e) {
			log.error("팔로우 취소 에러",e);
			return new ResponseEntity<>("팔로우 취소에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>("팔로우 취소 되었습니다. ", HttpStatus.OK);
	}
		
	
	
}
