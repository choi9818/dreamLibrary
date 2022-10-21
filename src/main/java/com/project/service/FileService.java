package com.project.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class FileService {
	
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
		UUID uuid = UUID.randomUUID();	// 서로 다른 개체를 구별하기 위해 이름 부여
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extension;
			// UUID로 받은 값 + 원래 파일 이름의 확장자 -> 저장될 파일 이름
		
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
			// 파일이 저장될 위치, 파일 이름 -> 파일에 쓸 출력스트림 생성
		
		fos.write(fileData);
		fos.close();
		
		return savedFileName;
	}
	
	public void deleteFile(String filePath) throws Exception {
		File deleteFile = new File(filePath);
		
		if(deleteFile.exists()) {
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		} else {
			log.info("파일이 존재하지 않습니다.");
		}
	}

}
