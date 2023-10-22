package com.recipe.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.recipe.entity.Follow;
import com.recipe.entity.Member;
import com.recipe.exception.CustomException;
import com.recipe.myPage.dto.FollowHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;
import com.recipe.repository.FollowRepository;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageFollowService {
	
	private final MemberRepository memberRepository;
	
	private final FollowRepository followRepository;
	
//	나의 팔로우 가져오기
	public Page<FollowHistoryDto> findByMyFollowList(Long memberId, Pageable pageable,MyPageSerchDto myPageSerchDto) {
		
		Page<FollowHistoryDto> findByMyFollowList = null;
		
		if("following".equals(myPageSerchDto.getData())) {
			findByMyFollowList = memberRepository.findByMyFollowingList(memberId, myPageSerchDto.getSearchQuery(), pageable);
		}
		
		else if("follower".equals(myPageSerchDto.getData())) {
			findByMyFollowList = memberRepository.findByMyFollowerList(memberId, myPageSerchDto.getSearchQuery(), pageable);
		}
		
		else {
			findByMyFollowList = memberRepository.findByMyFollowerList(memberId, myPageSerchDto.getSearchQuery(), pageable);
		}
		
		return findByMyFollowList;
	}
	
	@Transactional
	public void followReg(Map<String, Object> requestBody,HttpSession session) throws CustomException{
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		String nicknameToMember = requestBody.get("nickname").toString();
		
		Member toMember = memberRepository.findByNickname(nicknameToMember);
		
		if(toMember == null) {
			throw new CustomException("사유: 팔로우 요청회원 조회 실패");
		}
		
		Follow followCheck = followRepository.findByMemberIdAndToMember(memberId, toMember.getId());
		
		if(followCheck != null) {
			throw new CustomException("사유: 이미 팔로우 중인 회원");
		}
		
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		Follow follow = Follow.createFollow(member, toMember.getId());
		followRepository.save(follow);
		
		
	}
	
	@Transactional
	public void followDelete(@RequestBody Map<String, Object> requestBody,HttpSession session) throws CustomException{
		
		
		Long followId = Long.parseLong(requestBody.get("followId").toString());
		
		Follow follow = followRepository.findById(followId)
				.orElseThrow(() -> new CustomException("사유: 팔로우취소 정보 조회 실패"));
		
		followRepository.delete(follow);
		
		
	}
	
	
}
