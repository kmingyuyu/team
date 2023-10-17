package com.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.constant.OrderStatus;
import com.recipe.dto.SearchDto;
import com.recipe.entity.Order;
import com.recipe.entity.QOrder;
import com.recipe.entity.QOrderItem;

import jakarta.persistence.EntityManager;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public OrderRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {

		QOrder order = QOrder.order;

		if (StringUtils.equals("orderNumber", searchBy)) {
			return order.orderNumber.like("%" + searchQuery + "%"); 
		} 
		else if (StringUtils.equals("buyerName", searchBy)) {
			return order.buyerName.like("%" + searchQuery + "%") ;
		}
		else if (StringUtils.equals("buyerEmail", searchBy)) {
			return order.buyerEmail.like("%" + searchQuery + "%"); 
		}

		return null; // 쿼리문을 실행하지 않는다.
	}

	@Override
	public Page<Order> findByAdminOrderList(Pageable pageable, SearchDto searchDto) {

		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;

		List<Order> content = queryFactory
					.select(order)
					.distinct()
					.from(order)
					.join(order.orderItems, orderItem)
					.where( searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()),
							orderItem.orderStatus.eq(OrderStatus.주문완료))
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch();
		
		Long total = queryFactory
					.select(Wildcard.count)
					.distinct()
					.from(order)
					.join(order.orderItems, orderItem)
					.where( searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()),
						orderItem.orderStatus.eq(OrderStatus.주문완료))
					.fetchOne();
					
		return new PageImpl<>(content, pageable, total);
	}

}
