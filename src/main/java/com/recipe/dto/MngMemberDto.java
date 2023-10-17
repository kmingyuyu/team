package com.recipe.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.querydsl.core.annotations.QueryProjection;
import com.recipe.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MngMemberDto {

	private Long id;

	private String nickname;

	private String email;

	private String phoneNumber;
	
	private String postCode;
	
	private String address;
	
	private String detailAddress;
	
	private String provider;

	private LocalDateTime regTime; // 등록날짜
	
	private String formattedRegTime; // 보기 좋은 형식으로 변환된 등록날짜

	@QueryProjection
	public MngMemberDto(Long id, String nickname, String email,  String phoneNumber , String postCode
			,String address , String detailAddress , String provider ,  LocalDateTime regTime) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.postCode = postCode;
		this.address = address;
		this.detailAddress = detailAddress;
		this.provider = provider;

		// 등록날짜를 보기 좋은 형식으로 변환하여 formattedRegTime에 저장
		if (regTime != null) {
			this.regTime = regTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			this.formattedRegTime = regTime.format(formatter);
		}

	}

	// 상품 이미지 정보를 저장
	private List<MngMemberDto> recipeDtoList = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	// dto -> entity로 바꿈
	public Member createMember() {
		return modelMapper.map(this, Member.class);
	}

	// entity -> dto로 바꿈
	public static MemberDto of(Member member) {

		return modelMapper.map(member, MemberDto.class);
	}

}
