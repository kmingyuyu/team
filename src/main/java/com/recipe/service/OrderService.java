package com.recipe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.constant.ImgMainOk;
import com.recipe.constant.ItemSellStatus;
import com.recipe.constant.PointEnum;
import com.recipe.dto.CartDto;
import com.recipe.dto.ItemDetailDto;
import com.recipe.dto.ItemOrderDto;
import com.recipe.dto.OrderDto;
import com.recipe.dto.OrderPayDto;
import com.recipe.entity.BuyInfo;
import com.recipe.entity.Cart;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.Member;
import com.recipe.entity.Order;
import com.recipe.entity.OrderItem;
import com.recipe.entity.Point;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.myPage.dto.OrderHistoryDto;
import com.recipe.repository.CartRepository;
import com.recipe.repository.ItemImgRepository;
import com.recipe.repository.ItemRepository;
import com.recipe.repository.MemberRepository;
import com.recipe.repository.OrderRepository;
import com.recipe.repository.PointRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final MemberRepository memberRepository;

	private final CartRepository cartRepository;

	private final ItemRepository itemRepository;

	private final OrderRepository orderRepository;
	
	private final ItemImgRepository itemImgRepository;
	
	private final PointRepository pointRepository;
	
	private final IamPortService iamPortService;

	private final Logger log = LoggerFactory.getLogger(OrderService.class);
	
	
	
//	order객체/member 객체 저장
	@Transactional
	public void orderSave(OrderPayDto orderPayDto , Long memberId , HttpSession session)  throws CustomException {
		
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException("사유: 회원조회 실패"));
		
		List<OrderItem> orderItemList = addOrderItemList(orderPayDto);
		
		BuyInfo buyInfo = iamPortService.buyInfoInput(orderPayDto);
		
		if(orderPayDto.getUsePoint() !=0) {
			
			member.minusPoint(orderPayDto.getUsePoint());
			
			session.setAttribute("point", member.getPoint());
			
			StringBuilder sb = new StringBuilder();
			sb.append("주문번호: ");
			sb.append(orderPayDto.getOrderNumber());
			
			Point point = Point.createPoint(member, orderPayDto.getUsePoint(), PointEnum.MINUS ,"포인트 결제", sb.toString());
			pointRepository.save(point);
		}
		
		Order order = Order.createOrder(member, orderItemList, orderPayDto, buyInfo);
		
		orderRepository.save(order);
		
	}
	
//	order객체에 저장할 OrderItem의 배열을 생성
	@Transactional
	public List<OrderItem> addOrderItemList(OrderPayDto orderPayDto) throws CustomException {
		
		List<Long> ids = orderPayDto.getItemIds();
		List<Integer> counts = orderPayDto.getCounts();
		List<Integer> itemOrderPrices = orderPayDto.getItemOrderPrices();
		List<Integer> itemSalePrices = orderPayDto.getItemSalePrices();
		
		List<OrderItem> orderItemList = new ArrayList<>();
		
		for (int i = 0; i < ids.size(); i++) {
		
			Item item = itemRepository.findById(ids.get(i))
					.orElseThrow(() -> new CustomException("사유: 상품 조회 실패."));
			
		item.orderStockAndSellStatus(counts.get(i));
		
		OrderItem orderitem = OrderItem.createOrderItem(item, counts.get(i), itemOrderPrices.get(i), itemSalePrices.get(i));
		
		orderItemList.add(orderitem);
		}
		
		return orderItemList;
	}
	
//	주문번호로 order객체를 가져온다
	public Order orderFindByOrderNumber(String orderNumber) {

		Order order = orderRepository.findByOrderNumber(orderNumber);

		if (order == null) {
			return null;
		}

		String orderTakeTell = order.getTakeTell();
		if (orderTakeTell != null && orderTakeTell.length() == 11) {
			String phoneNumber = orderTakeTell.substring(0, 3) + "-" + orderTakeTell.substring(3, 7) + "-"
					+ orderTakeTell.substring(7);
			order.setTakeTell(phoneNumber);
		}

		return order;
	}
	
//	orderId로 주문내역의 상품상세 정보(itemImg테이블의 imgUrl포함)를 가져온다
	@Transactional(readOnly = true)
	public List<OrderHistoryDto> orderHistoryList(Long id) {
		List<OrderHistoryDto> orderHistoryList = itemRepository.orderHistoryList(id);
		return orderHistoryList;
	}
	

//	주문성공시 장바구니정보 삭제
	@Transactional
	public void orderCartDelete(ItemOrderDto cartList , HttpSession session) throws CustomException {

		List<Long> cartIds = cartList.getCartIds();
		
		Long cartCount = (Long) session.getAttribute("cartCount");
		
		
		if (cartIds == null || cartIds.isEmpty()) {
				return;
			
			}
			
		try {
				for (Long cartId : cartIds) {
						Cart cart = cartRepository.findById(cartId)
									.orElseThrow(() -> new CustomException("장바구니 처리 중 문제가 발생했습니다. 결제된 장바구니가 삭제 실패하였습니다."));
				
						cartRepository.delete(cart);
						cartCount--;
				}
				
				session.setAttribute("cartCount", cartCount);
				
		} catch (CustomException e) {
				log.error("orderCartDelete-error",e);
				throw e;
				
		}
	}
	
	
//	 결제 전/후 주문요청 금액과 실금액이랑 비교
	@Transactional(readOnly = true)
	public void itemCheck(OrderPayDto orderPayDto, Long memberId) throws CustomException {
		
		try {
			List<Long> ids = orderPayDto.getItemIds();
			List<Integer> counts = orderPayDto.getCounts();
			
			int totalPirce = 0; // 오리지널 총 금액
			int salePrice = 0; // 할인받는 총 금액
			int deleveryPrice = 4000; // 배송비 4000원
			int finalPrice = 0;
			
			for (int i=0; i<ids.size(); i++) {
				
				Item item = itemRepository.findById(ids.get(i)).orElseThrow(
		                () -> new CustomException("사유: 상품 조회 실패."));
				
				if(ItemSellStatus.SOLD_OUT.equals(item.getItemSellStatus())) {
					throw new CustomException("사유: 해당[" + item.getItemNm() +"]상품 품절");
					}
				
				if(counts.get(i) > item.getStockNumber()) {
					throw new CustomException("사유: 해당[" + item.getItemNm() +"]상품 재고 부족");
					}
				
				totalPirce += item.getPrice() * counts.get(i);
				salePrice += (item.getPrice() * item.getSale() / 100) * counts.get(i);
			}
			
			
			if ((totalPirce - salePrice) >= 40000) {
				deleveryPrice = 0;
			}
			
			finalPrice = totalPirce + deleveryPrice - salePrice - orderPayDto.getUsePoint();
			
			if(orderPayDto.getTotalPrice() != totalPirce || orderPayDto.getSalePrice() != salePrice || 
			   orderPayDto.getDeleveryPrice() != deleveryPrice || orderPayDto.getFinalPrice() != finalPrice) {
				throw new CustomException("사유: 주문금액과 결제 요청금액 불일치.");
			}
			
			
			
			Member member = memberRepository.findById(memberId).orElseThrow(
					() -> new CustomException("사유: 회원정보 조회 실패."));
			
			if(orderPayDto.getUsePoint() > member.getPoint()) {
				throw new CustomException("사유: 사용포인트가 보유포인트 초과.");
			}
			
		} catch (CustomException e) {
			throw e;
		}
		
	}
	
	
	
//	결제 전후 유효성검사
	public void bindingResultErrorCheck(BindingResult bindingResult) throws CustomException {
		
		if (bindingResult.hasErrors()) {
		    StringBuilder requiredFields = new StringBuilder();
		    List<FieldError> errors = bindingResult.getFieldErrors();
		    List<String> customErrors = new ArrayList<>();

		    for (FieldError error : errors) {
		        // 포인트 사용과 관련된 에러일 경우, customErrors에 메시지 추가
		        if ("usePoint".equals(error.getField())) {
		            customErrors.add("포인트사용 에러 : " + error.getDefaultMessage());
		        } else {
		            // 그 외의 경우, requiredFields에 추가
		            requiredFields.append(error.getDefaultMessage()).append(",");
		        }
		    }

		    StringBuilder finalMessage = new StringBuilder("사유:\n");

		    if (requiredFields.length() > 0) {
		        finalMessage.append("필수정보 에러[").append(requiredFields.substring(0, requiredFields.length() - 1)).append("]\n");
		    }

		    if (!customErrors.isEmpty()) {
		        for (String customError : customErrors) {
		            finalMessage.append(customError).append("\n");
		        }
		    }

		    throw new CustomException(finalMessage.toString());
		}
	}
	
	

	
	
//	주문페이지 가기전 장바구니 업데이트
	@Transactional
	public Map<Long , OrderDto> findOrderMapAndOrderUpdateCart(ItemOrderDto itemOrderDto) throws CustomException {
			
		Map<Long, OrderDto> orderMap = new HashMap<>();
			
		List<Long> ids = itemOrderDto.getIds();
		List<Long> cartIds = itemOrderDto.getCartIds();
		List<Integer> counts = itemOrderDto.getCounts();
		
			try {
				
				for (int i = 0; i < ids.size(); i++) {
				
					Long itemId = ids.get(i);
					Long cartId = cartIds.get(i);
					int count = counts.get(i);
					
					
					Cart cart = cartRepository.findById(cartId)
						.orElseThrow(() -> new CustomException("사유: 장바구니 조회 실패"));
				
					Map<String , Object> itemMap = findItemInfo(itemId);
					
					Item item = (Item) itemMap.get("item");
					
					ItemImg itemImg = (ItemImg) itemMap.get("itemImg");
					
					cart.setCount(count);
				
					if (orderMap.containsKey(itemId)) {
						OrderDto existingOrder = orderMap.get(itemId);
						existingOrder.setCount(existingOrder.getCount() + count);

					}else {
					
						OrderDto order = new OrderDto(); // 각 아이템에 대한 새로운 OrderDto 객체 생성
						order.setId(ids.get(i));
						order.setCartId(cartIds.get(i));
						order.setCount(counts.get(i));
						order.setItemNm(item.getItemNm());
						order.setSale(item.getSale());
						order.setPrice(item.getPrice());
						order.setImgUrl(itemImg.getImgUrl());

						orderMap.put(itemId, order);
					}
				
				
				}
				
			}  catch (CustomException e) {
				log.error("orderUpdateCart-error",e);
				throw e;
				
			}
			
			return orderMap;
	}
	
//	주문페이지 가기전 상품/상품이미지 찾기
	public  Map<String , Object> findItemInfo(Long itemId) throws CustomException {
		
		Map<String , Object> itemMap = new HashMap<>();
		
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new CustomException("사유: 상품 조회 실패"));
		
		ItemImg itemImg = itemImgRepository.findByItemIdAndImgMainOk(itemId, ImgMainOk.Y);
		
		if (itemImg == null) {
			throw new CustomException("사유: 상품 조회 실패");
		}
		
		itemMap.put("item", item);
		itemMap.put("itemImg", itemImg);
		
		return itemMap;
		
	}
	
//	주문페이지 가기전 상품 저장
	public List<OrderDto> addOrderList(ItemOrderDto itemOrderDto) throws CustomException{
		
		Map<String , Object> itemMap = findItemInfo(itemOrderDto.getId());
		
		Item item = (Item) itemMap.get("item");
		
		ItemImg itemImg = (ItemImg) itemMap.get("itemImg");
		
		OrderDto order = new OrderDto();
		
		order.setId(itemOrderDto.getId());
		order.setCount(itemOrderDto.getCount());
		order.setImgUrl(itemImg.getImgUrl());
		order.setItemNm(item.getItemNm());
		order.setPrice(item.getPrice());
		order.setSale(item.getSale());
		
		List<OrderDto> orderList = new ArrayList<>();
		orderList.add(order);
		
		return orderList;
	}
	
	
	
	

//	주문페이지 가기전 체크
	public void orderCheck(List<OrderDto> orderList) throws FindNotException, CustomException {

		try {

			for (OrderDto orderDto : orderList) {

				Item item = itemRepository.findById(orderDto.getId())
						.orElseThrow(() -> new FindNotException("사유: 상품 조회 실패"));

				if (ItemSellStatus.SOLD_OUT.equals(item.getItemSellStatus())) {
					throw new CustomException("사유: 해당[" + item.getItemNm() +"]상품 품절");
				}

				if (orderDto.getCount() > item.getStockNumber()) {
					throw new CustomException("사유: 해당[" + item.getItemNm() +"]상품 재고 부족");
				}

			}

		} catch (FindNotException e) {
			log.error(e.getMessage());
			throw e;

		} catch (CustomException e) {
			throw e;

		}
	}
	
	
	public boolean orderNumberCheck(String orderNumber) {

		Order order = orderRepository.findByOrderNumber(orderNumber);

		if (order == null) {
			return false;
		}

		return true;
	}
	

//	랜덤번호생성 중복이면 다시 반복 하는 메소드
	public String orderNumberCreate() {
		while (true) {
			String firstPart = generateRandomDigits(10);
			String secondPart = generateRandomDigits(10);

			String orderNumber = firstPart + "-" + secondPart;
			boolean ck = orderNumberCheck(orderNumber);

			if (!ck) {
				return orderNumber;
			}

		}
	}

//	랜덤번호생성 메소드
	public static String generateRandomDigits(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append(random.nextInt(10)); // 0 ~ 9 까지의 랜덤 숫자를 append
		}

		return sb.toString();
	}

//	 주문페이지 결제금액 계산
	public Map<String, Integer> orderPriceMath(List<OrderDto> orderList) {
		
		Map<String, Integer> priceMap = new HashMap<>();

		int totalCount = 0; // 오리지널 총 금액
		int saleCount = 0; // 할인받는 총 금액
		int deleveryCount = 4000; // 배송비 4000원
		int finalCount = 0; // 최종 결제금액

		for (OrderDto countPlus : orderList) {
			totalCount += countPlus.getPrice() * countPlus.getCount();
			saleCount += (countPlus.getPrice() * countPlus.getSale() / 100) * countPlus.getCount();
		}
		if ((totalCount - saleCount) >= 40000) {
			deleveryCount = 0;
		}
		
		finalCount = totalCount + deleveryCount - saleCount;
		
		priceMap.put("totalCount", totalCount);
		priceMap.put("saleCount", saleCount);
		priceMap.put("deleveryCount", deleveryCount);
		priceMap.put("finalCount", finalCount);
		
		return priceMap;
	}

}
