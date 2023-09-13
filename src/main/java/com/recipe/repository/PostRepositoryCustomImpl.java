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
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.PostDto;
import com.recipe.entity.QMember;
import com.recipe.entity.QPost;

import jakarta.persistence.EntityManager;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public PostRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchByLike(QMember m, String searchBy, String searchQuery) {
		if (StringUtils.equals("nickname", searchBy)) {
			// 등록자로 검색시
			return QMember.member.nickname.like("%" + searchQuery + "%"); // title like %검색어%
		} else if (StringUtils.equals("email", searchBy)) {
			return QMember.member.email.like("%" + searchQuery + "%"); // content like %검색어%
		}

		return null; // 쿼리문을 실행하지 않는다.
	}

	@Override
	public Page<PostDto> getAdminPostListPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		QPost p = QPost.post;
		QMember m = QMember.member;
		
		JPQLQuery<PostDto> query = queryFactory
                .select(Projections.constructor(
                        PostDto.class,
                        p.id,
                        m.nickname,
                        m.email,
                        p.title,
                        p.content,
                        p.regTime,
                        p.updateTime,
                        p.postReplyStatus
                        
                ))
                .from(p)
                .leftJoin(p.member, m)
                .where(searchByLike(m, mngRecipeSearchDto.getSearchBy(), mngRecipeSearchDto.getSearchQuery()))
                .groupBy(p.id, p.title, p.content, p.regTime, m.nickname, m.email)
                .orderBy(p.regTime.desc());

		List<PostDto> content = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<PostDto> getPostListPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		return null;
	}

}
