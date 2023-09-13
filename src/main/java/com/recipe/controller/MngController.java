package com.recipe.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.dto.CommentDto;
import com.recipe.dto.MemberDto;
import com.recipe.dto.MngMemberDto;
import com.recipe.dto.MngRecipeDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.PostDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.service.CommentService;
import com.recipe.service.MemberService;
import com.recipe.service.PostService;
import com.recipe.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MngController {

	private final MemberService memberService;
	private final RecipeService recipeService;
	private final CommentService commentService;
	private final PostService postService;

	// 회원관리 페이지
	@GetMapping(value = { "/admin/memberMng", "/admin/memberMng/{page}" })
	public String memberMng(MngRecipeSearchDto mngRecipeSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MngMemberDto> members = memberService.getAdminMemberPage(mngRecipeSearchDto, pageable);

		model.addAttribute("members", members);
		model.addAttribute("maxPage", 5);

		return "mng/memberMng";
	}

	// 회원 삭제
	@DeleteMapping("/member/{memberId}/delete")
	public @ResponseBody ResponseEntity deleteMember(@PathVariable("memberId") Long memberId) {
		memberService.deleteMember(memberId);
		return new ResponseEntity<Long>(memberId, HttpStatus.OK);
	}

	// 멤버별 레시피 관리 페이지
	@GetMapping(value = { "/admin/recipeMng", "/admin/recipeMng/{page}" })
	public String recipeMng(MngRecipeSearchDto mngRecipeSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model) {

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);

		Page<MngRecipeDto> recipeDtoList = recipeService.getAdminRecipePage(mngRecipeSearchDto, pageable);

		model.addAttribute("recipes", recipeDtoList);
		model.addAttribute("recipeSearchDto", mngRecipeSearchDto);
		model.addAttribute("maxPage", 5);

		return "mng/recipeMng";
	}

	// 레시피 삭제
	@DeleteMapping("/recipe/{recipeId}/delete")
	public @ResponseBody ResponseEntity deleteRecipe(@PathVariable("recipeId") Long recipeId) {
		recipeService.deleteRecipe(recipeId);
		return new ResponseEntity<Long>(recipeId, HttpStatus.OK);
	}

	// 댓글 관리 페이지
	@GetMapping(value = { "/admin/commentMng", "/admin/commentMng/{page}" })
	public String commentMng(MngRecipeSearchDto mngRecipeSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<CommentDto> comments = commentService.getAdminCommentPage(mngRecipeSearchDto, pageable);

		model.addAttribute("comments", comments);
		model.addAttribute("maxPage", 5);

		return "mng/commentMng";
	}

	// 회원 삭제
	@DeleteMapping("/comment/{commentId}/delete")
	public @ResponseBody ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
		commentService.deleteComment(commentId);
		return new ResponseEntity<Long>(commentId, HttpStatus.OK);
	}

	// 문의내역 관리 페이지
	@GetMapping(value = { "/admin/qaMng", "/admin/qaMng/{page}" })
	public String qaMng(MngRecipeSearchDto mngRecipeSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<PostDto> adminPosts = postService.getAdminPostListPage(mngRecipeSearchDto, pageable);

		model.addAttribute("adminPosts", adminPosts);
		model.addAttribute("maxPage", 5);

		return "mng/qaMng";
	}

	// 문의답변 등록(insert)
	@PostMapping(value = "/post/reg")
	public @ResponseBody ResponseEntity qaListNew(@RequestBody Map<String, Object> requestBody,
			BindingResult bindingResult) {

		try {
			postService.savePostResponse(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("문의 접수 되었습니다", HttpStatus.OK);
		}

		return new ResponseEntity<>("문의 접수 되었습니다", HttpStatus.OK);

	}

}
