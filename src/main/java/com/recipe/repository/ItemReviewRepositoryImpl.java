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
import com.recipe.constant.ImgMainOk;
import com.recipe.entity.ItemReviewAnswer;
import com.recipe.entity.ItemReviewImg;
import com.recipe.entity.QItem;
import com.recipe.entity.QItemImg;
import com.recipe.entity.QItemReview;
import com.recipe.entity.QItemReviewAnswer;
import com.recipe.entity.QItemReviewImg;
import com.recipe.myPage.dto.ItemReviewHistoryDto;
import com.recipe.myPage.dto.MyPageSerchDto;

import jakarta.persistence.EntityManager;

public class ItemReviewRepositoryImpl implements ItemReviewRepositoryCustom {
	

	private JPAQueryFactory queryFactory;

	public ItemReviewRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression whereClause(Long memberId, MyPageSerchDto myPageSerchDto) {
	    QItemReview ir = QItemReview.itemReview;
	    QItem i = QItem.item;
	    
		LocalDate startDateFromClient = myPageSerchDto.getStartTime(); 
		LocalDate endDateFromClient = myPageSerchDto.getEndTime();   
		
		LocalDateTime startDateTime = startDateFromClient.atStartOfDay();
		LocalDateTime endDateTime = endDateFromClient.atTime(23, 59, 59, 999999);
	    
	    BooleanExpression conditions = ir.member.id.eq(memberId)
	        .and(ir.regTime.between(startDateTime, endDateTime))
	        .and(ir.itemReviewStatus.stringValue().toLowerCase().like("%" + myPageSerchDto.getData().toLowerCase() + "%"))
	        .and(i.itemNm.like("%" + myPageSerchDto.getSearchQuery() + "%"));

	    return conditions; // 조건 반환
	}
	
	@Override
	public Page<ItemReviewHistoryDto> findByMyReviewList(Long memberId, Pageable pageable, MyPageSerchDto myPageSerchDto) {
		
		QItemReview ir = QItemReview.itemReview;
		QItem i = QItem.item;
		QItemImg im = QItemImg.itemImg;
		QItemReviewImg iri = QItemReviewImg.itemReviewImg;
		QItemReviewAnswer ira = QItemReviewAnswer.itemReviewAnswer;
		
		List<ItemReviewHistoryDto> content = queryFactory
					.select(Projections.constructor(
							ItemReviewHistoryDto.class,
							i.id.as("itemId"),
							ir.id.as("itemReviewId"),
							ir.rating,
							ir.content,
							ir.itemReviewStatus,
							i.itemNm,
							im.imgUrl,
							ir.regTime
							))
					.from(ir)
					.join(i).on(ir.item.id.eq(i.id))
					.join(im).on(i.id.eq(im.item.id).and(im.imgMainOk.eq(ImgMainOk.Y)))
					.where(whereClause(memberId,myPageSerchDto))
					.orderBy(ir.regTime.desc())
					.offset(pageable.getOffset())
				    .limit(pageable.getPageSize())
				    .fetch();
		
		for (ItemReviewHistoryDto dto : content) {
		    		List<ItemReviewImg> itemReviewImg = queryFactory
		    				.select(iri)
		    				.from(iri)
		    				.where(iri.itemReview.id.eq(dto.getItemReviewId()))
		    				.fetch();
		    		
		    		ItemReviewAnswer itemReviewAnswer = queryFactory
		    				.select(ira)
		    				.from(ira)
		    				.where(ira.itemReview.id.eq(dto.getItemReviewId()))
		    				.fetchOne();
		    		
		    dto.setItemReviewImgList(itemReviewImg);
		    dto.setItemReviewAnswer(itemReviewAnswer);
		}
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(ir)
				.join(i).on(ir.item.id.eq(i.id))
				.join(im).on(i.id.eq(im.item.id).and(im.imgMainOk.eq(ImgMainOk.Y)))
				.where(whereClause(memberId,myPageSerchDto))
				.fetchOne();
		
		
		return new PageImpl<>(content, pageable, total);
	}

}
