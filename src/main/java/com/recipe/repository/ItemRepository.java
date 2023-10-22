package com.recipe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.entity.Item;
import com.recipe.myPage.dto.OrderHistoryDto;

public interface ItemRepository  extends JpaRepository<Item, Long> , ItemRepositoryCustom{
	
	 Optional<Item> findById(Long id);
	 
	 @Query(value=" SELECT "
	 		+ " i.item_id itemId ,"
	 		+ " i.item_nm itemNm , "
	 		+ " oi.order_price orderPrice ,"
	 		+ " oi.sale_price salePrice ,"
	 		+ " oi.count orderCount , "
	 		+ " oi.order_status orderStatus , "
	 		+ " img.img_url imgUrl \r\n"
	 		+ " FROM orders o\r\n"
	 		+ " JOIN order_item oi ON o.order_id = oi.order_id \r\n"
	 		+ " JOIN item i ON oi.item_id = i.item_id \r\n"
	 		+ " JOIN item_img img ON i.item_id = img.item_id "
	 		+ " WHERE img.img_main_ok = 'Y' AND o.order_id = :orderId  ",nativeQuery = true)
	 List<OrderHistoryDto> orderHistoryList(@Param("orderId") Long id);
	 
}
