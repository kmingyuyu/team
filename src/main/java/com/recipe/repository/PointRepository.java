package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
	
	List<Point> findByMemberId(Long id); 
	
}
