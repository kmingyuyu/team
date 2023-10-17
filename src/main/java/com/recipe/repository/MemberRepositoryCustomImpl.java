package com.recipe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.dto.MemberDto;
import com.recipe.dto.MngMemberDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.entity.QComment;
import com.recipe.entity.QMember;
import com.recipe.entity.QRecipe;

import jakarta.persistence.EntityManager;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public MemberRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchByLike(QMember m, String searchBy, String searchQuery) {
		if (StringUtils.equals("email", searchBy)) {
			// 등록자로 검색시
			return QMember.member.email.like("%" + searchQuery + "%"); // email like %검색어%
		} else if (StringUtils.equals("nickname", searchBy)) {
			return QMember.member.nickname.like("%" + searchQuery + "%"); // nickName like %검색어%
		}

		return null; // 쿼리문을 실행하지 않는다.
	}

	@Override
	public Page<MngMemberDto> getAdminMemberPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		QMember m = QMember.member;
		

		JPQLQuery<MngMemberDto> query = queryFactory
				.select(Projections.constructor(
						MngMemberDto.class, 
						m.id, 
						m.nickname, 
						m.email, 
						m.phoneNumber,
						m.postCode,
						m.address,
						m.detailAddress,
						m.provider,
						m.regTime
						))

				.from(m)
				.where(searchByLike(m, mngRecipeSearchDto.getSearchBy(), mngRecipeSearchDto.getSearchQuery()))
				.orderBy(m.id.desc());

		List<MngMemberDto> content = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		
		long total = query.fetchCount();

		return new PageImpl<>(content, pageable, total);
	}

}
