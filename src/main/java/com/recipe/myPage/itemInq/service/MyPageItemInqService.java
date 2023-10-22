package com.recipe.myPage.itemInq.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.constant.AnswerOk;
import com.recipe.constant.ImgMainOk;
import com.recipe.constant.ItemInqBoardEnum;
import com.recipe.constant.ItemInqEnum;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.ItemInq;
import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.ItemInqHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.repository.ItemImgRepository;
import com.recipe.repository.ItemInqRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageItemInqService {
	
	private final ItemInqRepository itemInqRepository;
	
	private final ItemImgRepository itemImgRepository;
	
	
//	나의 상품 문의글 가져오기(/myPage/item_inq)
	public Page<ItemInqHistoryDto> findByMyItemInqList(Pageable pageable, Long memberId , MyPageSerchDto myPageSerchDto){
		
		Page<ItemInqHistoryDto> findByMyItemInqList = itemInqRepository.findByMyItemInqList(pageable,memberId, myPageSerchDto);
		
		return findByMyItemInqList;
	}
	
	
//	나의 상품 문의글 삭제(/myPage/item_inq)
	public void itemInqDelete(@RequestBody Map<String, Object> requestBody , Long memberId) throws CustomException {
		
		Long itemInqId = Long.parseLong(requestBody.get("itemInqId").toString());
		
		try {
			ItemInq itemInq = itemInqRepository.findById(itemInqId)
					.orElseThrow(() -> new CustomException("사유: 등록된 문의글 조회 실패"));
			
			if(itemInq.getMember().getId() != memberId) {
				new CustomException("사유: 문의글 작성자 조회 실패");
			}
			
			itemInqRepository.delete(itemInq);
			
			
		} catch (CustomException e) {
			log.error("itemInqDelete-error",e);
			throw e;
		}  
	}
	
//	나의 상품 문의글 수정팝업창 정보 가져오기(/myPage/item_inq/itemInq_popup_modi)
	public Map<String, Object> findByMyItemInq(Long itemInqId , Long memberId) throws CustomException{
		
		Map<String, Object> itemInqMap = new HashMap<>();
		
		try {
			ItemInq itemInq = itemInqRepository.findById(itemInqId)
					.orElseThrow(() -> new CustomException("사유: 문의글 조회 실패"));
			
			Item item = itemInq.getItem();
			
			if(AnswerOk.답변완료.equals(itemInq.getAnswerOk())) {
				throw new CustomException("사유: 등록된 답변 존재");
			}
			
			if(itemInq.getMember().getId() != memberId) {
				throw new CustomException("사유: 접속 회원과 문의 작성 회원 불일치");
			}
			
			if (item == null) {
				throw new CustomException("사유: 상품 조회 실패");
			}
			
			ItemImg itemImg = itemImgRepository.findByItemIdAndImgMainOk(item.getId(), ImgMainOk.Y);
			
			if (itemImg == null) {
				throw new CustomException("사유: 상품 이미지 조회 실패");
			}
			
			itemInqMap.put("itemInq",itemInq);
			itemInqMap.put("item",item);
			itemInqMap.put("itemImg",itemImg);
			
		} catch (CustomException e) {
			log.error("findByMyItemInq-error", e);
			throw e;
			
		}
		
		
		return itemInqMap;
		
	}
	
//	나의 상품 문의글 수정팝업창 수정하기(/myPage/item_inq/itemInq_popup_modi)
	@Transactional
	public void itemInqModi(@RequestBody Map<String, Object> requestBody , Long memberId) throws CustomException {
		
		Long itemInqId = Long.parseLong(requestBody.get("itemInqId").toString());
		String title = requestBody.get("title").toString();
		String content = requestBody.get("content").toString();
		int itemInqBoardEnum = Integer.parseInt(requestBody.get("itemInqBoardEnum").toString());
		int itemInqEnum = Integer.parseInt(requestBody.get("itemInqEnum").toString());
		
		if(itemInqBoardEnum == 0 || itemInqBoardEnum > 3) {
			new CustomException("사유: 문의글 상태 선택 오류");
		}
		
		if(itemInqEnum == 0 || itemInqEnum > 5) {
			new CustomException("사유: 문의유형 선택 오류");
		}
		
		if(title == null || title.length() > 15) {
			throw new CustomException("사유: 문의 제목 오류");
		}
		
		String contentCheck = content.replaceAll("\\s", "");
		if(content == null || contentCheck.length() > 350) {
			throw new CustomException("사유: 문의 내용 오류");
		}
		
		
		try {
			ItemInq itemInq = itemInqRepository.findById(itemInqId)
					.orElseThrow(() -> new CustomException("사유: 문의글 조회 실패"));
			
			itemInq.setTitle(title);
			itemInq.setContent(content);
			
			switch (itemInqBoardEnum) {
			case 1:
				itemInq.setItemInqBoardEnum(ItemInqBoardEnum.공개글);
				break;
			case 2:
				itemInq.setItemInqBoardEnum(ItemInqBoardEnum.비밀글);
				break;
			}
			
			switch (itemInqEnum) {
			case 1:
				itemInq.setItemInqEnum(ItemInqEnum.배송문의);
				break;
			case 2:
				itemInq.setItemInqEnum(ItemInqEnum.재입고문의);
				break;
			case 3:
				itemInq.setItemInqEnum(ItemInqEnum.상품상세문의);
				break;
			case 4:
				itemInq.setItemInqEnum(ItemInqEnum.기타문의);
				break;
			}
			
		} catch (CustomException e) {
			log.error("itemInqModi-error", e);
			throw e;
		}
		
		
	}
	
	
	
}
