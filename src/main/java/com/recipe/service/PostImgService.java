package com.recipe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.recipe.entity.PostImg;
import com.recipe.repository.PostImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostImgService {

	private String postImgLocation = "C:/recipe/post";
	private final PostImgRepository postImgRepository;
	private final FileService fileService;

	// 이미지 저장 1.파일을 postImgLocation에 저장 2.item_img 테이블에 저장

	public void savePostImg(PostImg attFile, MultipartFile postImgFile) throws Exception {
		String oriImgName = postImgFile.getOriginalFilename(); // 파일이름 -> 이미지1.jpg
		String imgName = "";
		String imgUrl = "";

		// 1.파일을 itemImgLocation에 저장
		if (!StringUtils.isEmpty(oriImgName)) {
			// oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(postImgLocation, oriImgName, postImgFile.getBytes());
			imgUrl = "/images/post/" + imgName;

		}

		// 2.item_img 테이블에 저장

		// entity값을 update
		attFile.updateAttFile(imgName, oriImgName, imgUrl);
		postImgRepository.save(attFile); // db에 insert
	}

	// 이미지 업데이트 메소드
	public void updatePostImg(Long postImgId, MultipartFile postImgFile) throws Exception {
		if (!postImgFile.isEmpty()) { // 첨부한 이미지 파일이 있으면

			// 해당 이미지를 가져오고
			PostImg savedAttFile = postImgRepository.findById(postImgId).orElseThrow(EntityNotFoundException::new);

			// 기존 이미지 파일 C:/shop/item 폴더에서 삭제
			if (!StringUtils.isEmpty(savedAttFile.getImgName())) {
				fileService.deleteProfileFile(postImgLocation + "/" + savedAttFile.getImgName());
			}

			// 수정된 이미지 파일 업로드 C:/shop/item에 업로드
			String oriImgName = postImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(postImgLocation, oriImgName, postImgFile.getBytes());
			String imgUrl = "/images/post/" + imgName;

			// update쿼리문 실행
			/*
			 * 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 그 이후에는 변경감지 기능이 동작하기 때문에 개발자는
			 * update쿼리문을 쓰지 않고 엔티티 데이터만 변경해주면 된다.
			 */
			savedAttFile.updateAttFile(imgName, oriImgName, imgUrl);

		}
	}
}
