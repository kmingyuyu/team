package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.PostResponse;

public interface PostResponseRepository extends JpaRepository<PostResponse, Long>, PostResponseRepositoryCustom {

}
