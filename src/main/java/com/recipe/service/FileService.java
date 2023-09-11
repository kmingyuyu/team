package com.recipe.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class FileService {
	
	//회원가입용 프로필 이미지 등록 메소드
	public String profileImgFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
		UUID uuid = UUID.randomUUID(); //중복되지 않은 이름을 만든다
		
		// 이미지1.jpg -> 이미지의 확장자 명을 구한다..
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 파일이름 생성 -> ERSFHG4FDGD454.jpg
		String savedProfileFileName = uuid.toString() + extension; 
		
		String fileUploadFullUrl = uploadPath + "/" + savedProfileFileName;
		
		//파일업로드
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		
		return savedProfileFileName;
	}
	
	
	//레시피 등록 이미지 처리용 메소드
	public String uploadFile(String uploadPath, String originalFileName,
			byte[] fileData) throws Exception {
		UUID uuid = UUID.randomUUID(); //중복되지 않은 이름을 만든다
		
		// 이미지1.jpg -> 이미지의 확장자 명을 구한다..
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 파일이름 생성 -> ERSFHG4FDGD454.jpg
		String savedFileName = uuid.toString() + extension; 
		
		// C:/shop/item/ERSFHG4FDGD454.jpg
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		
		//파일업로드
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		
		return savedFileName;
	}
	
	public void deleteProfileFile(String filePath) throws Exception {
		// filePath -> C://shop/item/ASDFA42FA3F.jpg
		File deleteFile = new File(filePath);
		
		if(deleteFile.exists()) { //해당경로에 파일이 있으면
			deleteFile.delete();
			log.info("파일을 삭제하였습니다"); //로그 기록을 저장
		} else {
			log.info("파일이 존재하지 않습니다");
		}
	}
}