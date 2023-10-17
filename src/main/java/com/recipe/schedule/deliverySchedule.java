package com.recipe.schedule;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.recipe.entity.Delivery;
import com.recipe.entity.Order;
import com.recipe.exception.CustomException;
import com.recipe.service.DeliveryService;
import com.recipe.service.KakaoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class deliverySchedule {

	private final KakaoService kakaoService;

	private final DeliveryService deliveryService;
	
	private final Logger log = LoggerFactory.getLogger(deliverySchedule.class);
	
	
//	매일 오전6시 배송요청받은 상품 택배사에 수거요청
	@Scheduled(cron = "0 0 06 * * ?") 
	public void orderDeliverySchedule() {
        try {
			orderDelivery();
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
    }
	
	public void orderDelivery() throws CustomException {

		List<Delivery> deliveryList = deliveryService.findDelivery();

		List<Order> orderList = deliveryService.findOrder();

		if (deliveryList.isEmpty() && orderList.isEmpty() && deliveryList.size() == orderList.size()) {
			return;
		}

		try {
			for (int i = 0; i < orderList.size(); i++) {
				
				String sendAddress = deliveryList.get(i).getDeliveryAddress().getSendAddress();
				
				String receiveAddress = deliveryList.get(i).getDeliveryAddress().getReceiveAddress();
				
				
				Map<String, Integer> durationMap = kakaoService.getDuration(sendAddress, receiveAddress);
					

					deliveryService.orderDeliveryRequest(durationMap, orderList.get(i),deliveryList.get(i));
			}

		} catch (Exception e) {
			throw e;
		}

	}
	
	

	

}
