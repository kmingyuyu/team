package com.recipe.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.recipe.entity.Member;
import com.recipe.repository.MemberRepository;
import com.recipe.service.CountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final MemberRepository memberRepository;

	private final CountService countService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		HttpSession session = request.getSession();

		if (session != null) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof org.springframework.security.core.userdetails.User) {
				String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();

				Member member = memberRepository.findByEmail(username);

				if (member != null) {

					session.setAttribute("memberId", member.getId());
					session.setAttribute("email", member.getEmail());
					session.setAttribute("role", member.getRole().toString());
					session.setAttribute("imgUrl", member.getImgUrl());
					session.setAttribute("introduce", member.getIntroduce());
					session.setAttribute("nickname", member.getNickname());
					session.setAttribute("point", member.getPoint());
					session.setAttribute("regTime", member.getRegTime());

					session.setAttribute("cartCount", countService.cartCount(member.getId()));


				}
			}
		}

		response.sendRedirect("/");
	}

}
