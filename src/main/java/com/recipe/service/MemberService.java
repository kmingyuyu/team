package com.recipe.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.dto.MemberDto;
import com.recipe.dto.MemberMainDto;
import com.recipe.dto.MngMemberDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.entity.Follow;
import com.recipe.entity.Member;
import com.recipe.entity.Point;
import com.recipe.repository.CartRepository;
import com.recipe.repository.FollowRepository;
import com.recipe.repository.MemberRepository;
import com.recipe.repository.PointRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository ;
	
	private final CartRepository cartRepository ;
	
	private final FollowRepository followRepository ;
	
	private final PointRepository pointRepository  ;
	
	
	public Member findMember(Long id) {
		
		Member member = memberRepository.findById(id).orElseThrow();
		
		if (member == null) {
		    return null;
		}
		 
		 return member;
	}
	
	
	public int findPoint(Long id) {
		int point = 0;
		
		List<Point> pointList = pointRepository.findByMemberId(id);
		
		if(pointList != null) {
			for(Point p : pointList) {
				point += p.getPoint();
			}
		}
		return point;
		
	}

	
	public Long cartCount(Long id) {
		
		Long cartCount = cartRepository.countByMemberId(id);
		
		return cartCount;
	}
	
	public boolean findMember(String email) {
		
		boolean ck = true;
		
		Member member = memberRepository.findByEmail(email);
		
		if(member != null) {
			ck = false;
		}
		
		return ck;
	}
	
	public String findEmailSearch(Map<String, Object> psdata) {
		
		String phone = psdata.get("phoneNumber").toString();
		String ck = null;
		
		Member member = memberRepository.findByPhoneNumber(phone);
		
		if (member == null) {
		    return ck;
		  }
		
		
		ck = member.getEmail();
		return ck;
	}
	
	
	public boolean findSns(String email) {
		
		boolean ck = true;
		
		Member member = memberRepository.findByEmail(email);
		
		if(member != null) {
			ck = false;
		}
		
		if(!"default".equals(member.getProviderId())) {
			ck = false;
		}
		
		
		return ck;
	}
	
	
	public Long findId(String email) {
		Member member = memberRepository.findByEmail(email);
		
		 if (member == null) {
		        return null;
		    }
		
		return member.getId();
	}
	
	
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
	
	
	public String followReg(Map<String, Object> requestBody , String email) {
		  Long id = Long.parseLong(requestBody.get("id").toString());
		
		  Member member = memberRepository.findByEmail(email);
		  
		  Follow followOk =
		  followRepository.findByMemberIdAndToMember(member.getId(),id);
		  
		  
		  if(member.getId().equals(id)) {
			  return "본인은 팔로우 할수 없습니다";
		  }
		  else if(followOk == null) {
			  Follow follow = new Follow();
			  follow.setMember(member);
			  follow.setToMember(id);
			  followRepository.save(follow);
			  return "팔로우 완료 되었습니다";
		  }
		 else {
			 return "이미 팔로우 되어있는 회원 입니다";
		 }
		  
		
	}
	
	@Transactional(readOnly = true)
	public Page<MngMemberDto> getAdminMemberPage(MngRecipeSearchDto MngrecipeSearchDto, Pageable pageable) {
		Page<MngMemberDto> memberPage = memberRepository.getAdminMemberPage(MngrecipeSearchDto, pageable);
		return memberPage;

	}

	// 회원 삭제
	public void deleteMember(Long memberId) {

		// ★delete하기 전에 select를 한번 해준다
		// ->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

		// delete
		memberRepository.delete(member);
	}
	
	
}
