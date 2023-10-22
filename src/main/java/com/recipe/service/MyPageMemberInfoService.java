package com.recipe.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.entity.Member;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.myPage.dto.MyPageMemberModiDto;
import com.recipe.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class MyPageMemberInfoService {
	
	private final MemberRepository memberRepository;
	
	private final FileService fileService;
	
	private final PasswordEncoder passwordEncoder;
	
	private String profileImgLocation = "C:/yummy/member";
	
//	회원 정보가져오기(/myPage/memberModi)
	@Transactional(readOnly = true)
	public Member findByMember(Long memberId) throws CustomException {
		
		Member	member = memberRepository.findById(memberId)
					.orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		return member;
	}

//	회원 정보 변경=닉네임(/myPage/memberModi)
	@Transactional
	public void nicknameModi(HttpSession session, MyPageMemberModiDto myPageMemberModiDto) throws CustomException , FindNotException {
		
		String nicknameOri = (String) session.getAttribute("nickname");
		
		String nicknameNew = myPageMemberModiDto.getNicknameNew();
		
		if(nicknameNew == null) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		if(nicknameNew.equals(nicknameOri)) {
			throw new FindNotException("기존 닉네임과 동일 합니다.");
		}
		
		Member	memberOri = memberRepository.findByNickname(nicknameOri);
		
		Member memberNew = memberRepository.findByNickname(nicknameNew);
		
		if(memberOri == null) {
			throw new CustomException("사유: 회원 조회 실패");
		}
		
		if(memberNew != null) {
			throw new FindNotException("이미 등록된 닉네임 입니다.");
		}
		
		memberOri.setNickname(nicknameNew);
		
		session.setAttribute("nickname", nicknameNew);
		
	}
	
//	회원 정보 변경=비밀번호(/myPage/memberModi)
	@Transactional
	public void passwordModi(HttpSession session, MyPageMemberModiDto myPageMemberModiDto) throws CustomException , FindNotException {
	
		String passwordOri = myPageMemberModiDto.getPasswordOri();
		String passwordNew = myPageMemberModiDto.getPasswordNew();
		String passwordRecom = myPageMemberModiDto.getPasswordRecom();
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		if(passwordOri == null || passwordNew == null || passwordRecom == null) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		
		if(!passwordNew.equals(passwordRecom)) {
			throw new FindNotException("신규 비밀번호와 신규 재입력 비밀번호가 불일치 합니다.");
		}
		
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		boolean passCheck = passwordEncoder.matches(passwordOri, member.getPassword());
		
		if(!passCheck) {
			throw new FindNotException("입력하신 기존 비밀번호가 불일치 합니다.");
		}
		
		String password = passwordEncoder.encode(passwordNew);
		
		member.setPassword(password);
		
		
	}
	
//	회원 정보 변경=휴대전화(/myPage/memberModi)
	@Transactional
	public void phoneNumberModi(HttpSession session, MyPageMemberModiDto myPageMemberModiDto) throws CustomException , FindNotException {
		
		
		String phoneNumberNew = myPageMemberModiDto.getPhoneNumberNew();
		
		if(phoneNumberNew == null) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		
		if (!phoneNumberNew.startsWith("010")) {
			throw new FindNotException("휴대전화(010) 정보만 입력해주세요.");
		} 
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		String formattedNumber = phoneNumberNew.substring(0, 3) + "-" + phoneNumberNew.substring(3, 7) + "-" + phoneNumberNew.substring(7);
		
		if(member.getPhoneNumber().equals(formattedNumber)) {
			throw new FindNotException("기존 휴대전화 정보와 동일 합니다.");
		}
		
		member.setPhoneNumber(formattedNumber);
		
	}
	
//	회원 정보 변경=자기소개(/myPage/memberModi)
	@Transactional
	public String introduceModi(HttpSession session, MyPageMemberModiDto myPageMemberModiDto) throws CustomException , FindNotException {
		
		
		String introduceNew = myPageMemberModiDto.getIntroduceNew();
		
		if(introduceNew == null) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		if(member.getIntroduce() != null && member.getIntroduce().equals(introduceNew)) {
			throw new FindNotException("기존 자기소개와 동일 합니다.");
		}
		
		String comment = member.getIntroduce() == null ? "등록" : "변경";
		
		member.setIntroduce(introduceNew);
		session.setAttribute("introduce", introduceNew);
		
		return comment;
		
	}
	
//	회원 정보 변경=성함(/myPage/memberModi)
	@Transactional
	public String nameModi(HttpSession session, MyPageMemberModiDto myPageMemberModiDto) throws CustomException , FindNotException {
		
		
		String nameNew = myPageMemberModiDto.getNameNew();
		
		if(nameNew == null) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		if(member.getName() != null && member.getIntroduce().equals(nameNew)) {
			throw new FindNotException("기존 성함과 동일 합니다.");
		}
		
		String comment = member.getName() == null ? "등록" : "변경";
		
		member.setName(nameNew);
		
		return comment;
		
	}
	
//	회원 정보 변경=주소(/myPage/memberModi)
	@Transactional
	public String addressModi(HttpSession session, MyPageMemberModiDto myPageMemberModiDto) throws CustomException , FindNotException {
		
		String postCodeNew = myPageMemberModiDto.getPostCodeNew();
		String addressNew = myPageMemberModiDto.getAddressNew();
		String detailAddressNew = myPageMemberModiDto.getDetailAddressNew();
		
		
		if((postCodeNew == null) || addressNew == null || detailAddressNew== null) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		String postCodeOri = member.getPostCode();  
		String addressOri = member.getAddress();   
		String detailAddressOri = member.getDetailAddress(); 
		
		
		if (postCodeOri != null && addressOri != null && detailAddressOri != null) {
		    
		    if (postCodeOri.equals(postCodeNew) && addressOri.equals(addressNew) && detailAddressOri.equals(detailAddressNew)) {
		        throw new FindNotException("기존 주소와 동일 합니다.");
		    }
		}
		
		String comment = member.getPostCode() == null ? "등록" : "변경";
		
		member.setPostCode(postCodeNew);
		member.setAddress(addressNew);
		member.setDetailAddress(detailAddressNew);
		
		return comment;
		
	}
	
//	회원 정보 변경=이미지(/myPage/memberModi)
	@Transactional
	public void imgModi(MultipartFile imgFile , HttpSession session) throws CustomException , IOException {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		
		if(imgFile == null || imgFile.isEmpty()) {
			throw new CustomException("사유: 요청 이미지 조회 실패");
		}
		
		 if (imgFile != null && !imgFile.isEmpty()) {
		        String originalFilename = imgFile.getOriginalFilename();
		        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

		        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
		        if (!allowedExtensions.contains(fileExtension)) {
		            throw new CustomException("사유: 확장자 오류.");
		        }

		        if (imgFile.getSize() > 2 * 1024 * 1024) {
		            throw new CustomException("사유: 이미지 파일 크기 초과");
		        }
		        
		        String email = member.getEmail();
		        
		        StringBuilder sb = new StringBuilder();
		        sb.append(profileImgLocation + "/" + email + "/profile");
		        String result = sb.toString();
		        
		        if(member.getOriImgName() != null) {
		        	fileService.deleteFileImg(result, member.getImgName());
		        }
		        	
		        String savedImgName = fileService.uploadFileImg(result, originalFilename,
		        			imgFile.getBytes());
		        
		        sb = new StringBuilder();
		        
		        sb.append("/img/member/");
		        sb.append(email);
		        sb.append("/profile/");
		        sb.append(savedImgName);
		        String imgUrl = sb.toString();
		        
		        member.setImgUrl(imgUrl);
		        member.setImgName(savedImgName);
		        member.setOriImgName(originalFilename);
				session.setAttribute("imgUrl", imgUrl);
		    }
		
		
	}	
	
//	회원 정보 삭제=자기소개,성함,주소(/myPage/memberModi)
	@Transactional
	public String memberInfoDelete(@RequestBody Map<String, Object> requestBody , HttpSession session) throws CustomException, IOException {
		
		String comment = null;
		
		String delete = requestBody.get("delete").toString();
		
		if(delete == null || delete.isEmpty()) {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("사유: 접속회원 조회 실패"));
		
		if(delete.equals("introduce")) {
			comment = "자기소개";
			member.setIntroduce(null);
			session.setAttribute("introduce", null);
		}
		
		else if(delete.equals("name")) {
			comment = "성함";
			member. setName(null);
		}
		
		else if(delete.equals("address")) {
			comment = "주소";
			member.setPostCode(null);
			member.setAddress(null);
			member.setDetailAddress(null);
		}
		
		else if(delete.equals("img")) {
			
		    if(member.getOriImgName() != null) {
		    	
		    	StringBuilder sb = new StringBuilder();
				String email = member.getEmail();
				sb.append(profileImgLocation + "/" + email + "/profile");
			    String result = sb.toString();
		    	
	        	fileService.deleteFileImg(result, member.getImgName());
	        }
			
			comment = "이미지";
			member.setOriImgName(null);
			member.setImgName("비회원이미지.jpg");
			member.setImgUrl("/img/비회원이미지.jpg");
			session.setAttribute("imgUrl", member.getImgUrl());
		}
		
		else {
			throw new CustomException("사유: 요청정보 조회 실패");
		}
		
		
		return comment;
	}
	
	
}