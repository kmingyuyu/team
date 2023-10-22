package com.recipe.entity;


import com.recipe.constant.PointEnum;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="point")
public class Point extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="point_id")
	private Long id;
	
	private int point; // 포인트
	
	@Enumerated(EnumType.STRING)
	private PointEnum pointEnum; // 포인트종류
	
	private String pointInfo; // 포인트 내용
	
	private String pointDetailInfo; // 포인트 상세내용

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	
	public static Point createPoint (Member member , int point, PointEnum pointEnum , String pointInfo , String pointDetailInfo) {
		
		Point pointEntity = new Point() ;
		
		pointEntity.setMember(member);
		pointEntity.setPoint(point);
		pointEntity.setPointEnum(pointEnum);
		pointEntity.setPointInfo(pointInfo);
		pointEntity.setPointDetailInfo(pointDetailInfo);
		
		return pointEntity;
	}
	
	
}
