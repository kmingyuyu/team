package com.recipe.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.dto.MyPageDto;
import com.recipe.dto.MyPageSerchDto;
import com.recipe.entity.Member;
import com.recipe.entity.Recipe;
import com.recipe.repository.BookMarkRepository;
import com.recipe.repository.FollowRepository;
import com.recipe.repository.MemberRepository;
import com.recipe.service.MyPageService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MypageController {

	private final MyPageService myPageService;
	

	
	private final MemberRepository memberRepository;
	private final BookMarkRepository bookmarkRepository;
	private final FollowRepository followRepository;

	// 마이페이지 보여주기 (레시피)
	@GetMapping(value = {"/myPage" , "/myPage/{page}" })
	public String myPage(@PathVariable("page") Optional<Integer> page ,
			HttpSession session, Model model , MyPageSerchDto myPageSerchDto ) {


		Long id = (Long) session.getAttribute("memberId");
		
		Pageable Pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		
		Page<Recipe> recipeList = myPageService.findRecipePage(id,myPageSerchDto.getData(),Pageable);
		
		
		model.addAttribute("recipeList", recipeList); // 레시피목록
		model.addAttribute("myPageSerchDto", myPageSerchDto); 
		model.addAttribute("maxPage", 5); 
		
		/*
		myPageService.deleteMarkedBookmarks(); // 여기서 삭제 로직을 호출
		 * List<MyPageDto> bookmarkList = myPageService.getBookmark(id);
		 * 
		 * List<MyPageDto> myCommentList = myPageService.getMyComment(id);
		 * 
		 * List<MyPageDto> myReviewList = myPageService.getMyReview(id);
		 * 
		 * List<MyPageDto> receivedReviewList = myPageService.getReceivedReview(id);
		 */

		/*
		 * model.addAttribute("receivedReviewList", receivedReviewList);
		 * model.addAttribute("myReviewList", myReviewList);
		 * model.addAttribute("myCommentList", myCommentList);
		 * model.addAttribute("bookmarkList", bookmarkList);
		 */
		return "myPage/myPageResipe";

	}

	
	
	
//	마이페이지 보여주기 (나의 상품 문의)
	@GetMapping(value = {"/myPage/item_inq" , "/myPage/item_inq/{page}" })
	public String myPageItemReview(@PathVariable("page") Optional<Integer> page ,HttpSession session, Model model , MyPageSerchDto myPageSerchDto) {
		
		
		
		
		return "myPage/itemReviewAndInq/myItemReview";
	}
	
	
	
	


	// 프로필 보여주기
	@GetMapping(value = "/profile/{nickname}")
	public String profile(@PathVariable("nickname") String nickname, Model model, HttpSession session) {

		Member member = memberRepository.findByNickname(nickname);

		if (member == null) {
			return "/";
		}

		Long id = member.getId();

		myPageService.getFollowingCount(id);
		Member myPageDto = memberRepository.getfindmemberbyid(id);
		List<Recipe> allRecipeList = myPageService.getAllRecipeList(id);
		List<Recipe> popularRecipeList = myPageService.getPopularRecipeList(id);
		for (Recipe recipe : allRecipeList) {
			Long recipeId = recipe.getId();
			int bookmarkCount = bookmarkRepository.countByRecipeId(recipeId);
		}
		
		String email = null;
		if(session.getAttribute("email") != null) {
		   email = session.getAttribute("email").toString();
		   model.addAttribute("email", email.equals(myPageDto.getEmail()));
		}
		
		Long follower = followRepository.countFromMember(id);
		Long following = followRepository.countToMember(id);
		
		model.addAttribute("follower", follower);
		model.addAttribute("following", following);
		model.addAttribute("myPageDto", myPageDto);// 회원정보
		model.addAttribute("allRecipeList", allRecipeList); // 레시피목록
		model.addAttribute("popularRecipeList", popularRecipeList); // 레시피목록
		return "profile";
	}

	// 회원정보수정하기
	@PostMapping(value = "/myPage/{id}")
	public String editMember(@PathVariable("id") Long id, @Valid MyPageDto myPageDto, Model model,
			@RequestParam("imgFile") MultipartFile imgFile) {

		try {
			// 이미지 파일 업로드 처리
			// imgUrl을 MyPageDto에 설정

			myPageService.editMember(myPageDto, imgFile);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/myPage";
	}

	// 회원탈퇴
	@DeleteMapping("/myPage/deleteMember/{memberId}")
	public @ResponseBody ResponseEntity deleteMember(@PathVariable("memberId") Long id, @Valid MyPageDto myPageDto) {
		myPageService.deleteMember(id);

		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	// 레시피목록 페이지 -> 수정

	// 레시피목록 페이지 -> 삭제
	@DeleteMapping("/myPage/deleteRecipe/{recipeId}")
	public @ResponseBody ResponseEntity deleteRecipe(@PathVariable("recipeId") Long recipeId) {

		myPageService.deleteRecipe(recipeId);

		return new ResponseEntity<Long>(recipeId, HttpStatus.OK);
	}

	// 찜목록 페이지 -> 찜삭제
	@DeleteMapping("/myPage/deleteBookmark/{bookmarkId}")
	public @ResponseBody ResponseEntity deleteBookmark(@PathVariable("bookmarkId") Long bookmarkId) {
		myPageService.deleteBookmark(bookmarkId);

		return new ResponseEntity<Long>(bookmarkId, HttpStatus.OK);
	}

	// 찜목록 페이지 -> 찜삭제취소
	@PostMapping("/myPage/undeleteBookmark/{bookmarkId}")
	public @ResponseBody ResponseEntity undeleteBookmark(@PathVariable("bookmarkId") Long bookmarkId) {
		myPageService.undeleteBookmark(bookmarkId);

		return new ResponseEntity<Long>(bookmarkId, HttpStatus.OK);
	}

	// 댓글목록 -> 댓글삭제
	@DeleteMapping("myPage/deleteComment/{commentId}")
	public @ResponseBody ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {

		myPageService.deleteComment(commentId);

		return new ResponseEntity<Long>(commentId, HttpStatus.OK);
	}

	// 후기목록 -> 후기삭제
	@DeleteMapping("myPage/deleteReview/{reviewId}")
	public @ResponseBody ResponseEntity deleteReview(@PathVariable("reviewId") Long reviewId) {
		myPageService.deleteReview(reviewId);
		return new ResponseEntity<Long>(reviewId, HttpStatus.OK);

	}

	// 팔로우
	@PostMapping(value = "/follow/{followingId}")
	public ResponseEntity<String> followMember(@PathVariable Long followingId, Principal principal) {

		// 로그인한 사용자의 정보를 가져옵니다.
		// 여기서는 이메일을 사용자 ID로 가정합니다. 필요에 따라 다르게 구현할 수 있습니다.
		String followerEmail = principal.getName();
		Member follower = memberRepository.findByEmail(followerEmail);

		// 팔로우 대상 사용자의 정보를 가져옵니다.
		Member following = memberRepository.findById(followingId)
				.orElseThrow(() -> new EntityNotFoundException("Following user not found"));
		followingId = following.getId();
		// 팔로우 기능을 실행합니다.
		// 이 부분은 실제 팔로우 로직에 따라 다르게 구현될 수 있습니다.
		if (myPageService.isFollowing(followingId, follower.getId())) {

			return ResponseEntity.badRequest().body("이미 팔로우한 사용자입니다.");

		} else {

			myPageService.saveFollow(followingId, principal);
			return ResponseEntity.ok("Followed successfully.");
		}

	}

	// 언팔로우
	@DeleteMapping(value = "/unfollow/{followingId}")
	public ResponseEntity<String> unfollowMember(@PathVariable Long followingId, Principal principal) {
		// 팔로우 대상 사용자의 정보를 가져옵니다.
		Member following = memberRepository.findById(followingId)
				.orElseThrow(() -> new EntityNotFoundException("Following user not found"));

		// 로그인한 사용자의 정보를 가져옵니다.
		String followerEmail = principal.getName();
		Member follower = memberRepository.findByEmail(followerEmail);

		// 언팔로우 기능을 실행합니다.
		boolean success = myPageService.unfollow(following.getId(), follower.getId());

		if (success) {
			return ResponseEntity.ok("Unfollowed successfully.");
		} else {
			return ResponseEntity.badRequest().body("Unfollow failed.");
		}
	}

	// 팔로우 여부 체크
	@GetMapping("/api/follow/check/{toMemberId}")
	@ResponseBody // 이 어노테이션을 사용하여 반환값을 HTTP 응답 본문으로 설정
	public boolean checkFollowStatus(@PathVariable Long toMemberId, Principal principal) {
		Member toMember = memberRepository.findById(toMemberId)
				.orElseThrow(() -> new EntityNotFoundException("Following user not found"));
		String email = principal.getName();
		Member fromMember = memberRepository.findByEmail(email);
		return followRepository.existsByToMemberAndMember(toMember.getId(), fromMember);
	}
	


}
