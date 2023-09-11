package com.recipe.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialMemberDto {
	
	@NotEmpty(message = "이메일은 필수 입력 값입니다")
	@Email(message = "이메일 형식으로 입력해주세요")
	private String email;
	
	@Length(min = 2, max = 8, message = "닉네임은 2~8자 사이로 입력해주세요")
	@NotNull
	private String nickname;
	
	@NotEmpty
	@Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 휴대폰 번호를 입력해주세요. 양식은 010-xxxx-xxxx 입니다.")
	private String phoneNumber;
	
	private String name; //구글,카카오에서 주는 구글닉네임 
	
	private String password;
	
	private String provider; //구글,카카오 분류
 
	private String providerId; //구글 카카오 기본키 id값 
	
	private String postCode; //우편번호
	
	private String address; //주소
	
	private String detailAddress; //상세주소
	
	private String introduce; //자기소개 ,우선 기본값만 세팅 수정해서 쓰게끔
	
	private String imgUrl; //프로필이미지
	
	private String oriImgName; //프로필이지미 원본이름
	
	private String imgName; //바뀐 이미지 이름
}