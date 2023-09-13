package com.recipe.service;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.Tuple;
import com.recipe.dto.MngRecipeDto;
import com.recipe.dto.MngRecipeSearchDto;
import com.recipe.dto.RecipeCategoryDto;
import com.recipe.dto.RecipeIngreDto;
import com.recipe.dto.RecipeMainDto;
import com.recipe.dto.RecipeNewDto;
import com.recipe.dto.RecipeOrderDto;
import com.recipe.dto.RecipeSearchDto;
import com.recipe.entity.Member;
import com.recipe.entity.Recipe;
import com.recipe.entity.RecipeIngre;
import com.recipe.entity.RecipeOrder;
import com.recipe.repository.CommentRepository;
import com.recipe.repository.MemberRepository;
import com.recipe.repository.RecipeIngreRepository;
import com.recipe.repository.RecipeListRepository;
import com.recipe.repository.RecipeOrderRepository;
import com.recipe.repository.RecipeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
	
	private final RecipeListRepository recipeListRepository;
	
	private final CommentRepository commentRepository;
	
	private final RecipeRepository recipeRepository;
	
	private final RecipeIngreRepository recipeIngreRepository;
	
	private final RecipeOrderRepository recipeOrderRepository;
	
	private final MemberRepository memberRepository;
	
	private final FileService fileService;
	
private String recipeImgLocation = "C:/recipe/memberRecipe";
	
	//레시피 저장 메소드
	public Long saveRecipe(RecipeNewDto recipeNewDto, MultipartFile recipeImgFile,
			List<String> recipeingreMaterialList , List<String>recipeingreNameList,
			List<String>recipeOrderContentList, List<MultipartFile>recipeOrderImgFile, Principal principal) throws Exception {
		//로그인된 사용자의 이메일 가져옴
		String email = principal.getName();
		Member member;
		
		if(email.contains("@")) {
			
			member = memberRepository.findByEmail(email);
		} else {
		    member = memberRepository.findByname(email);
		}
		
		Recipe recipe = recipeNewDto.createRecipe();
		recipe.setMember(member);
		recipeRepository.save(recipe);
		
		String imgName = recipeImgFile.getOriginalFilename();
		String imgUrl = "";
		
		//등록할때 레시피 이미지 저장
		if(!StringUtils.isEmpty(imgName)) {
			imgName = fileService.uploadFile(recipeImgLocation, 
					imgName, recipeImgFile.getBytes());
			imgUrl = "/img/memberRecipe/" + imgName;
			
			recipe.updateRecipeImg(imgUrl, imgName);
			recipeRepository.save(recipe);
		}
			
		//레시피등록할때 넘긴 재료정보 가져오기 => 등록
		for(int i=0; i<recipeingreMaterialList.size(); i++) {
			
			RecipeIngre recipeIngre = new RecipeIngre();
			
			recipeIngre.setRecipe(recipe);
			recipeIngre.setIngreMaterial(recipeingreMaterialList.get(i));
			recipeIngre.setIngreName(recipeingreNameList.get(i));
			recipeIngreRepository.save(recipeIngre);
		}
			//레시피 조리순서 가져오기
		for(int i=0; i<recipeOrderContentList.size(); i++) {
				RecipeOrder recipeOrder = new RecipeOrder();
				
				recipeOrder.setRecipe(recipe);
				recipeOrder.setContent(recipeOrderContentList.get(i));
				recipeOrder.setOrderNumber(i+1);
				
				String imgName2 = recipeOrderImgFile.get(i).getOriginalFilename();
				String imgUrl2 = "";
				
				//조리순서에 포함된 이미지 가져오기
				if(!StringUtils.isEmpty(imgName2)) {
					imgName2 = fileService.uploadFile(recipeImgLocation, imgName2, recipeOrderImgFile.get(i).getBytes());
					imgUrl2 = "/img/memberRecipe/" + imgName2;
					
					recipeOrder.updateRecipeOrderImg(imgUrl2, imgName2);
					recipeOrderRepository.save(recipeOrder);
				}
				
			}
				
				return recipe.getId();
		}
	
	//레시피 수정을 위해 정보 가져오기
	@Transactional(readOnly = true)
	public RecipeNewDto getRecipeDtl(Long recipeId) {
		
		List<RecipeIngre> recipeIngreList = recipeIngreRepository.findByRecipeId(recipeId);
		
		List<RecipeOrder> recipeOrderList = recipeOrderRepository.findByRecipeId(recipeId);
		
		List<RecipeIngreDto> recipeIngreDtoList = new ArrayList<>();
		
		List<RecipeOrderDto> recipeOrderDtoList = new ArrayList<>();
		
		//반복문으로 재료정보 담기
		for(RecipeIngre recipeIngre : recipeIngreList) {
			RecipeIngreDto recipeIngreDto = RecipeIngreDto.of(recipeIngre);
			
			recipeIngreDtoList.add(recipeIngreDto);
		}
		
		//반복문으로 레시피 조리설명이랑이미지 담기
		for(RecipeOrder recipeOrder : recipeOrderList) {
			RecipeOrderDto recipeOrderDto = RecipeOrderDto.of(recipeOrder);
			
			recipeOrderDtoList.add(recipeOrderDto);
		}
		
		Recipe recipe = recipeRepository.findById(recipeId)
								.orElseThrow(EntityNotFoundException::new);
		
		RecipeNewDto recipeNewDto = RecipeNewDto.of(recipe);
		
		recipeNewDto.setRecipeIngreDtoList(recipeIngreDtoList);
		
		recipeNewDto.setRecipeOrderDtoList(recipeOrderDtoList);
		
		return recipeNewDto;
	}
	
	//레시피 수정기능
	public Long updateRecipe(RecipeNewDto recipeNewDto, MultipartFile recipeImgFile,
			List<String> recipeingreMaterialList , List<String>recipeingreNameList,
			List<String>recipeOrderContentList, List<MultipartFile>recipeOrderImgFile ) {
		
		Recipe recipe = recipeRepository.findById(recipeNewDto.getId())
									.orElseThrow(EntityNotFoundException::new);
		
		
		recipe.updateRecipe(recipeNewDto);
		
	    // 메인이미지 업로드 및 레시피 정보 갱신
	    updateRecipeImage(recipe, recipeImgFile);
	    
	    // 재료 정보 업데이트
	    updateRecipeIngredients(recipe, recipeingreMaterialList, recipeingreNameList);
	    
	    // 조리 순서 업데이트
	    updateRecipeOrders(recipe, recipeOrderContentList, recipeOrderImgFile);
		
		return recipe.getId();
		
	}
	
	//레시피 등록 메인 이미지 업데이트
	public void updateRecipeImage(Recipe recipe , MultipartFile recipeImgFile) {
		String imgName = recipeImgFile.getOriginalFilename();
		String imgUrl = "";
		
		if(!StringUtils.isEmpty(imgName)) {
			try {
				imgName = fileService.uploadFile(recipeImgLocation, imgName, recipeImgFile.getBytes());
				imgUrl = "/img/memberRecipe/" + imgName;
				recipe.updateRecipeImg(imgUrl, imgName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//재료 정보 업데이트
	public void updateRecipeIngredients(Recipe recipe, List<String> recipeingreMaterialList,List<String> recipeingreNameList) {
		List<RecipeIngre> recipeIngredients = recipeIngreRepository.findByRecipeId(recipe.getId());
	    // 기존 재료 삭제
	    recipeIngreRepository.deleteAll(recipeIngredients);
	    
	    // 새로운 재료 등록
	    for (int i = 0; i < recipeingreMaterialList.size(); i++) {
	        RecipeIngre recipeIngre = new RecipeIngre();
	        recipeIngre.setRecipe(recipe);
	        recipeIngre.setIngreMaterial(recipeingreMaterialList.get(i));
	        recipeIngre.setIngreName(recipeingreNameList.get(i));
	        
	        recipeIngreRepository.save(recipeIngre);
	    }
	}
	
	
	// 조리 순서 업데이트
	private void updateRecipeOrders(Recipe recipe, List<String> recipeOrderContentList, List<MultipartFile> recipeOrderImgFile) {
	    List<RecipeOrder> recipeOrders = recipeOrderRepository.findByRecipeId(recipe.getId());
	    // 기존 조리 순서 삭제
	    recipeOrderRepository.deleteAll(recipeOrders);
	    
	    // 새로운 조리 순서 등록
	    for (int i = 0; i < recipeOrderContentList.size(); i++) {
	        RecipeOrder recipeOrder = new RecipeOrder();
	        recipeOrder.setRecipe(recipe);
	        recipeOrder.setContent(recipeOrderContentList.get(i));
	        recipeOrder.setOrderNumber(i + 1);

	        String imgName = recipeOrderImgFile.get(i).getOriginalFilename();
	        if (!StringUtils.isEmpty(imgName)) {
	            try {
	                imgName = fileService.uploadFile(recipeImgLocation, imgName, recipeOrderImgFile.get(i).getBytes());
	                String imgUrl = "/img/memberRecipe/" + imgName;
	                
	                recipeOrder.updateRecipeOrderImg(imgUrl, imgName);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	        recipeOrderRepository.save(recipeOrder);
	    }
	}
	
	
	@Transactional(readOnly = true)
	public List<RecipeMainDto> getRecipeNewList() {
		List<RecipeMainDto> getRecipeNewList = recipeRepository.getRecipeNewList();
		return getRecipeNewList;
	}
	
	@Transactional(readOnly = true)
	public List<RecipeMainDto> getRecipeHeaderBestList() {
		List<RecipeMainDto> getRecipeHeaderBestList = recipeRepository.getRecipeHeaderBestList();
		return getRecipeHeaderBestList; 
	}
	
	@Transactional(readOnly = true)
	public List<RecipeMainDto> getRecipeBestList() {
		List<RecipeMainDto> getRecipeBestList = recipeRepository.getRecipeBestList();
		return getRecipeBestList; 
	}
	
	
	@Transactional(readOnly = true)
	public Page<RecipeCategoryDto> getRecipeCategoryReviewBestList(Pageable pageable , RecipeSearchDto recipeSearchDto) {
		Page<RecipeCategoryDto> getRecipeCategoryReviewBestList = recipeRepository.getRecipeCategoryReviewBestList(pageable, recipeSearchDto);
	    return getRecipeCategoryReviewBestList;
	}
	

	@Transactional(readOnly = true)
	public Page<MngRecipeDto> getAdminRecipePage(MngRecipeSearchDto mngRecipeSearchDto, Pageable pageable) {
		Page<MngRecipeDto> getAdminRecipePage = recipeListRepository.getAdminRecipePage(mngRecipeSearchDto, pageable);
		return getAdminRecipePage;

	}
	
	// 레시피 삭제
		public void deleteRecipe(Long recipeId) {

			commentRepository.deleteByRecipeId(recipeId);
			// ★delete하기 전에 select를 한번 해준다
			// ->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
			Recipe recipe = recipeListRepository.findById(recipeId).orElseThrow(EntityNotFoundException::new);

			// delete
			recipeListRepository.delete(recipe);
		}
	
}
