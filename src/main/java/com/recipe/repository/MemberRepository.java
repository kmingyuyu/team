package com.recipe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.recipe.dto.MemberMainDto;
import com.recipe.entity.Member;
import com.recipe.myPage.dto.FollowHistoryDto;


public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom  {
	
	
		//select * from member where email = ?
		Member findByEmail(String email);
		
		Member findByPhoneNumber(String phoneNumber);
		
		Member findByNickname(String nickname);
		
		//이메일 찾기
		@Query("SELECT m.email FROM Member m WHERE m.phoneNumber = :phoneNumber")
		String findEmailByPhoneNumber(@Param("phoneNumber") String phoneNumber);
		
		Member findByname(String name);
		
		@Query(value = "select * from member where member_id = ?1", nativeQuery = true)
		Member getfindmemberbyid(Long id);
		
	
//	모든 회원의 팔로워수 / 팔로잉수 / 레시피수 /닉네임 / 자기소개 / id (팔로워 많은순으로 정렬)  
//	메인화면
	@Query ( value = "SELECT " +
			"m.member_id id, " +
			"m.nickname nickname, " +
			"m.img_url imgUrl, " +
            "ifnull(followers.followers_count,0) followCount, " +
            "ifnull(followings.followings_count,0) followingCount, " +
            "ifnull(recipes.recipes_count,0) recipeCount " +
            "FROM member m " +
            "LEFT JOIN (SELECT to_member, COUNT(member_id) AS followers_count FROM follow GROUP BY to_member) followers ON m.member_id = followers.to_member " +
            "LEFT JOIN (SELECT member_id, COUNT(to_member) AS followings_count FROM follow GROUP BY member_id) followings ON m.member_id = followings.member_id " +
            "LEFT JOIN (SELECT member_id, COUNT(recipe_id) AS recipes_count FROM recipe GROUP BY member_id) recipes ON m.member_id = recipes.member_id " +
            "ORDER BY followers.followers_count DESC"
			, nativeQuery = true)
	List<MemberMainDto> getMemberBestList();
	
	
	@Query(value = "SELECT "
			+ " m.nickname nickname , "
			+ " m.introduce introduce , "
			+ " m.img_url imgUrl , "
			+ " (CASE WHEN f2.member_id IS NOT NULL THEN 'true' ELSE 'false' END) followOk "
            + " FROM member m "
            + " JOIN follow f ON m.member_id = f.member_id "
            + " LEFT JOIN follow f2 ON m.member_id = f2.to_member AND f.to_member = f2.member_id"
            + " WHERE f.to_member = :memberId "
            + " AND m.nickname like CONCAT('%', :searchQuery, '%') "
            + " ORDER BY f.reg_time DESC", nativeQuery = true)
	Page<FollowHistoryDto> findByMyFollowerList(
			@Param("memberId") Long memberId,
			@Param("searchQuery") String searchQuery, 
			Pageable pageable);
	
	
	@Query(value = "SELECT "
			+ " m.nickname nickname , "
			+ " m.introduce introduce , "
			+ " m.img_url imgUrl , "
			+ " f.follow_id followId "
			 + " FROM member m "
			+ " JOIN follow f ON m.member_id = f.to_member "
			+ " WHERE f.member_id = :memberId "
			+ " AND m.nickname like CONCAT('%', :searchQuery, '%') "
			+ " ORDER BY f.reg_time DESC", nativeQuery = true)
	Page<FollowHistoryDto> findByMyFollowingList(
			@Param("memberId") Long memberId,
			@Param("searchQuery") String searchQuery, 
			Pageable pageable);

	
	
}
