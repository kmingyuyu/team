package com.recipe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.dto.CommentDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.entity.Comment;
import com.recipe.repository.CommentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // @Autowired를 사용하지 않고 필드의 의존성 주입을 시켜준다
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public Page<CommentDto> getAdminCommentPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		Page<CommentDto> commentPage = commentRepository.getAdminCommentPage(mngRecipeSearchDto, pageable);
		return commentPage;

	}

	// 회원 삭제
	public void deleteComment(Long commentId) {

		// ★delete하기 전에 select를 한번 해준다
		// ->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

		// delete
		commentRepository.delete(comment);
	}

}
