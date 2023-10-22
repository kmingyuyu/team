package com.recipe.myPage.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.entity.Member;
import com.recipe.exception.CustomException;
import com.recipe.exception.FindNotException;
import com.recipe.myPage.dto.MyPageMemberModiDto;
import com.recipe.service.MyPageMemberInfoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageMemberInfoController {
	
	private final MyPageMemberInfoService myPageMemberInfoService;
	
// 	마이페이지 보여주기 (회원 정보 수정)
	@GetMapping("/myPage/memberInfo" )
	public String myPageMemberModi(HttpSession session, Model model) {
		
		Long memberId = (Long) session.getAttribute("memberId");
		
		try {
			Member member = myPageMemberInfoService.findByMember(memberId);
			
			
			model.addAttribute("member",member);
			
		} catch (CustomException e) {
			model.addAttribute("errorMessage","회원정보를 찾을 수 없습니다.");
		}
		
		return "myPage/MemberMng/myMemberInfo";
	}
	
	
	@PatchMapping("/myPage/memberInfo/nicknameModi")
	public @ResponseBody ResponseEntity<String> nicknameModi(@Valid @RequestBody MyPageMemberModiDto myPageMemberModiDto , HttpSession session){
		
		try {
			myPageMemberInfoService.nicknameModi(session, myPageMemberModiDto);
			
		} catch (FindNotException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		} catch (CustomException e) {
			log.error("닉네임 변경 에러",e);
			return new ResponseEntity<>("닉네임 변경에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>("닉네임 변경 되었습니다. ", HttpStatus.OK);
	}
	
	
	@PatchMapping("/myPage/memberInfo/passwordModi")
	public @ResponseBody ResponseEntity<String> passwordModi(@Valid @RequestBody MyPageMemberModiDto myPageMemberModiDto , HttpSession session){
		
		try {
			myPageMemberInfoService.passwordModi(session, myPageMemberModiDto);
			
		} catch (FindNotException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		} catch (CustomException e) {
			log.error("비밀번호 변경 에러",e);
			return new ResponseEntity<>("비밀번호 변경에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>("비밀번호 변경 되었습니다. ", HttpStatus.OK);
	}
	
	
	@PatchMapping("/myPage/memberInfo/phoneNumberModi")
	public @ResponseBody ResponseEntity<String> phoneNumberModi(@Valid @RequestBody MyPageMemberModiDto myPageMemberModiDto , HttpSession session){
		
		try {
			myPageMemberInfoService.phoneNumberModi(session, myPageMemberModiDto);
			
		} catch (FindNotException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		} catch (CustomException e) {
			log.error("휴대전화 변경 에러",e);
			return new ResponseEntity<>("휴대전화 정보 변경에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		return new ResponseEntity<>("휴대전화 정보 변경 되었습니다. ", HttpStatus.OK);
		
	}
	
	
	@PatchMapping("/myPage/memberInfo/introduceModi")
	public @ResponseBody ResponseEntity<String> introduceModi(@Valid @RequestBody MyPageMemberModiDto myPageMemberModiDto , HttpSession session){
		
		try {
			String comment = myPageMemberInfoService.introduceModi(session, myPageMemberModiDto);
			
			return new ResponseEntity<>("자기소개 " + comment + " 되었습니다.", HttpStatus.OK);
		} catch (FindNotException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		} catch (CustomException e) {
			log.error("자기소개 변경 에러",e);
			return new ResponseEntity<>("자기소개 수정에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PatchMapping("/myPage/memberInfo/nameModi")
	public @ResponseBody ResponseEntity<String> nameModi(@Valid @RequestBody MyPageMemberModiDto myPageMemberModiDto , HttpSession session){
		
		try {
			String comment = myPageMemberInfoService.nameModi(session, myPageMemberModiDto);
			
			return new ResponseEntity<>("성함 정보가 " + comment + " 되었습니다.", HttpStatus.OK);
		} catch (FindNotException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		} catch (CustomException e) {
			log.error("성함 변경 에러",e);
			return new ResponseEntity<>("성함 정보 수정에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PatchMapping("/myPage/memberInfo/addressModi")
	public @ResponseBody ResponseEntity<String> addressModi(@Valid @RequestBody MyPageMemberModiDto myPageMemberModiDto , HttpSession session){
		
		try {
			String comment = myPageMemberInfoService.addressModi(session, myPageMemberModiDto);
			
			return new ResponseEntity<>("주소 정보가 " + comment + " 되었습니다.", HttpStatus.OK);
		} catch (FindNotException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			
		} catch (CustomException e) {
			log.error("주소 변경 에러",e);
			return new ResponseEntity<>("주소 수정에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PatchMapping("/myPage/memberInfo/imgModi")
	public @ResponseBody ResponseEntity<String> imgModi(@RequestParam(value="imgFile") MultipartFile imgFile , HttpSession session){
		
		
		try {
			myPageMemberInfoService.imgModi(imgFile, session);
			
		} catch (Exception e) {
			log.error("이미지 변경 에러",e);
			return new ResponseEntity<>("이미지 변경에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
		return new ResponseEntity<>("이미지 변경 되었습니다.", HttpStatus.OK);
	}
	
	@PatchMapping("/myPage/memberInfo/memberInfoDelete")
	public @ResponseBody ResponseEntity<String> memberInfoDelete(@RequestBody Map<String, Object> requestBody , HttpSession session){
		
		try {
			
			String comment = myPageMemberInfoService.memberInfoDelete(requestBody, session);
			
			return new ResponseEntity<>(comment + " 정보가 수정 되었습니다. ", HttpStatus.OK);
			
		} catch (Exception e) {
			
			log.error("회원정보 삭제 에러",e);
			return new ResponseEntity<>("회원정보 수정에 실패하였습니다. 잠시후에 시도해주세요.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    List<String> errors = new ArrayList<>();
	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	        errors.add(error.getDefaultMessage());
	    }

	    String combinedErrorMessage = String.join("\n", errors);
	    String finalErrorMessage = "요청에 실패하였습니다. \n사유:\n" + combinedErrorMessage;

	    return ResponseEntity.badRequest().body(finalErrorMessage);
	}
	
}
