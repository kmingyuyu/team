package com.recipe.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.service.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@GetMapping(value="/cart/{id}")
	public String Cart(@PathVariable("id") Long id , Model model) {
		
		
		
		
		return "cart";
		
	}
	
	
//	카트 상품담기
	@PostMapping(value="/cartReq")
	public @ResponseBody ResponseEntity inqReq(@RequestBody Map<String, Object> requestBody
			, HttpSession session) {
		
		if(!isAuthenticated()) {
		   return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	
		String cartOk = orderService.cartReg(requestBody,session);
		
		return new ResponseEntity<>(cartOk , HttpStatus.OK);
	}
	
	
//	로그인/비로그인 여부 로그인상태면 true
	private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }
	
	
	
	
}
