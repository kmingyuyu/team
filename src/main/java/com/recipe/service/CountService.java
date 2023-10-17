package com.recipe.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.constant.ItemReviewStatus;
import com.recipe.constant.OrderStatus;
import com.recipe.repository.CartRepository;
import com.recipe.repository.FollowRepository;
import com.recipe.repository.ItemReviewRepository;
import com.recipe.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CountService {

	private final FollowRepository followRepository;

	private final CartRepository cartRepository;

	private final OrderItemRepository orderItemRepository;
	
	private final ItemReviewRepository itemReviewRepository;

//	팔로잉 갯수 찾기
	public Long countToMember(Long id) {

		Long following = followRepository.countFromMember(id);

		return following;
	}

// 팔로워 갯수 찾기
	public Long countFromMember(Long id) {

		Long follower = followRepository.countToMember(id);

		return follower;
	}

// 장바구니 갯수 찾기
	public Long cartCount(Long id) {

		Long cartCount = cartRepository.countByMemberId(id);

		return cartCount;
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
		
		itemReviewCount = itemReviewRepository.countByItemReviewStatusAndMemberId(ItemReviewStatus.일반, memberId);
		countMap.put("일반", itemReviewCount);
		
		itemReviewCount = itemReviewRepository.countByItemReviewStatusAndMemberId(ItemReviewStatus.포토, memberId);
		countMap.put("포토", itemReviewCount);
		
		return countMap;
	}

}
