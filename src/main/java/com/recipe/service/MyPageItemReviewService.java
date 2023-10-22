package com.recipe.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.constant.ImgMainOk;
import com.recipe.constant.ItemReviewStatus;
import com.recipe.constant.PointEnum;
import com.recipe.entity.Item;
import com.recipe.entity.ItemImg;
import com.recipe.entity.ItemReview;
import com.recipe.entity.ItemReviewImg;
import com.recipe.entity.Member;
import com.recipe.entity.Order;
import com.recipe.entity.OrderItem;
import com.recipe.entity.Point;
import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.ItemReviewHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.repository.ItemImgRepository;
import com.recipe.repository.ItemReviewImgRepository;
import com.recipe.repository.ItemReviewRepository;
import com.recipe.repository.MemberRepository;
import com.recipe.repository.PointRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageItemReviewService {
	
	private final ItemImgRepository itemImgRepository;
	
	private final ItemReviewRepository itemReviewRepository;
	
	private final MemberRepository memberRepository;
	
	private final ItemReviewImgRepository itemReviewImgRepository;
	
	private final PointRepository pointRepository;
	
	private final FileService fileService;
	
//	나의 상품후기 목록 가져오기(myPage/item_review)
	@Transactional(readOnly = true)
	public Page<ItemReviewHistoryDto> findByMyItemReviewList(Long memberId, Pageable pageable,
			MyPageSerchDto myPageSerchDto) {

		Page<ItemReviewHistoryDto> findByMyReviewList = itemReviewRepository.findByMyReviewList(memberId, pageable,
				myPageSerchDto);

		return findByMyReviewList;
	}
	
//	나의상품리뷰 리뷰 삭제(myPage/item_review)
	@Transactional
	public void itemReviewDelete(Map<String, Object> requestBody , HttpSession session) throws CustomException, IOException {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Long itemReviewId = Long.parseLong(requestBody.get("itemReviewId").toString());
		
		try {
			
			Member member = memberRepository.findById(memberId)
					.orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
			
			ItemReview itemReview = itemReviewRepository.findById(itemReviewId)
										.orElseThrow(() -> new CustomException("사유: 등록된 후기 조회 실패"));
			
			
			String orderNumber = itemReview.getOrderNumber();

			String itemNm = itemReview.getItem().getItemNm();

			String email = member.getEmail();
			
			if(ItemReviewStatus.포토.equals(itemReview.getItemReviewStatus())) {
				
				List<ItemReviewImg> itemReviewImgList = itemReviewImgRepository.findByItemReviewId(itemReviewId);
				
				if(itemReviewImgList == null) {
					new CustomException("사유: 등록된 이미지 조회 실패");
					
				} else {
					for(ItemReviewImg itemReviewImg : itemReviewImgList) {
						itemReviewImgRepository.delete(itemReviewImg);
					}
				}
				
				String highPath = "C:/yummy/member/" + email + "/review/order/" + orderNumber;
				
				String path = "C:/yummy/member/" + email + "/review/order/" + orderNumber + "/" + itemNm;
				
				fileService.deleteFolder(path,highPath);
			}
			
			int p = ItemReviewStatus.일반.equals(itemReview.getItemReviewStatus()) ? 500 : 1000;
			String info = "후기 삭제";
			PointEnum pointEnum = PointEnum.EXPIRE;
			
			StringBuilder sb = new StringBuilder();
			sb.append("주문번호: ");
			sb.append(orderNumber);
			sb.append(",");
			sb.append("상품명: ");
			sb.append(itemNm);
			
			Point point = Point.createPoint(member, p, pointEnum , info, sb.toString());
			pointRepository.save(point);
			
			member.minusPoint(p);
			session.setAttribute("point", member.getPoint());
		
			itemReviewRepository.delete(itemReview);
			
		} catch (IOException e) {
			log.error("orderItemReviewModi -> deleteFolder-error", e);
			throw new IOException("사유: 이미지 삭제 오류");

		} catch (Exception e) {
			log.error("itemReviewDelete-error", e);
			throw e;
		}
		
	}
	
	
//	나의상품후기 리뷰수정 팝업창 주문상품정보 가져오기(myPage/item_review/itemReview_popup_modi)
	public Map<String, Object> findByMyItemReview(Long itemReviewId, Long memberId) throws CustomException {

		Map<String, Object> itemReviewMap = new HashMap<>();

		try {

			ItemReview itemReview = itemReviewRepository.findById(itemReviewId)
					.orElseThrow(() -> new CustomException("사유: 후기 조회 실패"));

			Item item = itemReview.getItem();

			if (item == null) {
				throw new CustomException("사유: 상품 조회 실패");
			}

			ItemImg itemImg = itemImgRepository.findByItemIdAndImgMainOk(item.getId(), ImgMainOk.Y);

			if (itemImg == null) {
				throw new CustomException("사유: 상품 이미지 조회 실패");
			}


			itemReviewMap.put("itemReview", itemReview);
			itemReviewMap.put("item", item);
			itemReviewMap.put("itemImg", itemImg);

			if (ItemReviewStatus.포토.equals(itemReview.getItemReviewStatus())) {
				List<ItemReviewImg> itemReviewImgList = itemReviewImgRepository.findByItemReviewId(itemReviewId);
				itemReviewMap.put("itemReviewImgList", itemReviewImgList);
			}

		} catch (Exception e) {
			log.error("findByMyItemReview-error", e);
			throw e;

		}

		return itemReviewMap;
	}
	
//	주문상품 리뷰작성 팝업창 리뷰 수정(myPage/itemReview_popup_modi)
	@Transactional
	public void orderItemReviewModi(MultipartFile[] files, String[] oriImgDeleteNames, String[] oriImgNames,
			double star, String content, Long itemReviewId, HttpSession session) throws IOException, CustomException {

		Long memberId = (Long) session.getAttribute("memberId");

		try {
			ItemReview itemReview = itemReviewRepository.findById(itemReviewId)
					.orElseThrow(() -> new CustomException("사유: 등록된 후기 조회 실패"));

			Member member = memberRepository.findById(memberId)
					.orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));

			if (star == 0 || star > 5.0) {
				throw new CustomException("사유: 별점 오류");
			}

			String contentCheck = content.replaceAll("\\s", "");
			if (content == null || contentCheck.length() > 350) {
				throw new CustomException("사유: 리뷰내용 오류");
			}
			
			

			itemReview.setRating(star);
			itemReview.setContent(content);

			String orderNumber = itemReview.getOrderNumber();

			String itemNm = itemReview.getItem().getItemNm();
			
			String email = member.getEmail();
			
			String result = "C:/yummy/member/" + email + "/review/order/" + orderNumber + "/" + itemNm;

			if (oriImgDeleteNames != null) {

				for (String imgOriName : oriImgDeleteNames) {
					ItemReviewImg itemReviewImg = itemReviewImgRepository.findByImgOriNameAndItemReviewId(imgOriName,itemReview.getId());
					itemReviewImgRepository.delete(itemReviewImg);
					
					fileService.deleteFileImg(result, itemReviewImg.getImgName());
				}

			}

//이미지 파일이 있다면 이미지파일 itemReivewImg 생성
			if (files != null) {

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
			
			int p = 500;
			String info;
			
			int totalImageCount = (files != null ? files.length : 0) + (oriImgNames != null ? oriImgNames.length : 0);
			
			if (ItemReviewStatus.일반.equals(itemReview.getItemReviewStatus()) && totalImageCount > 0) {
					
					member.plusPoint(p);
					
					itemReview.setItemReviewStatus(ItemReviewStatus.포토);
					
					info = "일반후기 -> 포토후기 ";
					Point point = Point.createPoint(member, p, PointEnum.PLUS , info, sb.toString());
					pointRepository.save(point);
			}
			
			else if(ItemReviewStatus.포토.equals(itemReview.getItemReviewStatus())) {
				
				 if (totalImageCount > 8) {
				        throw new CustomException("사유: 이미지 등록 갯수 제한");
				    }
				
				if(totalImageCount == 0) {
					
					member.minusPoint(p);
					itemReview.setItemReviewStatus(ItemReviewStatus.일반);
					
					info = "포토후기 -> 일반후기";
					Point point = Point.createPoint(member, p, PointEnum.EXPIRE , info, sb.toString());
					pointRepository.save(point);
					
				}
				
			}
			
			session.setAttribute("point", member.getPoint());

		}  catch (CustomException e) {
			log.error("orderItemReviewModi-error", e);
			throw e;

		}

	}
	

	

}
