package com.recipe.myPage.point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.myPage.dto.PointHistoryDto;
import com.recipe.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPagePointService {
	
	private final PointRepository pointRepository;
	
//	마이페이지 나의 포인트정보 가져오기(/myPage/point)
	@Transactional(readOnly = true)
	public Page<PointHistoryDto> findByMyPointList(Long memberId, Pageable pageable, MyPageSerchDto myPageSerchDto){
		
		Page<PointHistoryDto> pointList = pointRepository.findByMyPointList(memberId, myPageSerchDto.getData(), pageable);
		
		return pointList;
	}
	


}
