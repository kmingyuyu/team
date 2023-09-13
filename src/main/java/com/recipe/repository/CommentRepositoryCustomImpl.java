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
import com.recipe.dto.CommentDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.entity.QComment;
import com.recipe.entity.QMember;
import com.recipe.entity.QRecipe;

import jakarta.persistence.EntityManager;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
	private JPAQueryFactory queryFactory;

	public CommentRepositoryCustomImpl(EntityManager em) {
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
	public Page<CommentDto> getAdminCommentPage(MngRecipeSearchDto MngrecipeSearchDto, Pageable pageable) {
		QComment c = QComment.comment;
		QRecipe r = QRecipe.recipe;
		QMember m = QMember.member;

		// QComment와 QRecipe를 이용하여 댓글 작성자, 댓글 작성자의 이메일, 댓글이 작성된 글의 제목, 글 작성자, 댓글 내용을
		// 조회하는 쿼리 작성
		JPQLQuery<CommentDto> query = queryFactory
				.select(Projections.constructor(
						CommentDto.class, 
						c.id, 
						c.member.nickname, 
						c.member.email, 
						c.recipe.title,
						c.writer,
						c.commentContent))
						.from(c)
						.leftJoin(c.recipe, r)
						.leftJoin(c.member, m)
						.where(searchByLike(m, MngrecipeSearchDto.getSearchBy(), MngrecipeSearchDto.getSearchQuery()))
						.groupBy(c.id, c.member.nickname, c.member.email, c.recipe.title, c.writer, c.commentContent)
						.orderBy(m.id.desc());
						
				

		List<CommentDto> content = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		// 전체 결과 개수를 구하는 쿼리를 작성
		long total = query.fetchCount();

		return new PageImpl<>(content, pageable, total); // 총 개수(total)를 계산하여 사용해야 합니다.
	}
}
