package com.recipe.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.constant.AnswerOk;
import com.recipe.constant.ImgMainOk;
import com.recipe.entity.ItemInqAnwser;
import com.recipe.entity.QItem;
import com.recipe.entity.QItemImg;
import com.recipe.entity.QItemInq;
import com.recipe.entity.QItemInqAnwser;
import com.recipe.myPage.dto.ItemInqHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;

import jakarta.persistence.EntityManager;

public class ItemInqRepositoryImpl implements ItemInqRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public ItemInqRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression whereClause(Long memberId, MyPageSerchDto myPageSerchDto) {
		
	    QItem i = QItem.item;
	    QItemInq ii = QItemInq.itemInq;
	    
		LocalDate startDateFromClient = myPageSerchDto.getStartTime(); 
		LocalDate endDateFromClient = myPageSerchDto.getEndTime();   
		
		LocalDateTime startDateTime = startDateFromClient.atStartOfDay();
		LocalDateTime endDateTime = endDateFromClient.atTime(23, 59, 59, 999999);
	    
	    BooleanExpression conditions = ii.member.id.eq(memberId)
	        .and(ii.regTime.between(startDateTime, endDateTime))
	        .and(ii.answerOk.stringValue().toLowerCase().like("%" + myPageSerchDto.getData().toLowerCase() + "%"))
	        .and(i.itemNm.like("%" + myPageSerchDto.getSearchQuery() + "%"));

	    return conditions; // 조건 반환
	}

	@Override
	public Page<ItemInqHistoryDto> findByMyItemInqList(Pageable pageable,Long memberId, MyPageSerchDto myPageSerchDto) {
		
		QItemInq ii = QItemInq.itemInq;
		QItemInqAnwser iia = QItemInqAnwser.itemInqAnwser;
		QItem i = QItem.item;
		QItemImg im = QItemImg.itemImg;
		
		List<ItemInqHistoryDto> content = queryFactory
								.select(
										Projections.constructor(
												ItemInqHistoryDto.class,
											ii.id.as("itemInqId"),
											i.id.as("itemId"),
											ii.title,
											ii.content,
											ii.itemInqBoardEnum,
											ii.itemInqEnum,
											ii.answerOk,
											ii.regTime,
											i.itemNm,
											im.imgUrl
												))	
								.from(ii)
								.join(i).on(ii.item.id.eq(i.id))
								.join(im).on(i.id.eq(im.item.id).and(im.imgMainOk.eq(ImgMainOk.Y)))
								.where(whereClause(memberId,myPageSerchDto))
								.orderBy(ii.regTime.desc())
								.offset(pageable.getOffset())
							    .limit(pageable.getPageSize())
								.fetch();
		
			for(ItemInqHistoryDto dto: content) {
				if(AnswerOk.답변완료.equals(dto.getAnswerOk())) {
					ItemInqAnwser itemInqAnwser = queryFactory
						.select(iia)
						.from(iia)
						.where(iia.itemInq.id.eq(dto.getItemInqId()))
						.fetchOne();
				
					dto.setItemInqAnwser(itemInqAnwser);
				
				}
			}
			
			long total = queryFactory
					.select(Wildcard.count)
					.from(ii)
					.join(i).on(ii.item.id.eq(i.id))
					.join(im).on(i.id.eq(im.item.id).and(im.imgMainOk.eq(ImgMainOk.Y)))
					.where(whereClause(memberId,myPageSerchDto))
					.fetchOne();
		
			return new PageImpl<>(content, pageable, total);
	}

}
