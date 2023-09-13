package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipe.dto.MemberBestDto;
import com.recipe.dto.MemberMainDto;
import com.recipe.entity.Member;
import com.recipe.entity.Recipe;


public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom  {
	
	
		//select * from member where email = ?
		Member findByEmail(String email);
		
		//이메일 찾기
		@Query("SELECT m.email FROM Member m WHERE m.phoneNumber = :phoneNumber")
		String findEmailByPhoneNumber(@Param("phoneNumber") String phoneNumber);
		
		Member findByname(String name);
		
		@Query(value = "select * from member where member_id = ?1", nativeQuery = true)
		Member getfindmemberbyid(Long id);
		
	
//	모든 회원의 팔로워수 / 팔로잉수 / 레시피수 /닉네임 / 자기소개 / id (팔로워 많은순으로 정렬)  
//	메인화면
	@Query ( value = "SELECT " +
			"ROW_NUMBER() OVER (ORDER BY followers.followers_count DESC) num, "+
			"m.member_id id, " +
			"m.nickname nickname, " +
			"ifnull(img.img_url , 'none') imgUrl, " +
            "ifnull(followers.followers_count,0) followCount, " +
            "ifnull(followings.followings_count,0) followingCount, " +
            "ifnull(recipes.recipes_count,0) recipeCount, " +
            "ifnull(img.img_name , 'none') imgName, " +
            "ifnull(img.img_main_ok , 'none') imgMainOk " +
            "FROM member m " +
            "LEFT JOIN (SELECT to_member, COUNT(member_id) AS followers_count FROM follow GROUP BY to_member) followers ON m.member_id = followers.to_member " +
            "LEFT JOIN (SELECT member_id, COUNT(to_member) AS followings_count FROM follow GROUP BY member_id) followings ON m.member_id = followings.member_id " +
            "LEFT JOIN (SELECT member_id, COUNT(recipe_id) AS recipes_count FROM recipe GROUP BY member_id) recipes ON m.member_id = recipes.member_id " +
            "LEFT JOIN member_img img ON m.member_id = img.member_id " +
            "WHERE img.img_main_ok = 'Y' " +
            "OR img.member_id IS NULL " +
            "ORDER BY followers.followers_count DESC"
			, nativeQuery = true)
	List<MemberMainDto> getMemberBestList();
	
	
	
	
	
	@Query(value="SELECT\r\n"
			+ "    m.member_id id ,\r\n"
			+ "    COALESCE(followers.followers_count, 0) followCount ,\r\n"
			+ "    COALESCE(followings.followings_count, 0) FollowingCount ,\r\n"
			+ "    COALESCE(recipes.recipes_count, 0) recipeCount,\r\n"
			+ "    COALESCE(rv_avg, 0.0) retingAvg,\r\n"
			+ "    COALESCE(total_recipe_count, 0) totalCountCount,\r\n"
			+ "    COALESCE(total_comment_count, 0) commentCount,\r\n"
			+ "    COALESCE(total_review_count, 0) reviewCount, \r\n"
			+ "    m.nickname nickname ,\r\n"
			+ "    img.img_url imgUrl \r\n"
			+ "FROM member m\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT to_member, COUNT(member_id) AS followers_count\r\n"
			+ "    FROM follow\r\n"
			+ "    GROUP BY to_member\r\n"
			+ ") followers ON m.member_id = followers.to_member\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT member_id, COUNT(to_member) AS followings_count\r\n"
			+ "    FROM follow\r\n"
			+ "    GROUP BY member_id\r\n"
			+ ") followings ON m.member_id = followings.member_id\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT member_id, COUNT(recipe_id) AS recipes_count\r\n"
			+ "    FROM recipe\r\n"
			+ "    GROUP BY member_id\r\n"
			+ ") recipes ON m.member_id = recipes.member_id\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT r.member_id, AVG(rv.reting) AS rv_avg\r\n"
			+ "    FROM recipe r\r\n"
			+ "    LEFT JOIN review rv ON r.recipe_id = rv.recipe_id\r\n"
			+ "    GROUP BY r.member_id\r\n"
			+ ") rv_avg ON m.member_id = rv_avg.member_id\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT member_id, SUM(count) AS total_recipe_count\r\n"
			+ "    FROM recipe\r\n"
			+ "    GROUP BY member_id\r\n"
			+ ") total_recipe_count ON m.member_id = total_recipe_count.member_id\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT r.member_id, COUNT(c.comment_id) AS total_comment_count\r\n"
			+ "    FROM recipe r\r\n"
			+ "    LEFT JOIN comment c ON r.recipe_id = c.recipe_id\r\n"
			+ "    GROUP BY r.member_id\r\n"
			+ ") total_comment_count ON m.member_id = total_comment_count.member_id\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT r.member_id, COUNT(rv.review_id) AS total_review_count\r\n"
			+ "    FROM recipe r\r\n"
			+ "    LEFT JOIN review rv ON r.recipe_id = rv.recipe_id\r\n"
			+ "    GROUP BY r.member_id\r\n"
			+ ") total_review_count ON m.member_id = total_review_count.member_id \r\n"
			+ "LEFT JOIN member_img img ON m.member_id = img.member_id\r\n"
			+ "WHERE img.img_main_ok = 'Y' \r\n"
			+ "    OR img.member_id IS NULL\r\n"
			+ "ORDER BY followers.followers_count DESC\r\n"  , 
			nativeQuery = true)
	List<MemberBestDto> getRankMemberList();
	
	
	
}
