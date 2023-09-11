package com.recipe.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.recipe.dto.MemberDto;
import com.recipe.dto.SocialMemberDto;
import com.recipe.entity.Member;
import com.recipe.service.FileService;
import com.recipe.service.MemberService;
import com.recipe.service.RamdomPassword;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final RamdomPassword randomPassword; 
	private final FileService fileService;
	private String profileImgLocation = "C:/recipe/profile";
	
	// 로그인 화면
	@GetMapping(value = "/members/login")
	public String loginMember() {
		return "member/loginForm";
	}

	// 약관 동의
	@GetMapping(value = "/members/agree")
	public String agreeMember() {
		return "member/agreeForm";
	}

	//일반 회원가입 화면
	@GetMapping(value = "/members/newMember")
	public String newMemberForm(Model model) {
		model.addAttribute("memberDto", new MemberDto());
		return "member/newMemberForm";
	}
	
	//sns 회원가입 화면
	@GetMapping(value = "/members/snsMember")
	public String snsMemberForm(@RequestParam("email") String email, @RequestParam("provider") String provider, 
			@RequestParam("providerId") String providerId, 
			@RequestParam("name") String name,
			Model model ) {
		SocialMemberDto socialMemberDto = new SocialMemberDto();
		
				
		socialMemberDto.setEmail(email);
		socialMemberDto.setProvider(provider);
		socialMemberDto.setProviderId(providerId);
		socialMemberDto.setName(name);
	    
		model.addAttribute("socialMemberDto", socialMemberDto);
		
		return "member/snsMemberForm";
	}
	
	
	//일반 회원가입 기능
	@PostMapping(value = "/members/newMember")
	public String newMemberForm(@RequestParam("profileImgFile") MultipartFile profileImgFile, @Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
		
		//회원가입 에러처리
		if (!memberDto.getPassword().equals(memberDto.getPasswordConfirm()) || memberDto.getEmailConfirm2().isEmpty()) {
			return "member/newMemberForm";
		}
		
		//자기소개 기본값 넣기
		if (memberDto.getIntroduce() == null || memberDto.getIntroduce().isEmpty()) {
			memberDto.setIntroduce("자기소개가 없습니다.");
		}
		
		//에러처리
		if (bindingResult.hasErrors()) {
			return "member/newMemberForm";
		}
		
		String imgName = profileImgFile.getOriginalFilename();
		String imgUrl = "";
		
		 // 프로필 사진처리
	    if (!StringUtils.isEmpty(imgName)) {
	        try {
	            // 프로필 이미지 파일을 저장
	            String savedImgName = fileService.profileImgFile(profileImgLocation, imgName, profileImgFile.getBytes());
	            imgUrl = "/img/profile/" + savedImgName;
	            
	            memberDto.setOriImgName(imgName);
	            memberDto.setImgName(savedImgName);
	            memberDto.setImgUrl(imgUrl);

	            // 멤버생성 DB에저장
	            Member member = Member.createMember(memberDto, passwordEncoder);
	            memberService.saveMember(member);

	        } catch (Exception e) {
	            e.printStackTrace();
	         // 회원가입 실패시
				model.addAttribute("errorMessage", e.getMessage());
				return "member/newMemberForm";
	        }
			
		} else {
			
			// imgName이 비어있거나 null일 때 처리 고양이 이미지 추가
		    imgName = "이젠해먹자.jpg"; 
		    imgUrl = "/img/profile/" + imgName;
		    
		    memberDto.setImgName(imgName);
		    memberDto.setImgUrl(imgUrl);
		    
		    // 멤버생성 DB에저장
		    Member member = Member.createMember(memberDto, passwordEncoder);
		    memberService.saveMember(member);
		}
		
		// 회원가입 성공시
		return "redirect:/";
	}
	
	
	//sns 회원가입 기능
	@PostMapping(value = "/members/snsMember")
	public String snsMemberForm(@RequestParam("snsProfileImgFile") MultipartFile snsProfileImgFile ,@Valid SocialMemberDto socialMemberDto, BindingResult bindingResult, Model model) {
		
		//자기소개 기본값 넣기
		if (socialMemberDto.getIntroduce() == null || socialMemberDto.getIntroduce().isEmpty()) {
			socialMemberDto.setIntroduce("자기소개가 없습니다.");
		}
		
		//에러처리
		if (bindingResult.hasErrors()) {
			return "member/snsMemberForm";
		}
				
		String imgName = snsProfileImgFile.getOriginalFilename();
		String imgUrl = "";
		
		//프로필 사진처리
		if(!StringUtils.isEmpty(imgName)) {
			try {
	            // 프로필 이미지 파일을 저장
	            String savedImgName = fileService.profileImgFile(profileImgLocation, imgName, snsProfileImgFile.getBytes());
	            imgUrl = "/img/profile/" + savedImgName;
	            
	            socialMemberDto.setOriImgName(imgName);
	            socialMemberDto.setImgName(savedImgName);
	            socialMemberDto.setImgUrl(imgUrl);

	            // 멤버생성 DB에저장
	            Member member = Member.createSnsMember(socialMemberDto);
	            memberService.saveMember(member);

	        } catch (Exception e) {
	            e.printStackTrace();
	         // 회원가입 실패시
				model.addAttribute("errorMessage", e.getMessage());
				return "member/newMemberForm";
	        }
			
		} else {
			
			// oriImgName이 비어있거나 null일 때 처리 고양이 이미지 추가
			imgName = "이젠해먹자.jpg"; 
		    imgUrl = "/img/profile/" + imgName;
		    
		    socialMemberDto.setImgName(imgName);
		    socialMemberDto.setImgUrl(imgUrl);
		    
		    // 멤버생성 DB에저장
		    Member member = Member.createSnsMember(socialMemberDto);
		    memberService.saveMember(member);
		}
		
		// 회원가입 성공시
		return "redirect:/";
	}

	// 로그인 실패
	@GetMapping(value = "/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "로그인 실패 아이디 또는 비밀번호를 확인해주세요");
		return "member/loginForm";
	}

	// 아이디/비밀번호찾기 페이지
	@GetMapping(value = "/members/findIDPW")
	public String findIDPW() {
		return "member/findIDPW";
	}
	
	// 비밀번호 찾고 난수생성기로 랜덤비밀번호 생성 , 아이디/비밀번호 찾기
	@PostMapping(value = "/findPw")
	@ResponseBody
	public HashMap<String, String> memberps(@RequestBody Map<String, Object> psdata, Principal principal) {
		String email = (String) psdata.get("email");
		HashMap<String, String> msg = new HashMap<>();
		
		String memberEmail = randomPassword.findMember(email);
		
		// email이 DB에 없는 경우
	    if (memberEmail == null) {
	        msg.put("error", "입력하신 이메일 주소가 존재하지 않습니다.");
	        return msg;
	    }
		
		String ramdomps = randomPassword.getRamdomPassword(12); // 임시 비밀번호 12자리생성
		System.out.println("ramdomps===" + ramdomps);
		
		//임시 비밀번호를 DB에 저장해서 바꿈 => 임시 비밀번호로 저장해야 로그인성공
		String password = randomPassword.updatePassword(ramdomps, email, passwordEncoder);
		
		String emailBody = 
				"<h3>요청하신 임시 비밀번호입니다.</h3>" + 
                "<h1>" + ramdomps + "</h1>" + 
                "<h3>감사합니다.</h3>";
		
		randomPassword.sendEmail(email, "임시 비밀번호", emailBody);
		
		String asd1 = "이메일로 임시 비밀번호가 발송되었습니다.";
		String asd2 = "임시 비밀번호를 사용해 로그인 해주십시오.";
		msg.put("message1", asd1);
		msg.put("message2", asd2);
		return msg;
	}
	
	//이메일 찾기
	@PostMapping(value = "/findEmail")
	public ResponseEntity<Map<String, String>> findEmail(@RequestBody MemberDto memberDto) {
		
		String memberEmail = memberService.findEmail(memberDto.getPhoneNumber());
		
		Map<String, String> response = new HashMap<>();
		response.put("memberEmail", memberEmail);
		
		return ResponseEntity.ok(response);
		
	}
	
}