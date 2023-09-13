package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.PostImg;

public interface PostImgRepository extends JpaRepository<PostImg, Long> {
	// select * from att_file where post_Id = ? order by post_Id
	List<PostImg> findByPostIdOrderByIdAsc(Long postId);

	// select * from att_file where post_Id = ? and repimg_yn = ?
	PostImg findByPostIdAndRepimgYn(Long postId, String repimgYn);
}
