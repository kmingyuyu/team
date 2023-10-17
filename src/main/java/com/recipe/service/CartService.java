package com.recipe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.constant.ImgMainOk;
import com.recipe.constant.ItemSellStatus;
import com.recipe.dto.CartDto;
import com.recipe.dto.ItemOrderDto;
import com.recipe.dto.OrderDto;
import com.recipe.entity.Cart;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.Member;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.repository.CartRepository;
import com.recipe.repository.ItemImgRepository;
import com.recipe.repository.ItemRepository;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	
	private final CartRepository cartRepository ;
	
	private final MemberRepository memberRepository;
	
	private final ItemRepository itemRepository;
	
	private final Logger log = LoggerFactory.getLogger(CartService.class);

//	장바구니 정보불러오기
	public List<CartDto> getCartList(HttpSession session) {
		
		Long id = (Long) session.getAttribute("memberId");

		List<CartDto> cartList = cartRepository.getCartList(id);

		return cartList;
	}
	


//	장바구니 개별 버튼삭제
	public void cartDeleteButton(@RequestBody Map<String, Object> requestBody) throws FindNotException {
		try {
			Long id = Long.parseLong(requestBody.get("cartId").toString());
			
			Cart cart = cartRepository.findById(id)
					.orElseThrow(() -> new FindNotException("장바구니 삭제 중 오류가 발생했습니다. 삭제 실패하였습니다."));
			
			cartRepository.delete(cart);
		} catch (Exception e) {
			log.error("cartDeleteButton-error", e);
			throw e;
		}


	}

//	장바구니 체크박스 선택삭제
	public void cartDeleteCheckBox(@RequestBody List<Object> requestBody) throws FindNotException {
		
		try {
			
			for (int i = 0; i < requestBody.size(); i++) {
				Long id = Long.parseLong(requestBody.get(i).toString());
				
				Cart cart = cartRepository.findById(id)
						.orElseThrow(() -> new FindNotException("장바구니 삭제 중 오류가 발생했습니다. 삭제 실패하였습니다."));
				
				cartRepository.delete(cart);
			}
			
		} catch (Exception e) {
			log.error("cartDeleteCheckBox-error", e);
			throw e;
		}

	}

//	장바구니 품절 선택삭제
	public void cartDeleteSoldoutCheckBox(@RequestBody List<Object> requestBody) throws FindNotException{

		try {
			for (int i = 0; i < requestBody.size(); i++) {
				
				Long id = Long.parseLong(requestBody.get(i).toString());
				
				Cart cart = cartRepository.findById(id)
						.orElseThrow(() -> new FindNotException("장바구니 삭제 중 오류가 발생했습니다. 삭제 실패하였습니다."));
				
				Item item = itemRepository.findById(cart.getItem().getId())
						.orElseThrow(() -> new FindNotException("장바구니 삭제 중 오류가 발생했습니다. 삭제 실패하였습니다."));
				
				if (ItemSellStatus.SELL.equals(item.getItemSellStatus())) {
					throw new FindNotException("장바구니 삭제 중 오류가 발생했습니다. 삭제 실패하였습니다.");
				}
				
				cartRepository.delete(cart);
			}
			
		} catch (Exception e) {
			log.error("cartDeleteSoldoutCheckBox-error", e);
			throw e;
		}

	}

//	장바구니 수량 개별 버튼변경
	public void cartCountUpdateButton(@RequestBody Map<String, Object> requestBody) throws FindNotException , CustomException {

		Long cartId = Long.parseLong(requestBody.get("cartId").toString());
		int count = Integer.parseInt(requestBody.get("count").toString());
		
		try {
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new FindNotException("장바구니 수량변경 중 오류가 발생했습니다. 수량변경에 실패하였습니다."));
			
			Item item = itemRepository.findById(cart.getItem().getId())
					.orElseThrow(() -> new FindNotException("장바구니 수량변경 중 오류가 발생했습니다. 수량변경에 실패하였습니다."));
			
			if (item.getStockNumber() < count) {
				throw new CustomException("상품재고보다 변경수량이 많습니다.");
			} 
			
			cart.setCount(count);
			cartRepository.save(cart);
			
		} catch (FindNotException e) {
			log.error("cartCountUpdateButton-error", e);
			throw e;
		}  catch (CustomException e) {
			throw e;
		}
	
	}

//	장바구니 등록
	public Long cartReg(@RequestBody Map<String, Object> requestBody, HttpSession session) {

		Long id = (Long) session.getAttribute("memberId");

		Long itemId = Long.parseLong(requestBody.get("id").toString());
		int count = Integer.parseInt(requestBody.get("count").toString());

		Optional<Member> memberOptional = memberRepository.findById(id);
		Member member = memberOptional.get();

		Optional<Item> itemOptional = itemRepository.findById(itemId);
		Item item = itemOptional.get();

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
