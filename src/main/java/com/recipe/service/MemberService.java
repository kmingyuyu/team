package com.recipe.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.dto.MemberBestDto;
import com.recipe.dto.MemberMainDto;
import com.recipe.entity.Follow;
import com.recipe.entity.Member;
import com.recipe.repository.FollowRepository;
import com.recipe.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	
	private final FollowRepository followRepository;
	
	
	//회원가입 데이터를 DB에 저장한다
		public Member saveMember(Member member) {
			
			validateDuplicateMember(member); //중복체크
			
			
			Member savedMember = memberRepository.save(member);
			return savedMember;
		}
		
		//이메일 중복체크
		private void validateDuplicateMember(Member member) {
			Member findMember = memberRepository.findByEmail(member.getEmail());
			
			if(findMember != null) {
				throw new IllegalStateException("이미 가입된 회원입니다");
			}
		}

		@Override //시큐리티 ,DB에서 사용자의 정보를 확인해서 로그인
		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			Member member = memberRepository.findByEmail(email);
			
			if(member == null) { //DB에 사용자가 없으면
				throw new UsernameNotFoundException(email);
			}
			
			return User.builder()
						.username(member.getEmail())
						.password(member.getPassword())
						.roles(member.getRole().toString())
						.build();
		}
		
		//휴대폰 번호로 가입 이메일 찾기
		public String findEmail(String phoneNumber) {
			
			String memberEmail = memberRepository.findEmailByPhoneNumber(phoneNumber);
			
			return memberEmail;
		}
	
	
	
	
	
	
	@Transactional(readOnly = true)
	public List<MemberMainDto> getMemberBestList() {
		List<MemberMainDto> getMemberBestList = memberRepository.getMemberBestList();
		return getMemberBestList;
		
	}
	
	@Transactional(readOnly = true)
	public List<MemberBestDto> getRankMemberList() {
		List<MemberBestDto> getRankMemberList = memberRepository.getRankMemberList();
		return getRankMemberList;
	}
	
	
	public void followReg(Long id) {
		
		/*
		 * String id5 = "5" ;
		 * 
		 * Long memberId = Long.parseLong(id5);
		 * 
		 * Member member = memberRepository.findById(memberId).orElseThrow();
		 * 
		 * Follow followTest =
		 * followRepository.findByMemberIdAndToMember(member.getId(),memberId);
		 * 
		 * if(followTest == null) { Follow follow = new Follow();
		 * follow.setMember(member); follow.setToMember(id);
		 * followRepository.save(follow); System.out.println("저장되었습니다"); }
		 * 
		 * System.out.println("이미 팔로우 되있습니다");
		 */
		
	}
	
	
	
}
