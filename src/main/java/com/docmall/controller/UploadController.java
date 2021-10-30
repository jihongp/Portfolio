package com.docmall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.docmall.util.MediaUtils;
import com.docmall.util.UploadFileUtils;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class UploadController {

	@Resource(name= "uploadPath") // name과 아래 볁수명과 같던 안같던 상관 없다.
	private String uploadPath; // servlet-context.xml파일에서 bean 구문 작업이 되어있음.  D:\\upload\\real 경로를 같게됨.
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		
		log.info("uploadForm");
	}
	
	/*
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile) {
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-------------------------------");
			log.info("upload file name: " + multipartFile.getOriginalFilename());
			log.info("upload file size: " + multipartFile.getSize());
			log.info("-------------------------------");
		}
	}
	*/
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile) {
		
		String uploadFolder = "D:\\upload\\real";
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-------------------------------");
			log.info("upload file name: " + multipartFile.getOriginalFilename());
			log.info("upload file size: " + multipartFile.getSize());
			log.info("-------------------------------");
			
			// File 클래스 : 파일, 폴더 관련기능을 제공.
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile); // 파일복사 (업로드)
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
		log.info("uploadAjax");
	}
	
	@ResponseBody // ajax요청시 반드시 사용해야한다. 단 @RestController 어노테이션 사용시 생략한다.
	@PostMapping(value = "/uploadAjax", produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws IOException{
		
		log.info("originalName: " + file.getOriginalFilename());
		
		ResponseEntity<String> entity = null;
	
		// 클라이언트에게 보낼 응답데이터 : 리스폰스엔티티의 첫번째 파라미터 역할. 
		// String : "/2021/08/23/s_a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
		entity = new ResponseEntity<String>(UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes()), HttpStatus.CREATED);
		
		return entity;
	}
	
	@ResponseBody
	@GetMapping("/displayFile") // String fileName : 원본이미지 파일명
	public ResponseEntity<byte[]> displayFile(String fileName) throws IOException{
		
		
		ResponseEntity<byte[]> entity = null;
		
		InputStream in = null;
		
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1); // fomatName의 확장자를 가지고 오는거
		
		// 이미지 파일인지 일반파일인지 확인
		MediaType mType = MediaUtils.getMediaType(formatName);
		
		// 서버에서 클라이언트에 보내는 데이터에 대한 부연설명
		HttpHeaders headers = new HttpHeaders();
		
		// 업로드파일을 참조하는 파일입력스트림 객체생성
		in = new FileInputStream(uploadPath + fileName);
	
		// "/2021/08/23/s_a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
		// "/2021/08/24/9c6d11c6-4b9e-4393-8a9e-1cec3c8ae6fd_1.hwp"
		if(mType != null) {
			headers.setContentType(mType);
		}else {
			// 다운로드
			fileName = fileName.substring(fileName.indexOf("_") + 1); // 1.hwp
			
			// 표준으로 정의되어 있지 않은 파일인 경우 지정
			// 브라우저가 해석하지 못하는 의미로 지정 -> 대화상자저장화면이 진행이됨.
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			// 웹브라우저에게 보내는 정보가 웹페이지 자체 또는 일부가 아니라 클라이언트 컴퓨터에 저장될 용도인것을 알려주는 의미.
			headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"\"");
		}
		
		entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		
		in.close();
		
		return entity;
	}
	
	// 파일 삭제
	@ResponseBody
	@PostMapping("/deleteFile") // deleted   String fileName : 썸네일이미지
	public ResponseEntity<String> deleteFile(String fileName){
		
		
		ResponseEntity<String> entity = null;
		
		// 클라이언트에서 요청한 파일삭제구문
		// 이미지파일 2개 삭제.
		// 일반파일 1개 삭제
		
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
		
		// 이미지 파일인지 일반파일인지 확인
		MediaType mType = MediaUtils.getMediaType(formatName);
		
		
		// "/2021/08/23/s_a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
		// "/2021/08/24/9c6d11c6-4b9e-4393-8a9e-1cec3c8ae6fd_1.hwp"
		if(mType != null) {
			
			String front = fileName.substring(0,12); // "/2021/08/23"
			String end = fileName.substring(14); // "a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
			
			// 원본이미지 삭제
			new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete(); 
		}
		
		// 일반파일 또는 썸네일 이미지파일 삭제
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		
		
		entity = new ResponseEntity<String>("deleted", HttpStatus.OK);		
		
		
		return entity;
	}
	
	
}
