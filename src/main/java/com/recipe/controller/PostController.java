package com.recipe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.constant.PostReplyStatus;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.PostAnswerDto;
import com.recipe.dto.PostDto;
import com.recipe.dto.PostResponseDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	// 문의등록 페이지
	@GetMapping(value = "/post/qa")
	public String qa(Model model) {
		model.addAttribute("postDto", new PostDto());
		return "post/qa";
	}

	// 문의 등록(insert)
	@PostMapping(value = "/post/qa")
	public String qaNew(@Valid PostDto postDto, BindingResult bindingResult, Model model,
			@RequestParam("postImgFile") List<MultipartFile> postImgFileList) {

		if (bindingResult.hasErrors()) {
			return "post/qa";
		}

		try {
			postService.savePost(postDto, postImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글 등록중 에러가 발생했습니다.");
			return "post/qa";
		}

		return "redirect:/";

	}

	// 문의리스트 페이지
	@GetMapping(value = { "/post/qaList", "/post/qaList/{page}" })
	public String qaList(MngRecipeSearchDto mngRecipeSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

		Page<PostDto> posts = postService.getAdminPostListPage(mngRecipeSearchDto, pageable);
		PostReplyStatus status = PostReplyStatus.Y;
		
		PostAnswerDto postAnswerDto = new PostAnswerDto(); 
		
		model.addAttribute("postAnswerDto", postAnswerDto);
		model.addAttribute("posts", posts);
		model.addAttribute("maxPage", 5);
		model.addAttribute("status", status);

		return "post/qaList";
	}

	// 문의사항 수정 페이지 보기
	@GetMapping(value = "/post/qaModify/{postId}")
	public String qaModifyForm(@PathVariable("postId") Long postId, Model model) {

		try {
			PostDto postDto = postService.getQaModify(postId);
			model.addAttribute("postDto", postDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "정보를 가져올때 에러가 발생했습니다.");
			model.addAttribute("postDto", new PostDto());
			return "post/qaList";
		}
		return "post/qaModify";
	}
	
	

}
