package com.recipe.entity;

import com.recipe.constant.PointEnum;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Point {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="point_id")
	private Long id;
	
	private int point;
	
	private PointEnum pointEnum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	
}
