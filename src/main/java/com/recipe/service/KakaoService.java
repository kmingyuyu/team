package com.recipe.service;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.recipe.exception.CustomException;


@Service
@Transactional
public class KakaoService {
	
	RestTemplate restTemplate = new RestTemplate();

	HttpHeaders headers = new HttpHeaders();
	
	@Value("${kakaoApiKey}")
	private String apiKey;
	
	public Map<String, Integer> getDuration(String sendAddress , String receiveAddress) throws CustomException{
		
		Map<String, Integer> mapDuration = new HashMap<>();
		
		try {
			
//			출발지 x값 y값 
			Map<String, String> originsXandY = getAddressXAndY(sendAddress);
			
//			경유지(택배HUB) x값 y값 
			Map<String, String> waypointsXandY = getAddressXAndY("충청북도 옥천군 이원면 건진리 123");
			
//			도착지 x값 y값 
			Map<String, String> destinationXandY = getAddressXAndY(receiveAddress);
			
			Map<String, Map<String, String>> mapXandY = new HashMap<>();
			
			mapXandY.put("origins", originsXandY);
			mapXandY.put("waypoints", waypointsXandY);
			mapXandY.put("destination", destinationXandY);
			
			mapDuration = getAddressduration(mapXandY);
			
		} catch (Exception e) {
			throw e;
		}
		
		return mapDuration;
	}
	
	
//	지오코딩 x값 y값 구하기
	public Map<String, String> getAddressXAndY(String query) throws CustomException {
		
	    Map<String, String> map = new HashMap<>();
	    
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";

        try {

            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(apiUrl)
                    .queryParam("query",query)
                    .build();
            
            headers.add("Authorization", String.format("KakaoAK %s",apiKey));
            HttpEntity<String> requestMessage = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    uri.toUriString(),
                    HttpMethod.GET,
                    requestMessage,
                    String.class);

            JSONObject datas = new JSONObject(response.getBody().toString());
            JSONObject addressData = datas.getJSONArray("documents").getJSONObject(0).getJSONObject("address");
            String x = addressData.getString("x");
            String y = addressData.getString("y");
            
            
            map.put("x", x);
            map.put("y", y);
        } 
        
        catch (Exception e) {
            throw new CustomException("getAddressXAndY error");
        }  
        
        finally {
			headerClear();
		}

        return map;
    }
	
//	x값 y값으로 출발지에서 경유지 , 목적지 구하기
	public Map<String, Integer> getAddressduration(Map<String, Map<String, String>> map) throws CustomException {
		
//		경유지,목적지 도착까지 소요시간 저장
		 Map<String, Integer> durationMap = new HashMap<>();
		 
		 
		 String apiUrl = "https://apis-navi.kakaomobility.com/v1/directions";
		
		try {
			
//		 출발지 x축 y축
			Map<String, String> origins = map.get("origins");
			StringBuilder originsXandY = new StringBuilder();
			originsXandY.append(origins.get("x")+","+origins.get("y"));
			
			
//		 경유지 x축 y축
			Map<String, String> waypoints = map.get("waypoints");
			StringBuilder waypointsXandY = new StringBuilder();
			waypointsXandY.append(waypoints.get("x")+","+waypoints.get("y"));
			
//		 목적지 x축 y축
			Map<String, String> destination = map.get("destination");
			StringBuilder destinationXandY = new StringBuilder();
			destinationXandY.append(destination.get("x")+","+destination.get("y"));
			
			UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
		            .queryParam("origin", originsXandY.toString())
		            .queryParam("destination", destinationXandY.toString())
		            .queryParam("waypoints", waypointsXandY.toString())
		            .queryParam("priority", "RECOMMEND")
		            .queryParam("car_fuel", "GASOLINE")
		            .queryParam("car_hipass", "false")
		            .queryParam("alternatives", "false")
		            .queryParam("road_details", "false");
			
            headers.add("Authorization", String.format("KakaoAK %s",apiKey));
            HttpEntity<String> requestMessage = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    uri.toUriString(),
                    HttpMethod.GET,
                    requestMessage,
                    String.class);
            
            JSONObject datas = new JSONObject(response.getBody().toString());
            JSONObject routes = datas.getJSONArray("routes").getJSONObject(0);
            JSONObject summary = routes.getJSONObject("summary");
            JSONObject sections = routes.getJSONArray("sections").getJSONObject(0);
            
            int durationTotal = summary.getInt("duration") ;
			int durationWaypoint = sections.getInt("duration") ;
			int durationDestination = durationTotal-durationWaypoint;
			
			durationMap.put("waypoint", durationWaypoint);
			durationMap.put("destination", durationDestination);
            
		} 
		
		catch (Exception e) {
			 throw new CustomException("getAddressduration error");
		}  
		
		finally {
			headerClear();
		}
		
		return durationMap;
	}
	
	
	private void headerClear() {
		headers.clear();
	}
}
