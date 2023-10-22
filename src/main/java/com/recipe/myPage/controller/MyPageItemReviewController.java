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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.ItemReviewHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.service.CountService;
import com.recipe.service.GlobalService;
import com.recipe.service.MyPageItemReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageItemReviewController {
	
	private final CountService countService;
	
	private final MyPageItemReviewService myPageItemReviewService;
	
	private final GlobalService globalService;
	
// 	마이페이지 보여주기 (나의 상품 리뷰)
	@GetMapping(value = {"/myPage/itemReview" , "/myPage/itemReview/{page}" })
	public String myPageItemReview(@PathVariable("page") Optional<Integer> page ,HttpSession session, Model model , MyPageSerchDto myPageSerchDto) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Pageable Pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		
		Page<ItemReviewHistoryDto> reviewList = myPageItemReviewService.findByMyItemReviewList(memberId, Pageable, myPageSerchDto);
		
		Map<String, Long> countMap = countService.myPageItemReviewStatusCount(memberId);
		
		model.addAttribute("reviewList",reviewList);
		model.addAttribute("myPageSerchDto", myPageSerchDto);
		model.addAttribute("countMap", countMap);
		model.addAttribute("maxPage", 5); 
		
		return "myPage/itemReviewAndInq/myItemReview";
	}
	
//	마이 페이지 주문상품 리뷰 삭제 (나의 상품 리뷰)
	@DeleteMapping("/myPage/itemReview/itemReviewDelete")
	public @ResponseBody ResponseEntity<String> itemReviewDelete(@RequestBody Map<String, Object> requestBody, HttpSession session) {
		
		try {
			
			myPageItemReviewService.itemReviewDelete(requestBody,  session);
			
		} catch (Exception e) {
			return new ResponseEntity<>("후기 삭제에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>("후기 삭제 되었습니다. ", HttpStatus.OK);
	}
	
// 	마이페이지 보여주기 (나의 상품 리뷰-> 수정(팝업))
	@GetMapping("/myPage/itemReview/itemReviewPopupModi/{itemReviewId}")
	public String itemReviewPopupModi(@PathVariable("itemReviewId") Long itemReviewId ,HttpSession session , Model model ) {
		
		if(!globalService.isAuthenticated()){
			model.addAttribute("loginNo");
		}
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
			
			Map<String, Object> itemReviewMap = myPageItemReviewService.findByMyItemReview(itemReviewId, memberId);
			
			model.addAttribute("itemReview" , itemReviewMap.get("itemReview"));
			model.addAttribute("item" , itemReviewMap.get("item"));
			model.addAttribute("itemImg" , itemReviewMap.get("itemImg"));
			model.addAttribute("itemReviewImgList" , itemReviewMap.get("itemReviewImgList"));
			
		} catch (CustomException e) {
			model.addAttribute("errorMessage" , "후기 수정이 불가합니다. 잠시후에 시도해주세요.\n" + e.getMessage());
			
		}
		
		return "myPage/itemReviewAndInq/itemReviewPopupModi";
	}
	
//	마이페이지 주문상품 리뷰수정 (나의 상품 리뷰 - > 상품 리뷰(팝업))
	@PostMapping("/myPage/itemReview/itemReviewPopupModi/modiOk")
	public @ResponseBody ResponseEntity<String> orderItemReviewModi(
			HttpSession session ,
			@RequestParam("itemReviewId") Long itemReviewId,
			@RequestParam(value="imgFiles", required=false) MultipartFile[] files,
			@RequestParam(value="oriImgDeleteNames", required=false) String[] oriImgDeleteNames,
			@RequestParam(value="oriImgNames", required=false) String[] oriImgNames,
			@RequestParam("star") double star, 
			@RequestParam("content") String content) {
		
		if(!globalService.isAuthenticated()) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		try {
			myPageItemReviewService.orderItemReviewModi(files, oriImgDeleteNames, oriImgNames, star, content, itemReviewId, session);
			
		} catch (Exception e) {
			return new ResponseEntity<>("후기 수정에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<>("후기 수정 되었습니다. ", HttpStatus.OK);
		
	}
}
