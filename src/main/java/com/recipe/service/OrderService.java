package com.recipe.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.dto.CartDto;
import com.recipe.entity.Cart;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.Member;
import com.recipe.repository.CartRepository;
import com.recipe.repository.ItemImgRepository;
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
	
	private final ItemImgRepository itemImgRepository;
	
	
	
	public List<CartDto> getCartList(HttpSession session){
		Long id = (Long) session.getAttribute("memberId");
		
		List<CartDto> cartSearch = cartRepository.getCartList(id);
		
		if(cartSearch == null) {
			return null;
		}
		return cartSearch;
	}
	
	public Boolean cartDeleteButton(@RequestBody Map<String, Object> requestBody) {
		Long id = Long.parseLong(requestBody.get("cartId").toString());
		
		Boolean check = true;
		
		Cart cart = cartRepository.findById(id).orElseThrow();
		
		if(cart == null) {
			check = false;
		}
		
		cartRepository.delete(cart);
		
		return check;
	}
	
	public Boolean cartDeleteCheckBox(@RequestBody List<Object> requestBody) {
		
		Boolean check = true;
		
		for(int i=0; i<requestBody.size(); i++) {
			Long id = Long.parseLong(requestBody.get(i).toString());
			Cart cart = cartRepository.findById(id).orElseThrow();
			
			if(cart == null) {
				check = false;
			}
			
			cartRepository.delete(cart);
		}
		
		
		
		
		return check;
	}
	
	
	
	
	public Long cartReg(@RequestBody Map<String, Object> requestBody , HttpSession session) {
		
		Long id = (Long) session.getAttribute("memberId");
		
		Long itemId = Long.parseLong(requestBody.get("id").toString());
		int count = Integer.parseInt(requestBody.get("count").toString()) ;
		
		Member member = memberRepository.findById(id).orElseThrow();
		
		Item item = itemRepository.findById(itemId).orElseThrow();
		
		Cart cartCheck = cartRepository.findByMemberIdAndItemIdAndCount(id, itemId,count);
		
		
		if(cartCheck != null) {
			return null;
		}
		else {
			Cart cart = new Cart();
			cart.setMember(member);
			cart.setItem(item);
			cart.setCount(count);
			cartRepository.save(cart);
			
			Long cartCount = cartRepository.countByMemberId(id);
			
			return cartCount;
		}
	}
	
	
}
