package com.recipe.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.dto.CartDto;
import com.recipe.entity.Delivery;
import com.recipe.entity.Order;
import com.recipe.service.CartService;
import com.recipe.service.DeliveryService;
import com.recipe.service.KakaoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	private final DeliveryService deliveryService;

	private final KakaoService kakaoService;

//	장바구니 페이지
	@GetMapping(value = "/cart")
	public String Cart(Model model, HttpSession session) {

		List<CartDto> cartList = cartService.getCartList(session);

		model.addAttribute("cartList", cartList);

		
		  List<Delivery> deliveryList = deliveryService.findDelivery();
		  System.out.println("size" + deliveryList.size());
		  
		  List<Order> orderList = deliveryService.findOrder();
		  System.out.println("size" + orderList.size());
		  
		  try { for (int i = 0; i < orderList.size(); i++) {
		  
		  String sendAddress =
		  deliveryList.get(i).getDeliveryAddress().getSendAddress();
		  
		  String receiveAddress =
		  deliveryList.get(i).getDeliveryAddress().getReceiveAddress();
		  
		  
		  Map<String, Integer> durationMap = kakaoService.getDuration(sendAddress,
		  receiveAddress);
		  
		  
		  deliveryService.orderDeliveryRequest(durationMap,
		  orderList.get(i),deliveryList.get(i)); }
		  
		  } catch (Exception e) { e.printStackTrace(); }
		 
		 
		return "cart";

	}

//	카트 상품담기
	@PostMapping(value = "/cart/cartReq")
	public @ResponseBody ResponseEntity inqReq(@RequestBody Map<String, Object> requestBody, HttpSession session) {

		Long cartCount = cartService.cartReg(requestBody, session);

		if (cartCount == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(cartCount, HttpStatus.OK);
	}

//	장바구니 개별 버튼삭제
	@DeleteMapping(value = "/cart/cartDeleteButton")
	public @ResponseBody ResponseEntity cartDeleteButton(@RequestBody Map<String, Object> requestBody) {

		try {

			cartService.cartDeleteButton(requestBody);

			return new ResponseEntity<>("장바구니에서 삭제 되었습니다.", HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}

	}

//	장바구니 체크박스 선택삭제
	@DeleteMapping(value = "/cart/cartDeletCkeckBox")
	public @ResponseBody ResponseEntity cartDeletCkeckBox(@RequestBody List<Object> requestBody) {

		try {

			cartService.cartDeleteCheckBox(requestBody);

			return new ResponseEntity<>("장바구니에서 삭제 되었습니다.", HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}

	}

//	장바구니 품절 선택삭제
	@DeleteMapping(value = "/cart/cartDeleteSoldoutCheckBox")
	public @ResponseBody ResponseEntity cartDeleteSoldoutCheckBox(@RequestBody List<Object> requestBody) {

		try {

			cartService.cartDeleteSoldoutCheckBox(requestBody);

			return new ResponseEntity<>("장바구니에서 삭제 되었습니다.", HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}

	}

//	카트 상품 개별수량 변경
	@PatchMapping(value = "/cart/cartCountUpdateButton")
	public @ResponseBody ResponseEntity cartCountUpdateButton(@RequestBody Map<String, Object> requestBody) {

		try {

			cartService.cartCountUpdateButton(requestBody);

			return new ResponseEntity<>("수량 변경되었습니다.", HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}

	}
}
