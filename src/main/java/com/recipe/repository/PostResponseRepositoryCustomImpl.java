package com.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class PostResponseRepositoryCustomImpl implements PostResponseRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public PostResponseRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

}
