package com.recipe.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.constant.AnswerOk;
import com.recipe.constant.ImgMainOk;
import com.recipe.constant.ItemInqBoardEnum;
import com.recipe.constant.ItemInqEnum;
import com.recipe.constant.ItemSellStatus;
import com.recipe.dto.ItemCategoryDto;
import com.recipe.dto.ItemDetailDto;
import com.recipe.dto.ItemInqDto;
import com.recipe.dto.ItemReviewDto;
import com.recipe.dto.ItemReviewImgDto;
import com.recipe.dto.ItemSearchDto;
import com.recipe.dto.OrderPayDto;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.ItemInq;
import com.recipe.entity.ItemInqAnwser;
import com.recipe.entity.ItemReview;
import com.recipe.entity.ItemReviewAnswer;
import com.recipe.entity.Member;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.repository.ItemImgRepository;
import com.recipe.repository.ItemInqAnswerRepository;
import com.recipe.repository.ItemInqRepository;
import com.recipe.repository.ItemRepository;
import com.recipe.repository.ItemReviewAnswerRepository;
import com.recipe.repository.ItemReviewImgRepository;
import com.recipe.repository.ItemReviewRepository;
import com.recipe.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
	
	private final MemberRepository memberRepository;
	
	private final ItemRepository itemRepository;

	private final ItemReviewRepository itemReviewRepository;

	private final ItemReviewAnswerRepository itemReviewAnswerRepository;
	
	private final ItemReviewImgRepository itemReviewImgRepository ;
	
	private final ItemInqRepository itemInqRepository;
	
	private final ItemInqAnswerRepository itemInqAnswerRepository; 
	
	private final ItemImgRepository itemImgRepository;
	
	private final Logger log = LoggerFactory.getLogger(OrderService.class);
	
	
	public Item findItem(Long id) {
		
		Item item = itemRepository.findById(id).orElseThrow(
                () -> new FindNotException("itemId " + id + " not found"));
		return item;
	}
	
	
	public ItemImg findItemImg(Long id) {
		
		ItemImg itemImg = itemImgRepository.findByItemIdAndImgMainOk(id, ImgMainOk.Y);
		
		return itemImg;
	}
	
	
	
	@Transactional(readOnly = true)
	public ItemDetailDto getItemDetailList(Long itemId) {
		ItemDetailDto getItemDetailList = itemRepository.getItemDetailList(itemId);
		return getItemDetailList;
	}

//	모든 상품 가져오기
	@Transactional(readOnly = true)
	public Page<ItemCategoryDto> getItemCategoryList(Pageable pageable, ItemSearchDto itemSearchDto) {
		Page<ItemCategoryDto> getItemCategoryList = itemRepository.getItemCategoryList(pageable, itemSearchDto);
		return getItemCategoryList;
	}

//	상품의 리뷰글 가져오기
	@Transactional(readOnly = true)
	public Page<ItemReviewDto> getItemReviewList(Pageable pageable, Long itemId) {
		Page<ItemReviewDto> getItemReviewList = itemRepository.getItemReviewList(pageable, itemId);
		return getItemReviewList;
	}
	
//	상품의 리뷰의 이미지 가져오기
	@Transactional(readOnly = true)
	public List<ItemReviewImgDto> getItemReviewImgList(Long id) {
		List<ItemReviewImgDto> getItemReviewImgList = itemReviewImgRepository.getItemReviewImgList(id) ;
		return getItemReviewImgList;
	}
	
//	상품의 문의글 가져오기
	@Transactional(readOnly = true)
	public Page<ItemInqDto> getItemInqList(Pageable pageable , Long itemId){
		Page<ItemInqDto> getItemInqList =  itemRepository.getItemInqList(pageable ,itemId);
		return getItemInqList;
	}

//	리뷰 답변 등록
	@Transactional
	public void itemReviewAnswerReg(@RequestBody Map<String, Object> requestBody , String email) {
		
		Long id = Long.parseLong(requestBody.get("id").toString());
		String content = requestBody.get("content").toString();
		
		Optional<ItemReview> itemReviewOp = itemReviewRepository.findById(id);
		ItemReview itemReview = itemReviewOp.get();
		
		Member member = memberRepository.findByEmail(email);
		
		ItemReviewAnswer itemReviewAnswer = ItemReviewAnswer.createItemReviewAnswer(member, itemReview, content);
		itemReview.setItemReviewAnswer(itemReviewAnswer);
		
	}

//	리뷰 답변 수정
	public void itemReviewAnswerUpdate(@RequestBody Map<String, Object> requestBody) {
		Long id = Long.parseLong(requestBody.get("id").toString());
		String content = requestBody.get("content").toString();
		
		Optional<ItemReviewAnswer> itemReviewAnswerOp = itemReviewAnswerRepository.findById(id);
		ItemReviewAnswer itemReviewAnswer = itemReviewAnswerOp.get();
		
		
		itemReviewAnswer.setId(id);
		itemReviewAnswer.setContent(content);

		itemReviewAnswerRepository.save(itemReviewAnswer);
	}

//	리뷰 답변 삭제
	public void itemReviewAnswerDelete(Long id) {
		
		Optional<ItemReviewAnswer> itemReviewAnswerOp = itemReviewAnswerRepository.findById(id);
		ItemReviewAnswer itemReviewAnswer = itemReviewAnswerOp.get();
		
		itemReviewAnswerRepository.delete(itemReviewAnswer);
	}

//	문의글 등록
	public void itemInqReg(Map<String, Object> requestBody , String email) {
		
		Long id = Long.parseLong(requestBody.get("id").toString());
		String title = requestBody.get("title").toString();
		String content = requestBody.get("content").toString();
		int itemInqBoardEnum = Integer.parseInt(requestBody.get("itemInqBoardEnum").toString());
		int itemInqEnum = Integer.parseInt(requestBody.get("itemInqEnum").toString());
		Member member = memberRepository.findByEmail(email);
		
		Optional<Item> itemOp = itemRepository.findById(id);
		Item item = itemOp.get();
		
		ItemInq itemInq = new ItemInq();
		itemInq.setMember(member);
		itemInq.setItem(item);
		itemInq.setTitle(title);
		itemInq.setContent(content);
		
		System.out.println("itemInqBoardEnum num ::" + itemInqBoardEnum);
		System.out.println("itemInqEnum num ::" + itemInqEnum);
		
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
		
		
		itemInqRepository.save(itemInq);
	}
	
	
//	문의 답변 등록
	public void itemInqAnswerReg(Map<String, Object> requestBody,String email) {
		
		Long id = Long.parseLong(requestBody.get("id").toString());
		String content = requestBody.get("content").toString();
		
		Member member = memberRepository.findByEmail(email);
		
		Optional<ItemInq> itemInqOp = itemInqRepository.findById(id);
		ItemInq itemInq = itemInqOp.get();
		
		itemInq.setAnswerOk(AnswerOk.답변완료);
		itemInqRepository.save(itemInq);
		
		ItemInqAnwser itemInqAnwser = new ItemInqAnwser();
		itemInqAnwser.setMember(member);
		itemInqAnwser.setItemInq(itemInq);
		itemInqAnwser.setContent(content);
		
		itemInqAnswerRepository.save(itemInqAnwser);

	}
	
//	문의 답변 수정
	public void itemInqAnswerUpdate(Map<String, Object> requestBody) {
		
		Long id = Long.parseLong(requestBody.get("id").toString());
		String content = requestBody.get("content").toString();
		
		Optional<ItemInqAnwser> itemInqAnwserOp = itemInqAnswerRepository.findById(id);
		ItemInqAnwser itemInqAnwser = itemInqAnwserOp.get();
		
		itemInqAnwser.setId(id);
		itemInqAnwser.setContent(content);
		
		itemInqAnswerRepository.save(itemInqAnwser);

	}
	
	
//	문의 답변 삭제
	public void itemInqAnswerDelete(Map<String, Object> requestBody) {
		
		Long id = Long.parseLong(requestBody.get("id").toString());
		
		Optional<ItemInqAnwser> itemInqAnwserOp = itemInqAnswerRepository.findById(id);
		ItemInqAnwser itemInqAnwser = itemInqAnwserOp.get();
		
		Optional<ItemInq> itemInqOp = itemInqRepository.findById(itemInqAnwser.getItemInq().getId());
		ItemInq itemInq = itemInqOp.get();
		
		itemInq.setAnswerOk(AnswerOk.답변대기);
		itemInqRepository.save(itemInq);
		
		itemInqAnswerRepository.delete(itemInqAnwser);
	}
	

	
	
	
	
}
