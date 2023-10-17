package com.recipe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.recipe.constant.OrderStatus;
import com.recipe.dto.CommentDto;
import com.recipe.dto.ItemCategoryDto;
import com.recipe.dto.ItemSearchDto;
import com.recipe.dto.MemberDto;
import com.recipe.dto.MngMemberDto;
import com.recipe.dto.MngRecipeDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.PostDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.dto.SearchDto;
import com.recipe.entity.Order;
import com.recipe.exception.CustomException;
import com.recipe.service.CommentService;
import com.recipe.service.DeliveryService;
import com.recipe.service.ItemService;
import com.recipe.service.MemberService;
import com.recipe.service.MngService;
import com.recipe.service.PostService;
import com.recipe.service.RecipeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MngController {
	private final DeliveryService deliveryService;
	private final MngService mngService;
	private final ItemService itemService;
	private final MemberService memberService;
	private final RecipeService recipeService;
	private final CommentService commentService;
	private final PostService postService;

	// 회원관리 페이지
	@GetMapping(value = { "/admin/memberMng", "/admin/memberMng/{page}" })
	public String memberMng(MngRecipeSearchDto mngRecipeSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model , HttpSession session) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		Page<MngMemberDto> members = memberService.getAdminMemberPage(mngRecipeSearchDto, pageable);
		
		
		
		model.addAttribute("members", members);
		model.addAttribute("maxPage", 5);

		return "mng/memberMng";
		
		
	}
	
//	상품관리 페이지
	@GetMapping(value = { "/admin/itemMng", "/admin/itemMng/{page}" })
	public String itemMng(@PathVariable("page") Optional<Integer> page ,ItemSearchDto itemSearchDto , Model model , HttpSession session) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10) ;
		
		Page<ItemCategoryDto> items = itemService.getItemCategoryList(pageable, itemSearchDto);
		
		int currentPage = page.isPresent() ? page.get() : 0;
		
		model.addAttribute("items", items);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("maxPage", 5);
		
		return "mng/itemMng";
		
	}
	
//	주문관리 페이지
	@GetMapping(value = { "/admin/orderMng", "/admin/orderMng/{page}" })
	public String orderMng(@PathVariable("page") Optional<Integer> page , SearchDto searchDto , Model model , HttpSession session) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		
		Page<Order> orderList = mngService.getMngOrderPageList(pageable , searchDto);
		
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("maxPage", 5);
		
		return "mng/orderMng";
		
	}
	
//	주문상품 택배사에 배송요청
	@PostMapping(value="/admin/orderDeliveryRequest")
	public @ResponseBody ResponseEntity orderDeliveryRequest(@RequestBody Map<String, Object> requestBody, HttpSession session) {
		
		String role = session.getAttribute("role").toString();
		
		if(!"ADMIN".equals(role) || role == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		try {
			
			String orderNumber = requestBody.get("orderNumber").toString();
			
			if(orderNumber == null || orderNumber.isEmpty()) {
				throw new CustomException("요청된 주문번호가 없습니다. 다시 시도해주세요.");
			}
			
			Order order = mngService.orderDeliveryRequest(orderNumber);
			
			if(order == null) {
				throw new CustomException("요청된 주문번호가 존재하지 않습니다. 다시 시도해주세요.");
			}
			
			deliveryService.deliverySave(order);
			
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<>("수거 요청 완료 되었습니다.",HttpStatus.OK);
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
