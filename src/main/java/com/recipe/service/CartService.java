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
import com.recipe.entity.Member;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.repository.CartRepository;
import com.recipe.repository.ItemRepository;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {
	
	private final CartRepository cartRepository ;
	
	private final MemberRepository memberRepository;
	
	private final ItemRepository itemRepository;

//	장바구니 정보불러오기
	@Transactional(readOnly =  true)
	public List<CartDto> getCartList(HttpSession session) {
		
		Long id = (Long) session.getAttribute("memberId");

		List<CartDto> cartList = cartRepository.getCartList(id);

		return cartList;
	}
	


//	장바구니 개별 버튼삭제
	public void cartDeleteButton(Map<String, Object> requestBody , HttpSession session) throws CustomException {
		
		try {
			Long id = Long.parseLong(requestBody.get("cartId").toString());
			
			Long cartCount = (Long) session.getAttribute("cartCount");
			
			Cart cart = cartRepository.findById(id)
					.orElseThrow(() -> new CustomException("사유: 장바구니 조회 실패."));
			
			cartRepository.delete(cart);
			cartCount--;
			
			session.setAttribute("cartCount", cartCount);
			
		} catch (CustomException e) {
			log.error("cartDeleteButton-error", e);
			throw e;
		}


	}

//	장바구니 체크박스 선택삭제
	@Transactional
	public void cartDeleteCheckBox(List<Object> requestBody , HttpSession session) throws CustomException {
		
		try {
			
			Long cartCount = (Long) session.getAttribute("cartCount");;
			
			for (int i = 0; i < requestBody.size(); i++) {
				Long id = Long.parseLong(requestBody.get(i).toString());
				
				Cart cart = cartRepository.findById(id)
						.orElseThrow(() -> new CustomException("사유: 장바구니 조회 실패."));
				
				cartRepository.delete(cart);
				cartCount--;
			}
			
			session.setAttribute("cartCount", cartCount);
			
		} catch (CustomException e) {
			log.error("cartDeleteCheckBox-error", e);
			throw e;
		}

	}

//	장바구니 품절 선택삭제
	@Transactional
	public void cartDeleteSoldoutCheckBox(List<Object> requestBody , HttpSession session) throws CustomException{

		try {
			Long cartCount = (Long) session.getAttribute("cartCount");
			
			for (int i = 0; i < requestBody.size(); i++) {
				
				Long id = Long.parseLong(requestBody.get(i).toString());
				
				Cart cart = cartRepository.findById(id)
						.orElseThrow(() -> new CustomException("사유: 장바구니 조회 실패."));
				
				Item item = itemRepository.findById(cart.getItem().getId())
						.orElseThrow(() -> new CustomException("사유: 상품 조회 실패."));
				
				if (ItemSellStatus.SELL.equals(item.getItemSellStatus())) {
					throw new CustomException("사유: 상품 상태 오류.");
				}
				
				cartRepository.delete(cart);
				cartCount--;
			}
			
			
			session.setAttribute("cartCount", cartCount);
			
		} catch (CustomException e) {
			log.error("cartDeleteSoldoutCheckBox-error", e);
			throw e;
		}

	}

//	장바구니 수량 개별 버튼변경
	@Transactional
	public void cartCountUpdateButton(Map<String, Object> requestBody) throws FindNotException , CustomException {

		Long cartId = Long.parseLong(requestBody.get("cartId").toString());
		int count = Integer.parseInt(requestBody.get("count").toString());
		
		try {
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new FindNotException("사유: 장바구니 조회 실패."));
			
			Item item = itemRepository.findById(cart.getItem().getId())
					.orElseThrow(() -> new FindNotException("사유: 상품 조회 실패."));
			
			if (item.getStockNumber() < count) {
				throw new CustomException("사유: 상품 재고 부족");
			} 
			
			cart.setCount(count);
			
		} catch (FindNotException e) {
			log.error("cartCountUpdateButton-error", e);
			throw e;
		}  catch (CustomException e) {
			throw e;
		}
	
	}

//	장바구니 등록
	public Long cartReg(Map<String, Object> requestBody, HttpSession session) throws CustomException , FindNotException {

		Long id = (Long) session.getAttribute("memberId");
		
		Long cartCount = (Long) session.getAttribute("cartCount");

		Long itemId = Long.parseLong(requestBody.get("id").toString());
		int count = Integer.parseInt(requestBody.get("count").toString());
		
		try {
			Member member = memberRepository.findById(id)
					.orElseThrow(() -> new CustomException("사유: 회원 조회 실패"));
			
			Item item = itemRepository.findById(itemId)
					.orElseThrow(() -> new CustomException("사유: 상품 조회 실패"));
			
			Cart cartCheck = cartRepository.findByMemberIdAndItemIdAndCount(id, itemId, count);
			
			if (cartCheck != null) {
				throw new FindNotException("장바구니에 있는 상품입니다.");
				
			} else {
				
				Cart cart = Cart.createCart(member, item, count);
				cartRepository.save(cart);
				cartCount++;
			}
			
			session.setAttribute("cartCount", cartCount);
			
		} catch (CustomException e) {
			log.error("cartReg-error",e);
			throw e;
			
		}
		
		
		return cartCount;
	}
}
