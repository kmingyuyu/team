package com.recipe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.recipe.constant.PointEnum;
import com.recipe.dto.ItemOrderDto;
import com.recipe.dto.OrderDto;
import com.recipe.dto.OrderHistoryDto;
import com.recipe.dto.OrderPayDto;
import com.recipe.entity.BuyInfo;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.Member;
import com.recipe.entity.Order;
import com.recipe.entity.OrderItem;
import com.recipe.entity.Point;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.service.CartService;
import com.recipe.service.IamPortService;
import com.recipe.service.MemberService;
import com.recipe.service.OrderService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	private final MemberService memberService;

	private final IamPortService iamPortService;

	private final CartService cartService;

//	장바구니 -> 주문페이지 이동시 데이터 세션에 저장
	@PostMapping(value = "/order/cart")
	public ResponseEntity<String> cartOrder(@ModelAttribute ItemOrderDto itemOrderDto, Model model,
			HttpSession session) {
		
		try {
			session.setAttribute("cartList", itemOrderDto);
			
			
//		맵으로 중복된 아이디가 있다면 수량을 합쳐준다
			Map<Long, OrderDto> orderMap = orderService.findOrderMapAndOrderUpdateCart(itemOrderDto);

//		맵을 리스트로 변환
			List<OrderDto> orderList = new ArrayList<>(orderMap.values());

//		합친 아이템의 확인여부 재고여부,품절여부
			orderService.orderCheck(orderList);
				
			session.setAttribute("orderList", orderList);

		} catch (Exception e) {
			
			return new ResponseEntity<>("주문 페이지 이동이 실패 하였습니다. "+e.getMessage(), HttpStatus.BAD_REQUEST);

		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

//	상품페이지 -> 주문페이지 이동전 데이터 세션에 저장
	@PostMapping(value = "/order/item")
	public ResponseEntity<String> itemOrder(@ModelAttribute ItemOrderDto itemOrderDto, Model model, HttpSession session) {

		try {
			
			List<OrderDto> orderList = orderService.addOrderList(itemOrderDto);
			
			orderService.orderCheck(orderList);
			
			session.setAttribute("orderList", orderList);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("주문 페이지 이동이 실패 하였습니다. "+e.getMessage(), HttpStatus.BAD_REQUEST);
			
		} 

	}
	
	

//	주문 페이지
	@GetMapping(value = "/order/final")
	public String finalOrder(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

		Long id = (Long) session.getAttribute("memberId");

		Object obj = session.getAttribute("orderList");

//		저장된 주문 상품들이 없다면 메인페이지로 강제이동
		if (obj == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "정상적인 접근이 아닙니다.");
			return "redirect:/";
		}
		
		List<OrderDto> orderList = (List<OrderDto>) obj;

		Map<String, Integer> priceMap = orderService.orderPriceMath(orderList);
		
		Member member = memberService.findMember(id);

		String orderNumber = orderService.orderNumberCreate();

		model.addAttribute("orderList", orderList);
		model.addAttribute("member", member);
		model.addAttribute("totalCount", priceMap.get("totalCount"));
		model.addAttribute("deleveryCount", priceMap.get("deleveryCount"));
		model.addAttribute("saleCount", priceMap.get("saleCount"));
		model.addAttribute("finalCount", priceMap.get("finalCount"));
		model.addAttribute("orderNumber", orderNumber);

		return "order";
		
	}

//	결제전 체크
	@PostMapping(value = "/order/itemCheck")
	public ResponseEntity<String> itemCheck(@Valid @ModelAttribute OrderPayDto orderPayDto,BindingResult bindingResult , HttpSession session){

		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
//			결제 전/후 유효성 검사 
			orderService.bindingResultErrorCheck(bindingResult);
			
//			데이터베이스에서 데이터 검증
			orderService.itemCheck(orderPayDto,memberId);

			return new ResponseEntity<>(HttpStatus.OK);
			
		} catch (CustomException e) {
			return new ResponseEntity<>("결제 실패하였습니다.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}

	}
	
	

//		결제후 마지막 체크 후 결제정보 저장
	@PostMapping(value = "/order/payCheck")
	public ResponseEntity<String> payCheck(@Valid @ModelAttribute OrderPayDto orderPayDto, BindingResult bindingResult , HttpSession session) throws CustomException, FindNotException {
		
		Long memberId = (Long) session.getAttribute("memberId");


		try {
			
//			결제 전/후 유효성 검사 
			orderService.bindingResultErrorCheck(bindingResult);

//			실구매정보 불러와서 구매검증 없을시 에러를 던짐
			iamPortService.buyInfoCheck(orderPayDto);

//			데이터베이스에서 데이터 검증
			orderService.itemCheck(orderPayDto, memberId);
			
//			order객체를 생성해 주문한 회원정보/상품리스트정보/주문정보/결제정보를 저장한다.
			orderService.orderSave(orderPayDto,memberId);

//			CustomException 에러발생시 자동환불처리
		} catch (CustomException e) {
			
//			자동환불실행에 FindNotException 에러발생시 INTERNAL_SERVER_ERROR 로 이동
				try {
					iamPortService.cancelBuy(orderPayDto);
				
				} catch (FindNotException e2) {
					return new ResponseEntity<>("자동 환불처리 실패하였습니다. 관리자에게 문의 바랍니다.\n"+e2.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				
				}

			return new ResponseEntity<>("결제정보 저장 실패하였습니다. 자동 환불처리 됩니다.\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
//			주문한 장바구니도 삭제 // 에러발생시 409 에러후 장바구니 에러발생을 사용자에게 알리고 결제성공 페이지 이동
		try {
			ItemOrderDto cartList = (ItemOrderDto) session.getAttribute("cartList");

			if (cartList != null) {

				session.removeAttribute("cartList");
				session.removeAttribute("orderList");

				orderService.orderCartDelete(cartList);
			}

		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		}

//		주문완료후 로그아웃후 다시 로그인했을때 주문완료 페이지를 못가도록 세션에 저장 (로그아웃시 세션초기화 전략)
		String thanks = "ok";
		session.setAttribute("thanks", thanks);
		
		String orderNumber = orderPayDto.getOrderNumber();
		return new ResponseEntity<>(orderNumber, HttpStatus.OK);
	}

//	주문완료페이지 
	@GetMapping(value = "/order/thank/{orderNumber}")
	public String orderOk(@PathVariable("orderNumber") String orderNumber, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {

		String thanks = (String) session.getAttribute("thanks");
		Order order = orderService.orderFindByOrderNumber(orderNumber);

		if (thanks == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "만료된 페이지입니다.");
			return "redirect:/";
		}

		if (order == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "결제 페이지 에러가 발생했습니다.");
			return "redirect:/";
		}

		BuyInfo buyInfo = order.getBuyInfo();

		List<OrderHistoryDto> orderHistoryList = orderService.orderHistoryList(order.getId());

		model.addAttribute("order", order);
		model.addAttribute("orderHistoryList", orderHistoryList);
		model.addAttribute("info", buyInfo);

		return "orderThank";
	}

}
