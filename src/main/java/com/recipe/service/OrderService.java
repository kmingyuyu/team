package com.recipe.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.entity.Cart;
import com.recipe.entity.Item;
import com.recipe.entity.Member;
import com.recipe.repository.CartRepository;
import com.recipe.repository.ItemRepository;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final MemberRepository memberRepository;
	
	private final CartRepository cartRepository;
	
	private final ItemRepository itemRepository;
	
	
	public String cartReg(@RequestBody Map<String, Object> requestBody , HttpSession session) {
		
		Long id = (Long) session.getAttribute("memberId");
		
		Long itemId = Long.parseLong(requestBody.get("id").toString());
		int count = Integer.parseInt(requestBody.get("count").toString()) ;
		
		Member member = memberRepository.findById(id).orElseThrow();
		
		Item item = itemRepository.findById(itemId).orElseThrow();
		
		Cart cartCheck = cartRepository.findByMemberIdAndItemIdAndCount(id, itemId,count);
		
//		비정상적인 접근 방지
		if(item.getStockNumber() < count) {
			return "현재 재고가 부족합니다. 죄송합니다.";
		}
		
		
		if(cartCheck != null) {
			return "duplication";
		}
		else {
			Cart cart = new Cart();
			cart.setMember(member);
			cart.setItem(item);
			cart.setCount(count);
			cartRepository.save(cart);
			
			return "success";
		}
	}
	
	
}
