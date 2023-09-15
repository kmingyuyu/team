package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recipe.entity.BookMark;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

	
	@Query(value ="select b.* from book_mark b where b.member_id = ?1 " , nativeQuery = true)
	List<BookMark> getBookmarks(Long id);
	
	List<BookMark> findByIsDeleteTrue();
	
    // 특정 레시피 ID에 대한 북마크 수를 계산하는 메서드
    int countByRecipeId(Long recipeId);

}
