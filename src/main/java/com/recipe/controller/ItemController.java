package com.recipe.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.dto.ItemCategoryDto;
import com.recipe.dto.ItemDetailDto;
import com.recipe.dto.ItemDetailImgDto;
import com.recipe.dto.ItemImgDto;
import com.recipe.dto.ItemInqDto;
import com.recipe.dto.ItemReviewDto;
import com.recipe.dto.ItemReviewImgDto;
import com.recipe.dto.ItemSearchDto;
import com.recipe.service.ItemService;
import com.recipe.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;
	
	private final MemberService memberService;
	
//	상품페이지
	@RequestMapping(value= {"item/total", "item/total/{page}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public  String Item(ItemSearchDto itemSearchDto , @PathVariable("page") Optional<Integer> page ,
			Model model) {
		
		int pageNm = (itemSearchDto.getDataNum() == 0) ? 12 : itemSearchDto.getDataNum();
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , pageNm);
		
		Page<ItemCategoryDto> category = itemService.getItemCategoryList(pageable , itemSearchDto);
		
		int currentPage = page.isPresent() ? page.get() : 0;
		model.addAttribute("currentPage" , currentPage);
		
		System.out.println("currentPage" + currentPage);
		
		model.addAttribute("itemSearchDto" , itemSearchDto);
		model.addAttribute("category",category);
		model.addAttribute("maxPage" , 5);
		
		
		return "item";
	}
	
//	상품 상세페이지
	@GetMapping(value= {"/item/{ItemId}" })
	public   String ItemDetail( @PathVariable("ItemId") Long itemId ,
			Optional<Integer> reviewPage, Optional<Integer> inqPage, 
			Model model) {
		
		ItemDetailDto item = itemService.getItemDetailList(itemId);
		
		List<ItemImgDto> imgList =  item.getItemImgDtoList();
		
		List<ItemDetailImgDto> imgDetailList = item.getItemDetailImgDtoList();
		
		Pageable reviewPageable = PageRequest.of(reviewPage.isPresent() ? reviewPage.get() : 0 , 6);
		Page<ItemReviewDto> itemReviewList = itemService.getItemReviewList(reviewPageable, itemId);
		
		Pageable inqPageable = PageRequest.of(inqPage.isPresent() ? inqPage.get() : 0 , 12);
		Page<ItemInqDto> itemInqList = itemService.getItemInqList(inqPageable, itemId);
		
		List<ItemReviewImgDto> itemReviewImgList = new ArrayList<>();
		
		try {
			for(ItemReviewDto d : itemReviewList) {
				List<ItemReviewImgDto> test = itemService.getItemReviewImgList(d.getId());
				for(ItemReviewImgDto tt : test) {
					itemReviewImgList.add(tt);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("item" , item);
		model.addAttribute("imgList" , imgList);
		model.addAttribute("imgDetailList" , imgDetailList);
		model.addAttribute("itemReviewList" , itemReviewList);
		model.addAttribute("itemInqList" , itemInqList);
		model.addAttribute("itemReviewImgList" ,itemReviewImgList);
		model.addAttribute("maxPage" , 5);
		model.addAttribute("itemId" , itemId);
		
		
		
		
		String loginOk = "로그인";
		if(!isAuthenticated()) {
			 loginOk = "비로그인";
		}
		model.addAttribute("loginOk" , loginOk);
		
		
		
		
		
		
		return "itemDetail";
	}
	
//	상품문의 팝업창 
	@GetMapping("/popup/{ItemId}")
	public String popup(@PathVariable("ItemId") Long itemId , Model model) {
	
		
		
		
		
		
		ItemDetailDto item = itemService.getItemDetailList(itemId);
		
		List<ItemImgDto> imgList =  item.getItemImgDtoList();
		
		
		model.addAttribute("item" , item);
		model.addAttribute("imgList" , imgList);
		model.addAttribute("itemId" , itemId);
		
		return "popup";
	}
	
//	상품문의 등록
	@PostMapping(value="/inqReq")
	public @ResponseBody ResponseEntity inqReq(@RequestBody Map<String, Object> requestBody) {
		
		itemService.itemInqReg(requestBody);
		
		return new ResponseEntity<>("문의 접수 되었습니다" , HttpStatus.OK);
	}
	
//	리뷰 답변 등록
	@PostMapping(value="/answerReg")
	public @ResponseBody ResponseEntity reviewAnswerReg(@RequestBody Map<String, Object> requestBody) {
		
		 itemService.itemReviewAnswerReg(requestBody);
		
		return new ResponseEntity<>("답변 등록 되었습니다",HttpStatus.OK);
		
	} 
	
//	리뷰 답변 수정
	@PostMapping(value="/answerUpdate")
	public @ResponseBody ResponseEntity reviewAnswerUpdate(@RequestBody Map<String, Object> requestBody ) {
		
		itemService.itemReviewAnswerUpdate(requestBody);
		
		return new ResponseEntity<>("답변 수정 되었습니다.", HttpStatus.OK);
	} 
	
//	리뷰 답변 삭제
	@PostMapping(value="/answerDelete")
	public @ResponseBody ResponseEntity reviewAnswerDelete(@RequestBody Map<String, Object> requestBody) {
		Long id = Long.parseLong(requestBody.get("id").toString());
		itemService.itemReviewAnswerDelete(id);
		
		return new ResponseEntity("답변 삭제 되었습니다", HttpStatus.OK);
		
	} 
	
//	문의 답변 등록
	@PostMapping(value="/InqAnswerReg")
	public @ResponseBody ResponseEntity inqAnswerReg(@RequestBody Map<String, Object> requestBody ) {
		
		
		itemService.itemInqAnswerReg(requestBody);
		
		return new ResponseEntity<>("답변 등록 되었습니다.",HttpStatus.OK);
		
	} 
	
//	문의 답변 수정
	@PostMapping(value="/InqAnswerUpdate")
	public @ResponseBody ResponseEntity inqAnswerUpdate(@RequestBody Map<String, Object> requestBody ) {
		
		
		itemService.itemInqAnswerUpdate(requestBody);
		
		return new ResponseEntity<>("답변 수정 되었습니다.",HttpStatus.OK);
		
	} 
	
//	문의 답변 삭제
	@PostMapping(value="/InqAnswerDelelte")
	public @ResponseBody ResponseEntity inqAnswerDelete(@RequestBody Map<String, Object> requestBody ) {
		
		
		itemService.itemInqAnswerDelete(requestBody);
		
		return new ResponseEntity<>("답변 삭제 되었습니다.",HttpStatus.OK);
		
	} 
	
	
	private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }
	
	
	
}
