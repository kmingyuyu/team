package com.recipe.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.constant.CategoryEnum;
import com.recipe.constant.ImgMainOk;
import com.recipe.constant.ItemCategoryEnum;
import com.recipe.dto.ItemCategoryDto;
import com.recipe.dto.ItemDetailDto;
import com.recipe.dto.ItemDetailImgDto;
import com.recipe.dto.ItemImgDto;
import com.recipe.dto.ItemInqDto;
import com.recipe.dto.ItemReviewAnswerDto;
import com.recipe.dto.ItemReviewDto;
import com.recipe.dto.ItemSearchDto;
import com.recipe.entity.QBookMark;
import com.recipe.entity.QItem;
import com.recipe.entity.QItemDetailImg;
import com.recipe.entity.QItemImg;
import com.recipe.entity.QItemInq;
import com.recipe.entity.QItemInqAnwser;
import com.recipe.entity.QItemReview;
import com.recipe.entity.QItemReviewAnswer;
import com.recipe.entity.QMember;
import com.recipe.entity.QMemberImg;
import com.recipe.entity.QRecipe;
import com.recipe.entity.QReview;

import jakarta.persistence.EntityManager;

public class ItemRepositoryImpl implements ItemRepositoryCustom  {
	
	private JPAQueryFactory queryFactory;

	public ItemRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	
	private OrderSpecifier<?> orderByType(String type) {
		
		QItem i = QItem.item;
		QItemReview ir = QItemReview.itemReview;
	    
	    
	    if ("order".equals(type)) {
	        return i.regTime.desc();
	    }
	    else if ("reviewAvg".equals(type)) {
	    	return ir.reting.avg().coalesce(0.0).desc();
	    }
	    else if ("reviewCount".equals(type)) {
	    	return ir.count().desc();
	    }
	    else if ("lowPrice".equals(type)) {
	    	return i.price.asc();
	    }
	    else if ("highPrice".equals(type)) {
	    	return i.price.desc();
	    }
	    else {
	    	return i.regTime.desc();
	    }
	    
	}
	
	private BooleanExpression searchByLike(String searchQuery) {
		 QItem i = QItem.item;
			return i.itemNm.like("%" + searchQuery + "%");
	}
	
	private BooleanExpression mainCategoryEq(ItemCategoryEnum itemCategoryEnum) {
		 QItem i = QItem.item;
		  return itemCategoryEnum == null ? null : i.itemCategoryEnum.eq(itemCategoryEnum); 
	  }
		 
	
	
	
//	상품 모든정보 가져오기
	@Override
	public Page<ItemCategoryDto> getItemCategoryList(Pageable pageable , ItemSearchDto itemSearchDto) {
		
		QItem i = QItem.item;
		QItemImg im = QItemImg.itemImg;
		QItemReview ir = QItemReview.itemReview;
		
		
		List<ItemCategoryDto> content = queryFactory
		        .select(
		            Projections.constructor(
		                ItemCategoryDto.class,
		                i.id,
		                i.itemNm,
		                i.itemSubNm,
		                i.price,
		                i.itemSellStatus,
		                im.imgUrl,
		                i.itemCategoryEnum,
		                i.regTime,
		                i.sale,
		                ir.reting.avg().coalesce(0.0).as("retingAvg"),
		                ir.count().as("reviewCount") 
		            )
		        )
		        .from(i)
		        .join(im).on(im.item.id.eq(i.id).and(im.imgMainOk.eq(ImgMainOk.Y))) 
		        .leftJoin(ir).on(i.id.eq(ir.item.id))
		        .where( mainCategoryEq(itemSearchDto.getItemCategoryEnum()),
		        		searchByLike(itemSearchDto.getSearchQuery()))
		        .groupBy(i.id, i.itemNm, i.itemSubNm, i.price, i.itemSellStatus, i.itemCategoryEnum,
		                i.sale, im.imgUrl, i.regTime)
		        .orderBy(orderByType(itemSearchDto.getType()))
		        .offset(pageable.getOffset())
		        .limit(pageable.getPageSize())
		        .fetch();
		    
		    Long total = queryFactory
		        .select(Wildcard.count)
		        .from(i)
		        .join(im).on(im.item.id.eq(i.id).and(im.imgMainOk.eq(ImgMainOk.Y)))
		        .leftJoin(ir).on(i.id.eq(ir.item.id))
		        .where( mainCategoryEq(itemSearchDto.getItemCategoryEnum()),
		        		searchByLike(itemSearchDto.getSearchQuery()))
		        .fetchOne();
		    
		    return new PageImpl<>(content, pageable, total);
	}


//  상품 상세정보 가져오기
	@Override
	public ItemDetailDto getItemDetailList(Long itemId) {
		
		QItem i = QItem.item;
		QItemImg im = QItemImg.itemImg;
		QItemReview ir = QItemReview.itemReview;
		QItemDetailImg idm = QItemDetailImg.itemDetailImg;
		
		List<ItemImgDto> itemImgDto = queryFactory
										.select(
												Projections.constructor(
														ItemImgDto.class,
														im.id,
														im.imgUrl,
														im.imgName,
														im.imgOriName,
														im.imgMainOk
														))
										.from(im)
										.where(im.item.id.eq(itemId))
										.fetch();
		
		List<ItemDetailImgDto> itemDetialImgDto = queryFactory
				.select(
						Projections.constructor(
								ItemDetailImgDto.class,
								idm.id,
								idm.imgUrl,
								idm.imgName,
								idm.imgOriName
								))
				.from(idm)
				.where(idm.item.id.eq(itemId))
				.fetch();
		
		
		ItemDetailDto itemDetail = queryFactory
											.select(
													Projections.constructor(
													ItemDetailDto.class,
													i.id,
													i.itemNm,
													i.itemSubNm,
													i.price,
													i.itemSellStatus,
													i.itemCategoryEnum,
													i.sale,
													i.stockNumber,
													ir.reting.avg().coalesce(0.0).as("retingAvg"),
													ir.item.count().as("reviewCount")
													))
											.from(i)
											.leftJoin(ir).on(ir.item.id.eq(i.id))
											.where(i.id.eq(itemId))
											.fetchOne();
		
		itemDetail.setItemImgDtoList(itemImgDto);
		itemDetail.setItemDetailImgDtoList(itemDetialImgDto);
		
		return itemDetail;
	}


//	상품리뷰 / 리뷰 답변 가져오기
	@Override
	public Page<ItemReviewDto> getItemReviewList(Pageable pageable , Long itemId ) {
		
		QItemReview ir = QItemReview.itemReview;
		QMember m = QMember.member;
		QMemberImg mi = QMemberImg.memberImg;
		QItemReviewAnswer a = QItemReviewAnswer.itemReviewAnswer;
		
		List<ItemReviewDto> content = queryFactory
									.select(
											Projections.constructor(
													ItemReviewDto.class,
													ir.id,
													ir.reting,
													ir.content,
													ir.regTime,
													m.nickname,
													mi.imgUrl,
													a.id.as("answerId"),
													a.content.as("answerContent"),
													a.regTime.as("answerRegTime")
													))	
									.from(ir)
									.join(m).on(ir.member.id.eq(m.id))
									.leftJoin(mi).on(QMember.member.id.eq(mi.member.id).and(mi.imgMainOk.eq(ImgMainOk.Y)))
									.leftJoin(a).on(QItemReview.itemReview.id.eq(a.itemReview.id))
									.where(ir.item.id.eq(itemId))
									.offset(pageable.getOffset())
								    .limit(pageable.getPageSize())
									.fetch();
		
		Long total = queryFactory
				 	.select(Wildcard.count)
				 	.from(ir)
					.join(m).on(ir.member.id.eq(m.id))
					.leftJoin(mi).on(QMember.member.id.eq(mi.member.id).and(mi.imgMainOk.eq(ImgMainOk.Y)))
					.where(ir.item.id.eq(itemId))
					.fetchOne();
		
		
		
		return new PageImpl<>(content, pageable, total);
	}

//  상품문의 /문의답변 가져오기
	@Override
	public Page<ItemInqDto> getItemInqList(Pageable pageable, Long itemId) {
		
		QItemInq ii = QItemInq.itemInq;
		QItemInqAnwser iia = QItemInqAnwser.itemInqAnwser;
		QMember m = QMember.member;
		
		List<ItemInqDto> content = queryFactory
				.select(
						Projections.constructor(
								ItemInqDto.class,
								ii.id,
								ii.title,
								ii.content,
								ii.itemInqBoardEnum,
								ii.itemInqEnum,
								ii.answerOk,
								ii.regTime,
								m.email,
								m.nickname,
								iia.id.as("answerId"),
								iia.content.as("answerContent"),
								iia.regTime.as("answerRegTime")
								))
				.from(ii)
				.join(m).on(ii.member.id.eq(m.id))
				.leftJoin(iia).on(QItemInq.itemInq.id.eq(iia.itemInq.id))
				.where(ii.item.id.eq(itemId))
				.offset(pageable.getOffset())
			    .limit(pageable.getPageSize())
				.fetch();
		
		Long total = queryFactory
			 	.select(Wildcard.count)
			 	.from(ii)
			 	.join(m).on(ii.member.id.eq(m.id))
				.leftJoin(iia).on(QItemInq.itemInq.id.eq(iia.itemInq.id))
				.where(ii.item.id.eq(itemId))
				.fetchOne();
	
		return new PageImpl<>(content, pageable, total);
	}



	
	
	
	
}
