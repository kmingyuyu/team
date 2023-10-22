package com.recipe.myPage.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageMemberModiDto {
	
	@Size(min = 2, max = 10, message = "닉네임은 2자 ~ 10자 사이로 입력 해주세요.")
	@Pattern(regexp = "^[^&<>();'\"/]+$", message = "닉네임은 일부 특수문자 사용 불가능합니다.")
	String nicknameNew;
	
	@Size(min = 9, max = 15, message = "기존 비밀번호를 9자 ~ 15자 사이로 입력 해주세요.")
	String passwordOri;
	
	@Size(min = 9, max = 15, message = "신규 비밀번호를 9자 ~ 15자 사이로 입력 해주세요.")
	String passwordNew;
	
	@Size(min = 9, max = 15, message = "신규 비밀번호 재입력을 9자 ~ 15자 사이로 입력 해주세요.")
	String passwordRecom;
	
	@Size(min = 2, max = 20, message = "자기소개는 2자 ~ 20자 사이로 입력 해주세요.")
	@Pattern(regexp = "^[^&<>();'\"/]+$", message = "자기소개는 일부 특수문자 사용 불가능합니다.")
	String introduceNew;
	
	@Size(min = 11, max = 11, message = "휴대전화 번호는 11자로 입력 해주세요.")
	@Pattern(regexp = "^[0-9]+$", message = "휴대전화 번호는 숫자만 입력 가능합니다.")
	String phoneNumberNew;
	
	@Size(min = 2, max = 15, message = "성함은 2자 이상 입력 해주세요.")
	@Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣]+$", message = "성함은 한글만 입력 가능합니다.")
	String nameNew;
	
	String postCodeNew;
	
	String addressNew;
	
	String detailAddressNew;
	
	
}
