package com.recipe.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


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
	
	public String uploadFileImg(String uploadPath, String originalFileName,byte[] fileData) throws IOException {
		
//		해당경로에 폴더여부 확인 / 없으면 폴더생성
		Path folder = Paths.get(uploadPath);
		if (!Files.exists(folder)) {
			Files.createDirectories(folder);
		}
		
		UUID uuid = UUID.randomUUID();
		
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		String savedFileName = uuid.toString() + extension; 
		
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		
		return savedFileName;
	}
	
	
//	해당경로의 폴더안에 이미지/폴더 삭제
	public void deleteFolder(String path, String highPath) throws IOException {
	    Path highFolderPath = Paths.get(highPath);

	    // highPath의 하위 폴더 개수를 체크
	    long subDirCount = Files.list(highFolderPath).filter(Files::isDirectory).count();
	    
	    if (subDirCount == 1) {
	        // 하위 폴더가 1개인 경우 highPath 경로의 폴더와 그 안의 모든 내용 삭제
	        deleteDirectoryAndContents(highFolderPath);
	    } else {
	        // 그렇지 않으면 path 경로의 폴더와 그 안의 이미지만 삭제
	        Path folderPath = Paths.get(path);
	        if (Files.exists(folderPath)) {
	            deleteDirectoryAndContents(folderPath);
	        }
	    }
	}

	private void deleteDirectoryAndContents(Path directoryPath) throws IOException {
	    if (Files.exists(directoryPath)) {
	        Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
	            @Override
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                Files.delete(file);
	                return FileVisitResult.CONTINUE;
	            }
	            
	            @Override
	            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
	                Files.delete(dir);
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
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