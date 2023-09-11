package com.recipe.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recipe.entity.Member;
import com.recipe.repository.MemberRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RamdomPassword {
	private final MemberRepository memberRepository;
	private static final String senderEmail= "iptime102030@gmail.com"; //발신자메일
    private static final String senderName = "이젠 해먹자"; //이젠해먹자 <iptime102030@gmail.com>
	
	//임시비밀번호 생성
	public String getRamdomPassword(int size) {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&' };

		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		sr.setSeed(new Date().getTime());

		int idx = 0;
		int len = charSet.length;
		for (int i = 0; i < size; i++) {
			// idx = (int) (len * Math.random());
			idx = sr.nextInt(len); // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
			sb.append(charSet[idx]);
		}

		return sb.toString();
	}
	
	//임시 비밀번호로 비번 업데이트
	public String updatePassword(String pass, String email, PasswordEncoder passwordEncoder) {
		Member member = memberRepository.findByEmail(email);

		String password = member.updatePassword(pass, passwordEncoder);

		if (member == null || member.getEmail() == "") {
			return "일치하는 사용자가 없습니다";
		}

		// Update the password for the member
		member.setPassword(passwordEncoder.encode(pass));

		// Encode the new password
		memberRepository.save(member); // Save the updated member to the database

		return "비밀번호가 업데이트되었습니다";
	}

	
	//이메일 찾기
	public String findMember(String email) {

		Member member = memberRepository.findByEmail(email);

		 if (member == null) {
		        return null;
		    }
		
		return member.getEmail();
	}


	private final JavaMailSender javaMailSender;
	
	//임시비밀번호 메일전송
	public void sendEmail(String to, String subject, String text) {
		 MimeMessage message = javaMailSender.createMimeMessage();
		
		 try {
		        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
		        helper.setFrom(new InternetAddress(senderEmail, senderName));
		        helper.setTo(to);
		        helper.setSubject(subject);
		        helper.setText(text, true); // 'true' indicates that the text is HTML
		        javaMailSender.send(message);
		    } catch (MessagingException | UnsupportedEncodingException e) {
		        e.printStackTrace();
		    }
	}
	
}