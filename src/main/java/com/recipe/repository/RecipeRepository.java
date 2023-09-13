package com.recipe.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recipe.dto.RecipeCategoryDto;
import com.recipe.dto.RecipeMainDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.entity.Recipe;


public interface RecipeRepository extends JpaRepository<Recipe , Long>  ,  RecipeRepositoryCustom
										{
	
	
	@Query(value = "SELECT * FROM recipe WHERE member_id = ?1  ORDER BY recipe_id DESC" , nativeQuery = true)
	List<Recipe> findRecipe(Long id);
	
	@Query(value = "SELECT * FROM recipe WHERE member_id = ?1 AND writing_status = 'PUBLISHED' ORDER BY recipe_id DESC ", nativeQuery = true)
	List<Recipe> findAllRecipe(Long id);
	
	
	@Query(value = "SELECT r.* FROM recipe r " +
            "JOIN (SELECT recipe_id, COUNT(*) AS bookmark_count " +
            "      FROM book_mark " +
            "      GROUP BY recipe_id) b ON r.recipe_id = b.recipe_id " +
            "WHERE r.member_id = ?1 AND writing_status = 'PUBLISHED' " +
            "ORDER BY b.bookmark_count DESC " +
            "LIMIT 5", nativeQuery = true)
List<Recipe> getPopularRecipe(Long id);
	
	
	
	
//	조회수 가장 높은순으로 모든레시피 가져옴 (4개)
//	메인
	@Query ( value = "select recipe.recipe_id id , recipe.title title , recipe.sub_title subTitle , "
			+ "recipe.intro intro , recipe.dur_time durTime , recipe.image_url imageUrl ,  "
			+ "recipe.level level , recipe.count count "
			+ "from recipe "
			+ "where recipe.writing_status = 'PUBLISHED'"
			+ "order by count desc, RAND() limit 4" 
			, nativeQuery = true)
	List<RecipeMainDto> getRecipeHeaderBestList();

//	최근 등록순으로 모든레시피 가져옴 (제한 15개)
//	메인
	@Query ( value = "SELECT \r\n"
			+ "  r.recipe_id id, \r\n"
			+ "  r.title title, \r\n"
			+ "  r.sub_title subTitle, \r\n"
			+ "  r.intro intro, \r\n"
			+ "  r.dur_time durTime, \r\n"
			+ "  r.level level, \r\n"
			+ "  r.count count, \r\n"
			+ "  m.nickname nickname, \r\n"
			+ "  r.image_url imageUrl , \r\n"
			+ "  COALESCE(img.img_url, '/img/free-icon-member-5867267.png') imgUrl , "
			+ "  m.member_id memberId "
			+ "FROM recipe r\r\n"
			+ "JOIN member m ON r.member_id = m.member_id\r\n"
			+ "LEFT JOIN (\r\n"
			+ "  SELECT img_url, member_id\r\n"
			+ "  FROM member_img\r\n"
			+ "  WHERE img_main_ok = 'Y'\r\n"
			+ ") img ON m.member_id = img.member_id\r\n"
			+ " where r.writing_status = 'PUBLISHED'"
			+ "ORDER BY r.reg_time DESC limit 15"
			, nativeQuery = true)
	List<RecipeMainDto> getRecipeNewList();
	
	
	
	@Query ( value = "SELECT \r\n"
			+ "    ifnull(bm_count , 0) bookCount ,"
			+ "    ifnull(rv_count , 0) reviewCount ,\r\n"
			+ "    COALESCE(rv_avg, 0) retingAvg ,\r\n"
			+ "    r.recipe_id id , r.count count , r.dur_time durTime , r.image_url imageUrl , "
			+ "    r.level level , r.sub_title subTitle, r.title title , r.member_id memberId ,"
			+ "    r.reg_time regTime , r.intro intro ,\r\n"
			+ "    m.nickname nickname ,\r\n"
			+ "    COALESCE(mi.img_url, '/img/free-icon-member-5867267.png') imgUrl \r\n"
			+ "FROM recipe r "
			+ "LEFT JOIN ( "
			+ "    SELECT recipe_id, COUNT(*) AS bm_count\r\n"
			+ "    FROM book_mark\r\n"
			+ "    GROUP BY recipe_id\r\n"
			+ ") bm ON r.recipe_id = bm.recipe_id "
			+ "LEFT JOIN (\r\n"
			+ "    SELECT recipe_id, COUNT(*) AS rv_count, COALESCE(AVG(reting), 0) AS rv_avg\r\n"
			+ "    FROM review\r\n"
			+ "    GROUP BY recipe_id\r\n"
			+ ") rv ON r.recipe_id = rv.recipe_id\r\n"
			+ "JOIN member m ON r.member_id = m.member_id\r\n"
			+ "LEFT JOIN member_img mi ON m.member_id = mi.member_id AND mi.img_main_ok = 'Y'\r\n"
			+ " where r.writing_status = 'PUBLISHED'"
			+ "ORDER BY rv_count DESC limit 10 "
			, nativeQuery = true)
	List<RecipeMainDto> getRecipeBestList();
	

}
