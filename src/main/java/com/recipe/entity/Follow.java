package com.recipe.entity;

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
import lombok.ToString;

@Entity
@Table(name="Follow")
@Getter
@Setter
@ToString
public class Follow extends BaseTimeEntity {
	
	@Id
	@Column(name="follow_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	private Long toMember;
	
	public static Follow createFollow(Member member,Long toMember) {
		
		Follow follow = new Follow();
		
		follow.setMember(member);
		follow.setToMember(toMember);
		
		return follow;
		
		
	}
	
}
