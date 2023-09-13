package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.constant.ImgMainOk;
import com.recipe.entity.MemberImg;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long>{
	
	MemberImg findByMemberIdAndImgMainOk(Long memberId, ImgMainOk imgMainOk);
	
}
