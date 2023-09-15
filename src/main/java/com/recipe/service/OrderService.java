package com.recipe.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.constant.ItemSellStatus;
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

//	장바구니 정보불러오기
	public List<CartDto> getCartList(HttpSession session) {
		Long id = (Long) session.getAttribute("memberId");

		List<CartDto> cartSearch = cartRepository.getCartList(id);

		if (cartSearch == null) {
			return null;
		}
		return cartSearch;
	}

//	장바구니 개별 버튼삭제
	public Boolean cartDeleteButton(@RequestBody Map<String, Object> requestBody) {
		Long id = Long.parseLong(requestBody.get("cartId").toString());

		Boolean check = true;

		Cart cart = cartRepository.findById(id).orElseThrow();

		if (cart == null) {
			check = false;
		}

		cartRepository.delete(cart);

		return check;
	}

//	장바구니 체크박스 선택삭제
	public Boolean cartDeleteCheckBox(@RequestBody List<Object> requestBody) {

		Boolean check = true;

		for (int i = 0; i < requestBody.size(); i++) {
			Long id = Long.parseLong(requestBody.get(i).toString());
			Cart cart = cartRepository.findById(id).orElseThrow();

			if (cart == null) {
				check = false;
			}

			cartRepository.delete(cart);
		}

		return check;
	}
	
	
//	장바구니 품절 선택삭제
	public Boolean cartDeleteSoldoutCheckBok(@RequestBody List<Object> requestBody) {
		
		Boolean check = true;
		
		for (int i = 0; i < requestBody.size(); i++) {
			Long id = Long.parseLong(requestBody.get(i).toString());
			Cart cart = cartRepository.findById(id).orElseThrow();
			Item item = itemRepository.findById(cart.getItem().getId()).orElseThrow();
			
			if(ItemSellStatus.SELL.equals(item.getItemSellStatus())) {
				check = false;
			}
			
			cartRepository.delete(cart);
		}
		
		return check;
	}

//	장바구니 수량 개별 버튼변경
	public String cartCountUpdateButton(@RequestBody Map<String, Object> requestBody) {
		String check = null;

		Long cartId = Long.parseLong(requestBody.get("cartId").toString());
		int count = Integer.parseInt(requestBody.get("count").toString());

		Cart cart = cartRepository.findById(cartId).orElseThrow();
		Item item = itemRepository.findById(cart.getMember().getId()).orElseThrow();

		if (cart == null || item == null) {
			return check = "error";
		}
		else if (item.getStockNumber() < count) {
			return check  = "stockError";
		}
		else {
			cart.setCount(count);
			cartRepository.save(cart);
			return check;
		}
	}

//	장바구니 등록
	public Long cartReg(@RequestBody Map<String, Object> requestBody, HttpSession session) {

		Long id = (Long) session.getAttribute("memberId");

		Long itemId = Long.parseLong(requestBody.get("id").toString());
		int count = Integer.parseInt(requestBody.get("count").toString());

		Member member = memberRepository.findById(id).orElseThrow();

		Item item = itemRepository.findById(itemId).orElseThrow();

		Cart cartCheck = cartRepository.findByMemberIdAndItemIdAndCount(id, itemId, count);

		if (cartCheck != null) {
			return null;
		} else {
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
