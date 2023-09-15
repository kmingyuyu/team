package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.dto.CartDto;
import com.recipe.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findByMemberId(Long memberId);
	
	Long countByMemberId(Long memberId);
	
	Cart findByMemberIdAndItemIdAndCount(Long memberId , Long itemId , int count);
	
	@Query(value="SELECT \r\n"
			+ " c.cart_id cartId, "
			+ " i.item_id itemId, "
			+ " c.count count, "
			+ " i.price price, "
			+ " i.sale sale,"
			+ " i.stock_number stockNumber,"
			+ " i.item_nm itemNm,"
			+ " i.item_sell_status itemSellStatus,"
			+ " img.img_url imgUrl \r\n"
			+ "FROM cart c\r\n"
			+ "JOIN member m ON c.member_id = m.member_id\r\n"
			+ "JOIN item i ON c.item_id = i.item_id\r\n"
			+ "JOIN item_img img ON i.item_id = img.item_id\r\n"
			+ "WHERE m.member_id = :memberId  AND img.img_main_ok = 'Y';"
			, nativeQuery = true)
	List<CartDto> getCartList(@Param("memberId") Long memberId);
	
	
}
