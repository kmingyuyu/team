package com.recipe.myPage.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.constant.OrderStatus;
import com.recipe.entity.BuyInfo;
import com.recipe.entity.CancelInfo;
import com.recipe.entity.Delivery;
import com.recipe.entity.DeliveryAddress;
import com.recipe.entity.DeliveryInfo;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.Order;
import com.recipe.entity.OrderItem;
import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.myPage.dto.OrderHistoryDto;
import com.recipe.service.CountService;
import com.recipe.service.DeliveryService;
import com.recipe.service.GlobalService;
import com.recipe.service.MyPageOrderService;
import com.recipe.service.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageOrderController {
	
	private final MyPageOrderService myPageOrderService;
	
	private final CountService countService;
	
	private final GlobalService globalService;
	
	private final DeliveryService deliveryService;
	
	private final OrderService orderService;
	
	
	// 마이페이지 보여주기 (주문내역)
	@GetMapping(value = {"/myPage/order" , "/myPage/order/{page}" })
	public String myPageOrder(@PathVariable("page") Optional<Integer> page ,HttpSession session, Model model , MyPageSerchDto myPageSerchDto) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Pageable Pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		
		Page<OrderHistoryDto> OrderList = myPageOrderService.findByMyOrderList(memberId , Pageable , myPageSerchDto);
		
		Map<String, Long> countMap = countService.myPageOrderStatusCount(memberId);
		
		model.addAttribute("OrderList", OrderList);
		model.addAttribute("myPageSerchDto", myPageSerchDto);
		model.addAttribute("countMap", countMap);
		model.addAttribute("maxPage", 5); 
		
		return "myPage/order/myOrder";
	}
	
//	마이 페이지 보여주기 (주문내역 - > 배송 내역(팝업))
	@GetMapping("/myPage/order/deliveryPopup/{invoiceNumber}")
	public String deliveryPopup(@PathVariable("invoiceNumber") String invoiceNumber ,  Model model) {
		
		if(!globalService.isAuthenticated()){
			model.addAttribute("loginNo");
		}
		
		model.addAttribute("invoiceNumber" , invoiceNumber);
		
		Delivery delivery = deliveryService.findByInvoiceNumber(invoiceNumber);
		
		model.addAttribute("delivery", delivery); 
		
		if (delivery != null) {
			DeliveryAddress address = delivery.getDeliveryAddress();
			List<DeliveryInfo> infos = delivery.getDeliveryInfos();
			model.addAttribute("address", address); 
			model.addAttribute("infos", infos); 
		}
		
		
		return "myPage/order/deliveryPopup";
	}
	
//	마이 페이지 주문상품 구매확정 (주문내역)
	@PatchMapping("/myPage/order/orderConfirmed")
	public @ResponseBody ResponseEntity<String> orderConfirmed(@RequestBody Map<String, Object> requestBody) {
		
		try {
			myPageOrderService.orderConfirmed(requestBody);
			return new ResponseEntity<>("구매확정 되었습니다.", HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>("구매확정에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
	}
	
//	마이 페이지 주문상품 주문취소 (주문내역)
	@PatchMapping("/myPage/order/orderCancel")
	public  @ResponseBody ResponseEntity<String> orderCancel(@RequestBody Map<String, Object> requestBody , HttpSession session){
		
		try {
			
			Long memberId = (Long) session.getAttribute("memberId");
			
			myPageOrderService.orderCancel(requestBody,session);
			
		} catch (Exception e) {
			return new ResponseEntity<>("주문 취소에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<>("주문취소 되었습니다.", HttpStatus.OK);
	}
	
//	마이 페이지 보여주기 (주문내역 - > 상품 리뷰(팝업))
	@GetMapping("/myPage/order/itemReviewPopupReg/{orderItemId}")
	public String itemReviewPopupReg(@PathVariable("orderItemId") Long orderItemId , Model model , HttpSession session) {
			
		if(!globalService.isAuthenticated()){
			model.addAttribute("loginNo");
		}
		
		try {
			
			Long memberId = (Long) session.getAttribute("memberId");
			
			Map<String, Object> orderItemMap = myPageOrderService.findByOrderItem(orderItemId,memberId);
			
			OrderItem orderItem = (OrderItem) orderItemMap.get("orderItem");
			ItemImg itemImg = (ItemImg) orderItemMap.get("itemImg");
			Item item = (Item) orderItemMap.get("item");
			
			model.addAttribute("orderItem" , orderItem);
			model.addAttribute("itemImg" , itemImg);
			model.addAttribute("item" , item);
			
		} catch (CustomException e) {
			model.addAttribute("errorMessage" , "리뷰작성이 불가합니다. 잠시후에 시도해주세요.\n" + e.getMessage());
			
		}
		
		
		return "myPage/order/itemReviewPopupReg";
	}
	
//	마이페이지 주문상품 리뷰작성 (주문내역 - > 상품 리뷰(팝업))
	@PostMapping("/myPage/order/itemReviewPopupReg/regOk")
	public @ResponseBody ResponseEntity<String> orderItemReviewReg(
			HttpSession session ,
			@RequestParam("orderItemId") Long orderItemId,
			@RequestParam(value="imgFiles", required=false) MultipartFile[] files,
			@RequestParam("star") double star, 
			@RequestParam("content") String content) {
		
		if(!globalService.isAuthenticated()) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		try {
			myPageOrderService.orderItemReviewReg(files, star, content, orderItemId, session);
			
			return new ResponseEntity<>("후기 등록 되었습니다.", HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>("후기 등록에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
			
		} 
		
	}
	
//	마이 페이지 보여주기 (주문내역 - > 주문 상세내역)
	@GetMapping(value = {"/myPage/order/detail/{orderNumber}"})
	public String myPageOrderDetail(@PathVariable("orderNumber") String orderNumber,  Model model) {
		
		Order order = myPageOrderService.orderFindByOrderNumber(orderNumber);
		
//		주문번호가 없을시 페이지 이동
		if (order == null) {
			return "error/errorPage";
		}
		
		BuyInfo buyInfo = order.getBuyInfo();
		
		CancelInfo cancelInfo = order.getCancelInfo();

		List<OrderHistoryDto> orderHistoryList = orderService.orderHistoryList(order.getId());
		
		int deleteOk = 0;
		
		for(OrderHistoryDto history : orderHistoryList) {
		    if(!OrderStatus.배송완료.name().equals(history.getOrderStatus()) && 
		       !OrderStatus.구매확정.name().equals(history.getOrderStatus()) &&
		       !OrderStatus.환불완료.name().equals(history.getOrderStatus())) {
		        deleteOk++;
		    }
		}
		
		model.addAttribute("order", order);
		model.addAttribute("orderHistoryList", orderHistoryList);
		model.addAttribute("buyInfo", buyInfo);
		model.addAttribute("cancelInfo", cancelInfo);
		model.addAttribute("deleteOk", deleteOk);
		
		return "myPage/order/myOrderDetail";
	}
	
//	마이 페이지 주문내역 삭제 (주문내역 - > 주문 상세내역)
	@DeleteMapping("/myPage/order/detail/orderDelete")
	public @ResponseBody ResponseEntity<String> orderDelete(@RequestBody Map<String, Object> requestBody ) {
		
		String orderNumber = (requestBody.get("orderNumber").toString());
		
		Order order = orderService.orderFindByOrderNumber(orderNumber);
		
		try {
			myPageOrderService.orderDelete(order);
			
		} catch (Exception e) {
			return new ResponseEntity<>("주문내역 삭제에 실패 하였습니다. 잠시후에 다시 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>("주문내역 삭제 되었습니다.", HttpStatus.OK);
	}
}
