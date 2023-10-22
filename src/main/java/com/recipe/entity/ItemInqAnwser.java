package com.recipe.entity;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="item_inq_answer")
public class ItemInqAnwser extends BaseEntity {
	@Id
	@Column(name="item_inq_answer_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Lob
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "itemInqAnwser")
	private ItemInq itemInq;
	
	
	public static ItemInqAnwser createItemInqAnwser(Member member,ItemInq itemInq , String content) {
		
		ItemInqAnwser itemInqAnwser = new ItemInqAnwser();
		itemInqAnwser.setMember(member);
		itemInqAnwser.setItemInq(itemInq);
		itemInqAnwser.setContent(content);
		
		return itemInqAnwser;
	}
}
