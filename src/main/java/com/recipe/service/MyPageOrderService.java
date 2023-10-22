package com.recipe.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.constant.CancelStatus;
import com.recipe.constant.ImgMainOk;
import com.recipe.constant.OrderStatus;
import com.recipe.constant.PointEnum;
import com.recipe.entity.CancelInfo;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.ItemReview;
import com.recipe.entity.ItemReviewImg;
import com.recipe.entity.Member;
import com.recipe.entity.Order;
import com.recipe.entity.OrderItem;
import com.recipe.entity.Point;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.myPage.dto.OrderHistoryDto;
import com.recipe.repository.ItemImgRepository;
import com.recipe.repository.ItemReviewImgRepository;
import com.recipe.repository.ItemReviewRepository;
import com.recipe.repository.MemberRepository;
import com.recipe.repository.OrderItemRepository;
import com.recipe.repository.OrderRepository;
import com.recipe.repository.PointRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageOrderService {
	
	private final OrderRepository orderRepository;
	
	private final OrderItemRepository orderItemRepository;
	
	private final ItemImgRepository itemImgRepository;
	
	private final ItemReviewRepository itemReviewRepository;
	
	private final ItemReviewImgRepository itemReviewImgRepository;
	
	private final PointRepository pointRepository;
	
	private final MemberRepository memberRepository;
	
	private final FileService fileService;
	
	private final IamPortService iamPortService;
	
//	주문상품 목록 가져오기(myPage/order)
	@Transactional(readOnly = true)
	public Page<OrderHistoryDto> findByMyOrderList(Long id, Pageable pageable, MyPageSerchDto myPageSerchDto) {

		LocalDate startDateFromClient = myPageSerchDto.getStartTime();
		LocalDate endDateFromClient = myPageSerchDto.getEndTime();

		LocalDateTime startDateTime = startDateFromClient.atStartOfDay();
		LocalDateTime endDateTime = endDateFromClient.atTime(23, 59, 59, 999999);

		Page<OrderHistoryDto> orderList = orderRepository.findByMyOrderList(id, startDateTime, endDateTime,
				myPageSerchDto.getSearchQuery(), myPageSerchDto.getData(), pageable);

		return orderList;
	}
	

	
//	주문상품 구매확정(myPage/order)
	@Transactional
	public void orderConfirmed(Map<String, Object> requestBody) throws FindNotException {

		Long orderItemId = Long.parseLong(requestBody.get("orderItemId").toString());

		try {
			OrderItem orderItem = orderItemRepository.findById(orderItemId)
					.orElseThrow(() -> new FindNotException("사유: 주문상품 조회 실패"));

			orderItem.setOrderStatus(OrderStatus.구매확정);

		} catch (Exception e) {
			log.error("orderConfirmed-error", e);
			throw e;
		}
	}
	
//	주문상품 주문취소(myPage/order)
	@Transactional
	public void orderCancel(@RequestBody Map<String, Object> requestBody, HttpSession session) throws CustomException {
			
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
			Long orderItemId = Long.parseLong(requestBody.get("orderItemId").toString());

			Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 회원 조회 실패"));

			OrderItem orderItem = orderItemRepository.findById(orderItemId)
					.orElseThrow(() -> new CustomException("사유: 주문상품 조회 실패"));

			Order order = orderItem.getOrder();

			List<OrderItem> orderItemList = order.getOrderItems();

			if (!OrderStatus.주문완료.equals(orderItem.getOrderStatus())) {
				throw new CustomException("사유: 배송상태 오류");
			}

			int cancelPrice = 0;
			int cancelDeliveryPrice = 0;

			int otherPrice = 0;
			
			StringBuilder sb = new StringBuilder();
			sb.append("주문번호: ");
			sb.append(order.getOrderNumber());

			
//			하나의 order에서 상품종류가 1개일때
//			cancel객체 생성안되어 있으므로 생성
//			아임포트 결제취소 
			if (order.getOrderItems().size() == 1) {
				iamPortService.cancelBuy(order);

				cancelPrice = order.getFinalPrice();

				CancelInfo cancelInfo = CancelInfo.createCancelInfo(order, cancelPrice, cancelDeliveryPrice,
						CancelStatus.전체취소, order.getUsePoint());

				orderItem.setOrderStatus(OrderStatus.환불완료);
				order.setCancelInfo(cancelInfo);
				
				if(order.getUsePoint() !=0) {
					member.plusPoint(order.getUsePoint());
					session.setAttribute("point", member.getPoint());
					
					Point point = Point.createPoint(member, order.getUsePoint(), PointEnum.PLUS , "배송전 취소", sb.toString());
					pointRepository.save(point);
				}

			}

//			하나의 order에서 상품종류가 1개가 아닐때
			else {

				int cancelCount = 0;

				for (OrderItem item : orderItemList) {
					if (OrderStatus.환불완료.equals(item.getOrderStatus())) {
						cancelCount++;
					}
				}

//				하나의 order에서 상품종류가 1개가 아닐때 , 환불처리된 상품이 없을때
//				cancel객체 생성안되어 있으므로 생성
				if (cancelCount == 0) {
					cancelPrice = orderItem.getOrderPrice();

					otherPrice = order.getFinalPrice() - cancelPrice;

					if (otherPrice < 40000) {
						cancelDeliveryPrice = 4000;
						cancelPrice -= 4000;
					}

					CancelInfo cancelInfo = CancelInfo.createCancelInfo(order, cancelPrice, cancelDeliveryPrice,
							CancelStatus.부분취소, 0);

					orderItem.setOrderStatus(OrderStatus.환불완료);
					order.setCancelInfo(cancelInfo);

				}

//				하나의 order에서 상품종류가 1개가 아닐때 , 환불처리된 상품이 있지만 총 상품수보다 환불처리된 상품이 적을떄
//				cancel객체 생성되어있으므로 수정
				if (cancelCount != 0 && cancelCount != orderItemList.size() - 1) {
					CancelInfo cancelInfo = order.getCancelInfo();

					cancelPrice = orderItem.getOrderPrice();

					otherPrice = order.getFinalPrice() - (cancelPrice + cancelInfo.getCancelPrice());

					if (cancelInfo.getCancelDeliveryPrice() != 4000 || otherPrice < 40000) {
						cancelPrice -= 4000;
						cancelDeliveryPrice = 4000;
					}

					cancelInfo.setCancelDeliveryPrice(cancelDeliveryPrice);
					cancelInfo.setCancelPrice(cancelPrice + cancelInfo.getCancelPrice());
					orderItem.setOrderStatus(OrderStatus.환불완료);
				}

//				하나의 order에서 상품종류가 1개가 아닐때 , order의 상품들이 요청들어온 상품과 합쳐서 모두 품절처리 될때
//				cancel 객체 생성되어있으므로 수정
//				아임포트 결제취소 
				if (cancelCount == orderItemList.size() - 1) {
					iamPortService.cancelBuy(order);

					CancelInfo cancelInfo = order.getCancelInfo();

					cancelInfo.setCancelDeliveryPrice(0);
					cancelInfo.setCancelPrice(order.getFinalPrice());
					cancelInfo.setCancelStatus(CancelStatus.전체취소);
					cancelInfo.setCancelPoint(order.getUsePoint());
					orderItem.setOrderStatus(OrderStatus.환불완료);
					
					if(order.getUsePoint() !=0) {
						member.plusPoint(order.getUsePoint());
						session.setAttribute("point", member.getPoint());
						
						Point point = Point.createPoint(member, order.getUsePoint(), PointEnum.PLUS , "배송전 취소", sb.toString());
						pointRepository.save(point);
					}

				}

			}

		} catch (Exception e) {
			log.error("orderCancel-error", e);
			throw e;

		}

	}
	
//	주문상품 리뷰작성 팝업창 주문상품정보 가져오기(myPage/order/itemReview_popup_reg)
	@Transactional(readOnly = true)
	public Map<String, Object> findByOrderItem(Long orderItemId, Long memberId) throws CustomException {

		Map<String, Object> orderItemMap = new HashMap<>();

		try {

			OrderItem orderItem = orderItemRepository.findById(orderItemId)
					.orElseThrow(() -> new CustomException("사유: 주문상품 조회 실패"));

			Item item = orderItem.getItem();

			ItemImg itemImg = itemImgRepository.findByItemIdAndImgMainOk(item.getId(), ImgMainOk.Y);

			ItemReview itemReview = itemReviewRepository.findByOrderItemId(orderItemId);

			if (!OrderStatus.구매확정.equals(orderItem.getOrderStatus())) {
				throw new CustomException("사유: 주문상품 구매확정 상태X");
			}

			if (itemImg == null) {
				throw new CustomException("사유: 주문상품 조회 실패");
			}

			if (itemReview != null) {
				throw new CustomException("사유: 이미 등록된 리뷰");
			}

			orderItemMap.put("orderItem", orderItem);
			orderItemMap.put("itemImg", itemImg);
			orderItemMap.put("item", item);

		} catch (CustomException e) {
			log.error("findByOrderItem-error", e);
			throw e;

		}

		return orderItemMap;
	}
	
//	주문상품 리뷰작성 팝업창 리뷰 등록(myPage/order/itemReview_popup_reg)
	@Transactional
	public void orderItemReviewReg(MultipartFile[] files, double star, String content, Long orderItemId , HttpSession session)
			throws IOException, CustomException {

		int p;
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {

			OrderItem orderItem = orderItemRepository.findById(orderItemId)
					.orElseThrow(() -> new CustomException("사유: 주문상품 조회 실패"));

			Member member = memberRepository.findById(memberId)
					.orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));

			Item item = orderItem.getItem();

			if (star == 0 || star > 5.0) {
				throw new CustomException("사유: 별점 오류");
			}

			String contentCheck = content.replaceAll("\\s", "");
			if (content == null || contentCheck.length() > 350) {
				throw new CustomException("사유: 후기내용 오류");
			}

			p = files == null ? 500 : 1000;
			String info = files == null ? "일반후기 등록" : "포토후기 등록";

			ItemReview itemReview = ItemReview.createItemReview(member, item, orderItem, content, star, files);

			itemReviewRepository.save(itemReview);

			String orderNumber = orderItem.getOrder().getOrderNumber();

			String itemNm = item.getItemNm();

//		이미지 파일이 있다면 이미지파일 itemReivewImg 생성
			if (files != null) {
				
				if(files.length > 8) {
					throw new CustomException("사유: 이미지 등록 갯수 제한");
				}

				String email = member.getEmail();

				String result = "C:/yummy/member/" + email + "/review/order/" + orderNumber + "/" + itemNm;

				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						String oriImgName = file.getOriginalFilename();
						String imgName = fileService.uploadFileImg(result, oriImgName, file.getBytes());
						String imgUrl = "/img/member/" + email + "/review/order/" + orderNumber + "/" + itemNm + "/"
								+ imgName;

						ItemReviewImg itemReviewImg = ItemReviewImg.createItemReviewImg(imgUrl, imgName, oriImgName,
								itemReview);
						itemReviewImgRepository.save(itemReviewImg);
					}
				}
			}

			StringBuilder sb = new StringBuilder();
			sb.append("주문번호: ");
			sb.append(orderNumber);
			sb.append(",");
			sb.append("상품명: ");
			sb.append(itemNm);
			
			member.plusPoint(p);
			
			session.setAttribute("point", member.getPoint());
			
			Point point = Point.createPoint(member, p, PointEnum.PLUS , info, sb.toString());
			pointRepository.save(point);

		} catch (IOException e) {
			log.error("orderItemReviewReg -> uploadFileImg-error", e);
			throw new IOException("사유: 이미지 저장 오류");

		} catch (CustomException e) {
			log.error("orderItemReviewReg-error", e);
			throw e;

		}

	}
	

	
//	주문번호로 order객체를 가져온다(myPage/order/detail)
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
	
//	주문상품 주문내역 삭제(myPage/order/detail)
	public void orderDelete(Order order) throws CustomException {

		try {
			if (order == null) {
				throw new CustomException("사유: 주문번호 조회 실패");
			}

			List<OrderItem> orderItemList = order.getOrderItems();

			for (OrderItem orderItem : orderItemList) {

				if (!OrderStatus.배송완료.equals(orderItem.getOrderStatus())
						&& !OrderStatus.구매확정.equals(orderItem.getOrderStatus())
						&& !OrderStatus.환불완료.equals(orderItem.getOrderStatus())) {

					throw new CustomException("사유: 배송상태 오류");
				}
			}

			orderRepository.delete(order);

		} catch (Exception e) {
			log.error("orderDelete-error", e);
			throw e;

		}
	}

}
