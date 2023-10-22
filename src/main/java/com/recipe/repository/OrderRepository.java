package com.recipe.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.constant.OrderStatus;
import com.recipe.entity.Order;
import com.recipe.myPage.dto.OrderHistoryDto;

public interface OrderRepository extends JpaRepository<Order, Long> , OrderRepositoryCustom {
	
	Order findByOrderNumber(String orderNumber);
	
	
//	마이페이지 주문관리
	 @Query(value=" SELECT "
		 		+ " i.item_id itemId ,"
		 		+ " oi.order_item_id orderItemId ,"
		 		+ " i.item_nm itemNm , "
		 		+ " oi.order_price orderPrice ,"
		 		+ " oi.sale_price salePrice ,"
		 		+ " oi.count orderCount , "
		 		+ " oi.order_status orderStatus , "
		 		+ " img.img_url imgUrl ,"
		 		+ " o.order_number orderNumber ,"
		 		+ " d.invoice_number invoiceNumber ,"
		 		+ " o.reg_time regTime , "
		 		+ " (CASE WHEN ir.order_item_id IS NOT NULL THEN 'true' ELSE 'false' END) reviewOk  "
		 		+ " FROM orders o\r\n"
		 		+ " JOIN order_item oi ON o.order_id = oi.order_id \r\n"
		 		+ " JOIN member m ON m.member_id =  :memberId \r\n"
		 		+ " LEFT JOIN delivery d ON o.order_number = d.order_number \r\n"
		 		+ " JOIN item i ON oi.item_id = i.item_id \r\n"
		 		+ " JOIN item_img img ON i.item_id = img.item_id "
		 		+ " LEFT JOIN item_review ir ON oi.order_item_id = ir.order_item_id "
		 		+ " WHERE img.img_main_ok = 'Y' AND o.member_id = :memberId \r\n "
		 		+ " AND o.reg_time BETWEEN :startDate AND :endDate "
		 		+ " AND i.item_nm like CONCAT('%', :searchQuery, '%') "
		 		+ " AND oi.order_status like CONCAT('%', :orderStatus, '%') "
		 		+ " ORDER BY o.reg_time DESC" , nativeQuery = true)
	Page<OrderHistoryDto> findByMyOrderList(
			@Param("memberId") Long memberId ,
			@Param("startDate") LocalDateTime startDate, 
	        @Param("endDate") LocalDateTime endDate, 
	        @Param("searchQuery") String searchQuery, 
	        @Param("orderStatus") String data,
	        Pageable pageable);
	
	
	Page<Order> findAll(Specification<Order> spec, Pageable pageable);
	
	
	
	@Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems oi WHERE oi.orderStatus = :orderStatus")
	List<Order> findByOrderStatus(@Param("orderStatus") OrderStatus orderStatus);

}
