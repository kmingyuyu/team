package com.recipe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.dto.MngRecipeDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.entity.QComment;
import com.recipe.entity.QMember;
import com.recipe.entity.QRecipe;
import com.recipe.entity.QReview;

import jakarta.persistence.EntityManager;

public class RecipeListRepositoryCustomImpl implements RecipeListRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public RecipeListRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchByLike(String searchBy , String searchQuery) {
		QMember m = QMember.member;
		QRecipe r = QRecipe.recipe;

		if (StringUtils.equals("nickname", searchBy)) {
			// 등록자로 검색시
			return m.nickname.like("%" + searchQuery + "%"); // nickname like %검색어%
		} else if (StringUtils.equals("intro", searchBy)) {
			return r.intro.like("%" + searchQuery + "%"); // description like %검색어%
		}

		return null; // 쿼리문을 실행하지 않는다.
	}

	@Override
	public Page<MngRecipeDto> getAdminRecipePage(MngRecipeSearchDto mngRecipeSearchDto , Pageable pageable) {
		QRecipe r = QRecipe.recipe;
		QComment c = QComment.comment;
		QMember m = QMember.member;
		QReview rv = QReview.review;

		List<MngRecipeDto> content = queryFactory
				.select(
						Projections.constructor(
								MngRecipeDto.class, 
								r.id,
								r.title,
								r.intro,
								r.member.nickname,
								c.count().as("CommentCount"),
								r.imageUrl,
								rv.reting
								))
				.from(r)
				.join(r.member , m)
				.leftJoin(c).on(r.id.eq(c.recipe.id))
				.leftJoin(rv).on(r.id.eq(rv.recipe.id))
				.where(searchByLike(mngRecipeSearchDto.getSearchBy(),
						mngRecipeSearchDto.getSearchQuery()))
				.groupBy(r.id, r.title, r.intro, r.member.nickname, r.imageUrl, rv.reting)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(Wildcard.count)
				.from(r)
				.join(r.member , m)
				.leftJoin(c).on(r.id.eq(c.recipe.id))
				.where(searchByLike(mngRecipeSearchDto.getSearchBy(),
						mngRecipeSearchDto.getSearchQuery()))
				.fetchOne();
		
		if (total == null) {
		    total = 0L; // 검색 결과가 없을 때 0으로 초기화
		}

		return new PageImpl<>(content, pageable, total);

	}
}
