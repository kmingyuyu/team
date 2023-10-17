package com.recipe.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.recipe.constant.DeliveryStatus;
import com.recipe.constant.OrderStatus;
import com.recipe.entity.Delivery;
import com.recipe.entity.DeliveryInfo;
import com.recipe.entity.Order;
import com.recipe.entity.OrderItem;
import com.recipe.exception.CustomException;
import com.recipe.repository.DeliveryInfoRepository;
import com.recipe.repository.DeliveryRepository;
import com.recipe.repository.OrderItemRepository;
import com.recipe.repository.OrderRepository;

import jakarta.annotation.PreDestroy;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final OrderRepository orderRepository;

	private final OrderItemRepository orderItemRepository;

	private final DeliveryInfoRepository deliveryInfoRepository;

	private final DeliveryRepository deliveryRepository;

	private final Logger log = LoggerFactory.getLogger(DeliveryService.class);

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

//	   실행시 택배사 물류진행
	@Transactional
	public void orderDeliveryRequest(Map<String, Integer> durationMap, Order order, Delivery delivery) {

		try {
			orderDeliveryStart(order, delivery, durationMap);

		} catch (RuntimeException e) {
			String errorMessage = e.getMessage();
			logErrorAndThrowCustomException(errorMessage, order, delivery);
		}

	}

	public void orderDeliveryStart(Order order, Delivery delivery, Map<String, Integer> durationMap) {

		int waypoint = durationMap.get("waypoint");
		int destination = durationMap.get("destination");

		scheduler.schedule(() -> {
			try {
				deliveryStartUpdate(delivery, order);
				scheduler.schedule(() -> {
					try {
						delieveryStartMessageUpdate_1(delivery);
						scheduler.schedule(() -> {
							try {
								delieveryStartMessageUpdate_2(delivery);
								scheduler.schedule(() -> {
									try {
										delieveryStartMessageUpdate_3(delivery, order);
										scheduler.schedule(() -> {
											try {
												deliveryOkUpdate(delivery, order);
											} catch (Exception e) {
												throw new RuntimeException("deliveryOkUpdate error", e);
											}
										}, 3, TimeUnit.SECONDS);
									} catch (Exception e) {
										throw new RuntimeException("delieveryStartMessageUpdate_3 error", e);
									}
								}, 3, TimeUnit.SECONDS);
							} catch (Exception e) {
								throw new RuntimeException("delieveryStartMessageUpdate_2 error", e);
							}
						}, 3, TimeUnit.SECONDS);
					} catch (Exception e) {
						throw new RuntimeException("delieveryStartMessageUpdate_1 error", e);
					}
				}, 3, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw new RuntimeException("deliveryStartUpdate error.", e);
			}
		}, 1, TimeUnit.SECONDS);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void logErrorAndThrowCustomException(String errorMessage, Order order, Delivery delivery) {
		
		if (delivery != null && order != null) {
			
			OrderStatus orderStatus = OrderStatus.주문완료;

			orderItemSave(order, orderStatus);

			deliveryRepository.delete(delivery);
		}

		log.error(errorMessage);
		throw new RuntimeException(new CustomException(errorMessage));
	}

	// 택배사에 배송요청
	public void deliverySave(Order order) {

		OrderStatus orderStatus = OrderStatus.발송요청완료;

		orderItemSave(order, orderStatus);

		Delivery delivery = Delivery.createOrder(order);

		deliveryRepository.save(delivery);
	}

//  배송시작
	public void deliveryStartUpdate(Delivery delivery, Order order) {


		OrderStatus orderStatus = OrderStatus.배송준비중;
		
		orderItemSave(order, orderStatus);
		
		String invoiceNumber = invoiceCreate();
		delivery.setInvoiceNumber(invoiceNumber);
		DeliveryStatus deliveryStatus = DeliveryStatus.배송준비중;
		String message = "택배사로 상품 이동중입니다.";
		
		DeliveryAndDeliveryInfoSave(deliveryStatus, delivery, message);

		log.info("요청 수신되었습니다. 송장번호 생성되었습니다. 송장번호 : " + delivery.getInvoiceNumber());

	}

//	배송상태 업데이트_1
	public void delieveryStartMessageUpdate_1(Delivery delivery) {

		String message = "고객님의 상품이 집하되어 입고 되었습니다.";

		DeliveryInfo deliveryInfo = DeliveryInfo.messageUpdate(message, delivery);

		deliveryRepository.save(delivery);
		deliveryInfoRepository.save(deliveryInfo);

		log.info("배송정보 업데이트 되었습니다 : 상품집하 ");

	}

//	배송상태 업데이트_2
	public void delieveryStartMessageUpdate_2(Delivery delivery) {

		delivery.createDeliveryPerson();

		String message = "배송원이 배정되었습니다(배송원 : " + delivery.getDeliveryPerson().getDeliveryPerson() + ") 배송원이 배송 준비중입니다.";

		DeliveryInfo deliveryInfo = DeliveryInfo.messageUpdate(message, delivery);

		deliveryRepository.save(delivery);
		deliveryInfoRepository.save(deliveryInfo);

		log.info("배송정보 업데이트 되었습니다 : 배송원 배정");

	}

//	배송상태 업데이트_3
	public void delieveryStartMessageUpdate_3(Delivery delivery, Order order) {

		OrderStatus orderStatus = OrderStatus.배송중;
		orderItemSave(order, orderStatus);

		DeliveryStatus deliveryStatus = DeliveryStatus.배송중;
		String message = "배송출발 하였습니다.";
		DeliveryAndDeliveryInfoSave(deliveryStatus, delivery, message);

		log.info("배송정보 업데이트 되었습니다 : 배송출발");
		
	}

//	배송완료
	public void deliveryOkUpdate(Delivery delivery, Order order) {

		OrderStatus orderStatus = OrderStatus.배송완료;
		orderItemSave(order, orderStatus);

		DeliveryStatus deliveryStatus = DeliveryStatus.배송완료;
		String message = "배송완료 되었습니다.";
		DeliveryAndDeliveryInfoSave(deliveryStatus, delivery, message);

		log.info("배송정보 업데이트 되었습니다 : 배송완료");
	}

//	배송전 상태인 배송목록 불러오기
	public List<Delivery> findDelivery() {
		List<Delivery> DeliveryList = deliveryRepository.findByDeliveryStatus(DeliveryStatus.배송전);
		return DeliveryList;
	}

//	발송요청완료 상태인 주문목록 불러오기
	public List<Order> findOrder() {
		List<Order> orderList = orderRepository.findByOrderStatus(OrderStatus.발송요청완료);
		return orderList;
	}

//	상태 업데이트
	public void DeliveryAndDeliveryInfoSave(DeliveryStatus deliveryStatus, Delivery delivery, String message) {

		delivery.setDeliveryStatus(deliveryStatus);
		DeliveryInfo deliveryInfo = DeliveryInfo.messageUpdate(message, delivery);

		deliveryRepository.save(delivery);
		deliveryInfoRepository.save(deliveryInfo);
	}

//	orderItem 상태 업데이트
	public void orderItemSave(Order order, OrderStatus orderStatus) {

		List<OrderItem> orderItemList = order.getOrderItems();
		for (OrderItem orderitem : orderItemList) {
			orderitem.setOrderStatus(orderStatus);
			orderItemRepository.save(orderitem);
		}

	}

//  송장번호 생성/중복체크
	public String invoiceCreate() {
		while (true) {
			String invoice = generateRandomString();
			Delivery delivery = findByInvoiceNumber(invoice);
			if (delivery == null) {
				return invoice;
			}

		}
	}
	
	public Delivery findByInvoiceNumber(String invoice) {
		Delivery delivery = deliveryRepository.findByInvoiceNumber(invoice);
		return delivery;
	}
	

//  랜덤한 글자 생성
	public static String generateRandomString() {
		// 현재 날짜를 yyyyMMdd 형식으로 포맷팅
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String formattedDateTime = currentDateTime.format(formatter);

		// 랜덤한 영문 5자리 생성
		Random random = new Random();
		StringBuilder randomChars = new StringBuilder();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < 10; i++) {
			char randomChar = alphabet.charAt(random.nextInt(alphabet.length()));
			randomChars.append(randomChar);
		}

		// 최종 문자열 생성
		String finalString = formattedDateTime + randomChars.toString() + "KR";

		return finalString;
	}
	

	@PreDestroy
	public void preDestroy() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}

}
