package com.recipe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.recipe.dto.IamPortBuyInfoDto;
import com.recipe.dto.IamPortCancelInfoDto;
import com.recipe.dto.IamPortPayInfoDto;
import com.recipe.dto.OrderPayDto;
import com.recipe.entity.BuyInfo;
import com.recipe.entity.Order;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
@Transactional
public class IamPortService {

	private final Logger log = LoggerFactory.getLogger(IamPortService.class);

	RestTemplate restTemplate = new RestTemplate();

	HttpHeaders headers = new HttpHeaders();

	JSONObject body = new JSONObject();

	@Value("${iamPortapiKey}")
	private String apiKey;

	@Value("${iamPortApiSecret}")
	private String apiSecret;

//  결제 취소(주문)
	public void cancelBuy(OrderPayDto orderPayDto) throws FindNotException {

		IamPortPayInfoDto iamPortPayInfoDto = getToken();

		try {
			if (iamPortPayInfoDto == null) {
				throw new FindNotException("사유: 토근검증 실패");
			}

			IamPortBuyInfoDto iamPortBuyInfoDto = searchInfo(iamPortPayInfoDto, orderPayDto);

			Object amountObj = iamPortBuyInfoDto.getResponse().get("amount");
			int price = 0;
			if (amountObj != null && amountObj instanceof Number) {
				price = ((Number) amountObj).intValue();
			}

			if (price != 0) {

				headerAndBodyClear();

				headers.add("Authorization", (String) iamPortPayInfoDto.getResponse().get("access_token"));
				body.put("imp_uid", orderPayDto.getImp_uid());
				body.put("amount", price);

				HttpEntity<JSONObject> entity2 = new HttpEntity<JSONObject>(body, headers);
				IamPortCancelInfoDto cancel = restTemplate.postForObject("https://api.iamport.kr/payments/cancel",
						entity2, IamPortCancelInfoDto.class);

				if (cancel == null) {
					throw new FindNotException("사유: 환불정보 조회 실패");
				}
			}

		} catch (FindNotException e) {
			log.error("cancelBuy-error", e);
			throw e;

		} finally {
			headerAndBodyClear();
		}

	}
	
//  결제 취소(배송전 취소)
	public void cancelBuy(Order order) throws FindNotException {
		
		IamPortPayInfoDto iamPortPayInfoDto = getToken();
		
		try {
			if (iamPortPayInfoDto == null) {
				throw new FindNotException("토큰 검증 실패입니다");
			}
			
			IamPortBuyInfoDto iamPortBuyInfoDto = searchInfo(iamPortPayInfoDto, order);
			
			Object amountObj = iamPortBuyInfoDto.getResponse().get("amount");
			int price = 0;
			if (amountObj != null && amountObj instanceof Number) {
				price = ((Number) amountObj).intValue();
			}
			
			if (price != 0) {
				
				headerAndBodyClear();
				
				headers.add("Authorization", (String) iamPortPayInfoDto.getResponse().get("access_token"));
				body.put("imp_uid", order.getImpUid());
				body.put("amount", price);
				
				HttpEntity<JSONObject> entity2 = new HttpEntity<JSONObject>(body, headers);
				IamPortCancelInfoDto cancel = restTemplate.postForObject("https://api.iamport.kr/payments/cancel",
						entity2, IamPortCancelInfoDto.class);
				
				if (cancel == null) {
					throw new FindNotException("사유: 결제내역 조회 실패.");
				}
			}
			
		} catch (FindNotException e) {
			log.error("cancelBuy-error", e);
			throw e;
			
		} finally {
			headerAndBodyClear();
		}
		
	}

//  결제정보 검증
	public void buyInfoCheck(OrderPayDto orderPayDto) throws CustomException {

		IamPortPayInfoDto iamPortPayInfoDto = getToken();

		try {
			if (iamPortPayInfoDto == null) {
				throw new FindNotException("사유: 토큰 검증 실패.");
			}

			IamPortBuyInfoDto iamPortBuyInfoDto = searchInfo(iamPortPayInfoDto, orderPayDto);

//			주석 풀면 결제정보 찍혀있음
//			System.out.println(iamPortBuyInfoDto + " <- iamPortBuyInfoDto");

			if (!orderPayDto.getOrderNumber().equals(iamPortBuyInfoDto.getResponse().get("merchant_uid"))) {
				throw new CustomException("사유: 주문번호와 결제요청 주문번호의 정보 불일치.");
			}

			if (!orderPayDto.getBuyerEmail().equals(iamPortBuyInfoDto.getResponse().get("buyer_email"))
					|| !orderPayDto.getBuyerName().equals(iamPortBuyInfoDto.getResponse().get("buyer_name"))) {
				throw new CustomException("사유: 결제자와 주문자의 정보 불일치.");
			}

			if (!orderPayDto.getAddress().equals(iamPortBuyInfoDto.getResponse().get("buyer_addr"))
					|| !orderPayDto.getPostCode().equals(iamPortBuyInfoDto.getResponse().get("buyer_postcode"))) {
				throw new CustomException("사유: 결제 주소 불일치.");
			}

		} catch (CustomException e) {
			throw e;

		} finally {
			headerAndBodyClear();
			
		}

	}

//	결제정보 엔티티 저장
	public BuyInfo buyInfoInput(OrderPayDto orderPayDto) {

		BuyInfo buyInfo = new BuyInfo();

		IamPortPayInfoDto iamPortPayInfoDto = getToken();

		IamPortBuyInfoDto iamPortBuyInfoDto = searchInfo(iamPortPayInfoDto, orderPayDto);

//		주석 풀면 결제정보 찍혀있음
//		System.out.println(iamPortBuyInfoDto + " <- iamPortBuyInfoDto");

		Object obj = null;
		String payInfo = null;

		obj = iamPortBuyInfoDto.getResponse().get("emb_pg_provider");
		if (obj != null && obj instanceof String) {
			payInfo = (String) obj;
			buyInfo.setPayProvider(payInfo);
		}

		obj = iamPortBuyInfoDto.getResponse().get("name");
		if (obj != null && obj instanceof String) {
			payInfo = (String) obj;
			buyInfo.setOrderItemName(payInfo);
		}

		obj = iamPortBuyInfoDto.getResponse().get("pay_method");
		if (obj != null && obj instanceof String) {
			payInfo = (String) obj;
			buyInfo.setPayMethod(payInfo);
		}

		obj = iamPortBuyInfoDto.getResponse().get("card_name");
		if (obj != null && obj instanceof String) {
			payInfo = (String) obj;
			buyInfo.setCardName(payInfo);
		}

		obj = iamPortBuyInfoDto.getResponse().get("bank_name");
		if (obj != null && obj instanceof String) {
			payInfo = (String) obj;
			buyInfo.setBankName(payInfo);
		}

		obj = iamPortBuyInfoDto.getResponse().get("card_number");
		if (obj != null && obj instanceof String) {
			payInfo = (String) obj;
			buyInfo.setCardNumber(payInfo);
		}

		obj = iamPortBuyInfoDto.getResponse().get("amount");
		int price = 0;
		if (obj != null && obj instanceof Number) {
			price = ((Number) obj).intValue();
			buyInfo.setAmount(price);
		}

		return buyInfo;
	}

//	결제 정보가져오기(주문)
	public IamPortBuyInfoDto searchInfo(IamPortPayInfoDto iamPortPayInfoDto, OrderPayDto orderPayDto) {

		headers.add("Authorization", (String) iamPortPayInfoDto.getResponse().get("access_token"));
		HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(headers);

		IamPortBuyInfoDto iamPortBuyInfoDto = restTemplate.postForObject(
				"https://api.iamport.kr/payments/" + orderPayDto.getImp_uid(), entity, IamPortBuyInfoDto.class);

		return iamPortBuyInfoDto;
	}
	
	
//	결제 정보가져오기(배송전 취소)
	public IamPortBuyInfoDto searchInfo(IamPortPayInfoDto iamPortPayInfoDto, Order order) {
		
		headers.add("Authorization", (String) iamPortPayInfoDto.getResponse().get("access_token"));
		HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(headers);
		
		IamPortBuyInfoDto iamPortBuyInfoDto = restTemplate.postForObject(
				"https://api.iamport.kr/payments/" + order.getImpUid(), entity, IamPortBuyInfoDto.class);
		
		return iamPortBuyInfoDto;
	}

//	토큰 발급
	public IamPortPayInfoDto getToken() {

		headers.setContentType(MediaType.APPLICATION_JSON);

		body.put("imp_key", apiKey);
		body.put("imp_secret", apiSecret);

		try {
			HttpEntity<JSONObject> entity = new HttpEntity<>(body, headers);

			IamPortPayInfoDto token = restTemplate.postForObject("https://api.iamport.kr/users/getToken", entity,
					IamPortPayInfoDto.class);

			return token;
		} catch (Exception e) {
			log.error("getToken-error", e);

		} finally {
			headerAndBodyClear();
			
		}

		return null;
	}

	private void headerAndBodyClear() {
		headers.clear();
		body.clear();
	}

}
