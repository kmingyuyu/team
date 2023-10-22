package com.recipe.myPage.controller;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.ItemInqHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.service.CountService;
import com.recipe.service.GlobalService;
import com.recipe.service.MyPageItemInqService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageItemInqController {
	
	private final MyPageItemInqService myPageItemInqService;
	
	private final CountService countService;
	
	private final GlobalService globalService;
	
	
//	마이페이지 보여주기 (나의 상품 문의)
	@GetMapping(value = {"/myPage/itemInq" , "/myPage/itemInq/{page}" })
	public String myPageItemInq(@PathVariable("page") Optional<Integer> page ,HttpSession session, Model model , MyPageSerchDto myPageSerchDto) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Pageable Pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		
		Page<ItemInqHistoryDto> itemInqList = myPageItemInqService.findByMyItemInqList(Pageable, memberId, myPageSerchDto);
		
		Map<String, Long> countMap = countService.myPageItemInqAnswerOkCount(memberId);
		
		model.addAttribute("itemInqList", itemInqList);
		model.addAttribute("myPageSerchDto", myPageSerchDto);
		model.addAttribute("countMap", countMap);
		model.addAttribute("maxPage", 5); 
		
		return "myPage/itemReviewAndInq/myItemInq";
	}
	
//	마이페이지 나의 상품 문의글 수정팝업 (나의 상품 문의 -> 문의 수정(팝업)
	@GetMapping("/myPage/itemInq/itemInqPopupModi/{itemInqId}")
	public String ItemInqPopup(@PathVariable("itemInqId") Long itemInqId, HttpSession session , Model model) {
		
		if(!globalService.isAuthenticated()){
			model.addAttribute("loginNo");
		}
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
			Map<String, Object> itemInqMap = myPageItemInqService.findByMyItemInq(itemInqId, memberId);
			
			model.addAttribute("itemInq" , itemInqMap.get("itemInq"));
			model.addAttribute("item" , itemInqMap.get("item"));
			model.addAttribute("itemImg" , itemInqMap.get("itemImg"));
			
		} catch (CustomException e) {
			model.addAttribute("errorMessage" , "문의글 수정이 불가합니다. 잠시후에 시도해주세요.\n" + e.getMessage());
			
		}
		

		return "myPage/itemReviewAndInq/ItemInqPopupModi";
	}
	
//	마이페이지 나의 상품 문의글 수정팝업 수정하기 (나의 상품 문의 -> 문의 수정(팝업)
	@PatchMapping("/myPage/itemInq/itemInqPopupModi/modiOk")
	public @ResponseBody ResponseEntity<String> itemInqModi(@RequestBody Map<String, Object> requestBody,HttpSession session){
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
			myPageItemInqService.itemInqModi(requestBody, memberId);
		} catch (CustomException e) {
			return new ResponseEntity<>("문의글 수정에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>("문의글 수정 되었습니다. ", HttpStatus.OK);
	}
	
	
	
//	마이페이지 나의 상품 문의글 삭제 (나의 상품 문의)
	@DeleteMapping("/myPage/itemInq/itemInqDelete")
	public @ResponseBody ResponseEntity<String> itemInqDelete(@RequestBody Map<String, Object> requestBody ,HttpSession session) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
			myPageItemInqService.itemInqDelete(requestBody, memberId);
			
		} catch (CustomException e) {
			return new ResponseEntity<>("문의글 삭제에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>("문의글 삭제 되었습니다.", HttpStatus.OK);
	}
	
	
	
	
	
	
	
}
