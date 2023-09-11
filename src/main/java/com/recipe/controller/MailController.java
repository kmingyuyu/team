package com.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.dto.EmailDto;
import com.recipe.service.MailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailController {
	
	  private final MailService mailService;

	    @ResponseBody
	    @PostMapping("/email") // /mail로 들어오면 post방식으로 메일을 전송
	    public String MailSend(@RequestBody EmailDto emailDto  ,Model model){

	        int number = mailService.sendMail(emailDto.getEmail());
	        
	        //인증번호
	        String num = "" + number;
	        System.out.println(num);
	        
	        return num;
	    }
}