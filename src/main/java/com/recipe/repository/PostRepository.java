package com.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

	Optional<Post> findById(Long id);

}
