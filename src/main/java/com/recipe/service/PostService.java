package com.recipe.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.PostDto;
import com.recipe.dto.PostImgDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.entity.PostImg;
import com.recipe.entity.PostResponse;
import com.recipe.entity.Post;
import com.recipe.repository.PostImgRepository;
import com.recipe.repository.PostRepository;
import com.recipe.repository.PostResponseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final PostImgRepository postImgRepository;
	private final PostImgService postImgService;
	private final PostResponseRepository postResponseRepository;

	// post 테이블에 게시글 등록(insert)
	public Long savePost(PostDto postDto, List<MultipartFile> postImgFileList) throws Exception {

		// 1. 상품 등록
		Post post = postDto.createPost(); // dto -> entity
		postRepository.save(post); // insert(저장)

		// 2. 이미지 등록
		for (int i = 0; i < postImgFileList.size(); i++) {
			// ★fk키를 사용시 부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			PostImg attFile = new PostImg();
			attFile.setPost(post);

			postImgService.savePostImg(attFile, postImgFileList.get(i));

		}

		return post.getId(); // 등록한 상품 id를 리턴
	}

	// 상품 가져오기
	@Transactional(readOnly = true) // 트랙잰션 읽기 전용(변경감지 수해하지 않음) ->성능 향상
	public PostDto getQaModify(Long postId) {
		// 1.post_img 테이블의 이미지를 가져온다.
		List<PostImg> postImgList = postImgRepository.findByPostIdOrderByIdAsc(postId);

		// PostImg 엔티티 객체 -> PostImgDto로 변환
		List<PostImgDto> postImgDtoList = new ArrayList<>();
		for (PostImg postImg : postImgList) {
			PostImgDto postImgDto = PostImgDto.of(postImg);
			postImgDtoList.add(postImgDto);
		}

		// 2. post테이블에 있는데이터를 가져온다.
		Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

		// Post 엔티티 객체 -> dto로 변환
		PostDto postDto = PostDto.of(post);

		// 3.PostDto에 이미지 정보(postImgDtoList)를 넣어준다.
		postDto.setPostImgDtoList(postImgDtoList);
		return postDto;

	}

	@Transactional(readOnly = true)
	public Page<PostDto> getAdminPostListPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		Page<PostDto> postPage = postRepository.getAdminPostListPage(mngRecipeSearchDto, pageable);
		return postPage;
	}

	@Transactional(readOnly = true)
	public Page<PostDto> getPostListPage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		Page<PostDto> postPage = postRepository.getPostListPage(mngRecipeSearchDto, pageable);
		return postPage;
	}

	// postResponse 테이블에 문의답변 등록(insert)
	public void savePostResponse(Map<String, Object> requestBody) throws Exception {
		LocalDateTime regTime = LocalDateTime.now();
		Long id = Long.parseLong(requestBody.get("id").toString());
		String content = requestBody.get("content").toString();
System.out.println(id);
		// 1. 답변 등록
		PostResponse postResponse = new PostResponse(); // dto -> entity
		Post post = postRepository.findById(id).orElseThrow();
		postResponse.setRegTime(regTime);
		postResponse.setPost(post);
		postResponse.setContent(content);
		postResponseRepository.save(postResponse); // insert(저장)

	}
}
