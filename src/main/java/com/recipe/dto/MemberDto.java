package com.recipe.dto;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import com.recipe.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

	@NotEmpty(message = "* 이메일을 입력 해주세요 *")
	@Email(message = "* 이메일 형식으로 입력해주세요 *")
	private String email;
	
	@NotEmpty(message = "* 비밀번호를 입력 해주세요 *")
	@Length(min = 9, max = 15, message = "* 비밀번호는 9 ~ 15자 사이로 입력해주세요 *")
	private String password;
	
	private String passwordConfirm;
	
	@Length(min = 2, max = 8, message = "* 닉네임은 2~8자 사이로 입력해주세요 *")
	@NotNull
	private String nickname; //사이트용 닉네임
	
	@NotEmpty(message = "* 휴대폰번호를 입력 해주세요 *")
	@Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "* 올바른 휴대폰 번호를 입력해주세요. 양식은 010-xxxx-xxxx 입니다 *")
	private String phoneNumber;
	
	private Long id;
	
	private String emailConfirm; //이메일로 받은 인증번호
	
	private String emailConfirm2; ////이메일로 받은 인증번호 확인용
	
	private String postCode; //우편번호
	
	private String address; //주소
	
	private String detailAddress; //상세주소
	
	private String introduce; //자기소개 ,우선 기본값만 세팅 수정해서 쓰게끔
	
	private String imgUrl; //업로드한 프로필 이미지 url
	
	private String oriImgName; //원본이미지 이름
	
	private String imgName; //바뀐 사진이름(보안을위해)
	
	private String name;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static MemberDto of(Member member) {
		return modelMapper.map(member, MemberDto.class);
	}
}