package com.recipe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.constant.PointEnum;
import com.recipe.entity.Point;
import com.recipe.myPage.dto.PointHistoryDto;

public interface PointRepository extends JpaRepository<Point, Long> {
	
	List<Point> findByMemberId(Long id);
	
	Long countByMemberId(Long memberId);
	
	Long countByPointEnumAndMemberId(PointEnum pointEnum , Long memberId);
	
	@Query("SELECT COALESCE(SUM(p.point), 0) FROM Point p WHERE p.pointEnum = :pointEnum AND p.member.id = :memberId")
	Long sumByPointEnumAndMemberId(@Param("pointEnum") PointEnum pointEnum, @Param("memberId") Long memberId);
	
	@Query(value= "SELECT"
			+ " p.point_id pointId, "
			+ " p.point point , "
			+ " p.point_enum pointEnum, "
			+ " p.point_info pointInfo, "
			+ " p.point_detail_info pointDetailInfo, "
			+ " p.reg_time regTime "
			+ " FROM point p "
			+ " where p.member_id = :memberId \r\n"
			+ " AND p.point_enum like CONCAT('%', :pointEnum, '%') "
			+ " ORDER BY p.reg_time DESC" , nativeQuery = true)
	Page<PointHistoryDto> findByMyPointList(
			@Param("memberId") Long memberId, 
			@Param("pointEnum") String pointEnum,
	        Pageable pageable);
	
	
	
}
