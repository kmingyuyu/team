package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	Follow findByMemberIdAndToMember(Long memberId , Long id);
	
}
