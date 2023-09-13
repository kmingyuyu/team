package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recipe.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query(value ="select * from comment where member_id = ?1" , nativeQuery = true)
	List<Comment> getMyComment(Long id);
	
}