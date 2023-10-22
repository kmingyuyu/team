package com.recipe.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.constant.AnswerOk;
import com.recipe.constant.ItemReviewStatus;
import com.recipe.constant.OrderStatus;
import com.recipe.constant.PointEnum;
import com.recipe.repository.BookMarkRepository;
import com.recipe.repository.CartRepository;
import com.recipe.repository.FollowRepository;
import com.recipe.repository.ItemInqRepository;
import com.recipe.repository.ItemReviewRepository;
import com.recipe.repository.OrderItemRepository;
import com.recipe.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CountService {

	private final FollowRepository followRepository;

	private final CartRepository cartRepository;

	private final OrderItemRepository orderItemRepository;
	
	private final ItemReviewRepository itemReviewRepository;
	
	private final ItemInqRepository itemInqRepository;
	
	private final PointRepository pointRepository;
	
	private final BookMarkRepository bookMarkRepository;

//	팔로워 갯수 찾기
	public Long countToMember(Long id) {

		Long follower = followRepository.countFromMember(id);
		
		return follower;
	}

// 팔로잉 갯수 찾기
	public Long countFromMember(Long id) {

		Long following = followRepository.countToMember(id);
		
		return following;
	}

// 장바구니 갯수 찾기
	public Long cartCount(Long id) {

		Long cartCount = cartRepository.countByMemberId(id);

		return cartCount;
	}
	
// 북마크 갯수 찾기
	public Long bookMarkCount(Long id) {
		
		Long bookMarkCount = bookMarkRepository.countByMemberId(id);
		
		return bookMarkCount;
	}

// 마이페이지(주문내역/배송조회) 주문의 상태값 갯수 찾기	
	public Map<String, Long> myPageOrderStatusCount(Long memberId) {

		Map<String, Long> countMap = new HashMap<>();
		Long ordercount;

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.주문완료,memberId);
		countMap.put("주문완료", ordercount);
		
		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.발송요청완료,memberId);
		countMap.put("발송요청완료", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.배송준비중,memberId);
		countMap.put("배송준비중", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.배송중,memberId);
		countMap.put("배송중", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.배송완료,memberId);
		countMap.put("배송완료", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.구매확정,memberId);
		countMap.put("구매확정", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.반품요청,memberId);
		countMap.put("반품요청", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.반품진행중,memberId);
		countMap.put("반품진행중", ordercount);

		ordercount = orderItemRepository.countByOrderStatusAndOrder_Member_Id(OrderStatus.환불완료,memberId);
		countMap.put("환불완료", ordercount);

		return countMap;

	}
	
// 마이페이지(나의상품후기) 리뷰의 상태값 갯수 찾기	
	public Map<String , Long>myPageItemReviewStatusCount(Long memberId) {
		
		Map<String, Long> countMap = new HashMap<>();
		Long itemReviewCount;
		
		itemReviewCount = itemReviewRepository.countByMemberId(memberId);
		countMap.put("전체", itemReviewCount);
		
		itemReviewCount = itemReviewRepository.countByItemReviewStatusAndMemberId(ItemReviewStatus.일반, memberId);
		countMap.put("일반", itemReviewCount);
		
		itemReviewCount = itemReviewRepository.countByItemReviewStatusAndMemberId(ItemReviewStatus.포토, memberId);
		countMap.put("포토", itemReviewCount);
		
		return countMap;
	}
	
	
// 마이페이지(나의상품후기) 문의글의 상태값 갯수 찾기	
	public Map<String , Long>myPageItemInqAnswerOkCount(Long memberId) {
		
		Map<String, Long> countMap = new HashMap<>();
		Long itemInqCount;
		
		itemInqCount = itemInqRepository.countByMemberId(memberId);
		countMap.put("전체", itemInqCount);
		
		itemInqCount = itemInqRepository.countByAnswerOkAndMemberId(AnswerOk.답변대기, memberId);
		countMap.put("답변대기", itemInqCount);
		
		itemInqCount = itemInqRepository.countByAnswerOkAndMemberId(AnswerOk.답변완료, memberId);
		countMap.put("답변완료", itemInqCount);
		
		return countMap;
	}
	
// 마이페이지(포인트) 포인트의 상태값 갯수 찾기	
	public Map<String , Long>myPagePointEnumCount(Long memberId) {
		
		Map<String, Long> countMap = new HashMap<>();
		Long pointCount;
		
		pointCount = pointRepository.countByMemberId(memberId);
		countMap.put("전체", pointCount);
		
		pointCount = pointRepository.countByPointEnumAndMemberId(PointEnum.PLUS, memberId);
		countMap.put("적립", pointCount);
		
		pointCount = pointRepository.countByPointEnumAndMemberId(PointEnum.MINUS, memberId);
		countMap.put("사용", pointCount);
		
		pointCount = pointRepository.countByPointEnumAndMemberId(PointEnum.EXPIRE, memberId);
		countMap.put("차감", pointCount);
		
		
		pointCount = pointRepository.sumByPointEnumAndMemberId(PointEnum.PLUS, memberId);
		countMap.put("적립총합", pointCount);
		
		pointCount = pointRepository.sumByPointEnumAndMemberId(PointEnum.MINUS, memberId);
		countMap.put("사용총합", pointCount);
		
		pointCount = pointRepository.sumByPointEnumAndMemberId(PointEnum.EXPIRE, memberId);
		countMap.put("차감총합", pointCount);
		
		return countMap;
	}
	
	

}
