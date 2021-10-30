package com.docmall.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import lombok.extern.log4j.Log4j2;

// 업로드기능과 관련된 작업을 담당하는 클래스
//@Log4j2
public class UploadFileUtils {

	/*
	  String uploadPath : 업로드경로
	  String originalName : 원본파일명
	  byte[] fileData : 첨주된 파일을 참조하는 바이트 배열
	 */
	
	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws IOException {
	
		// 업로드시 파일명이 중복되는 것을 방지하기 위하여, 고유의 랜덤문자열을 반환해준다.  고유의 값을 파일명에 추가하여 사용하는 용도.
		UUID uid = UUID.randomUUID();
		
		String savedName = uid.toString() + "_" + originalName;
		
		//업로드시 날짜별 폴더를 생성하여, 파일을 관리한다.
		
		// 파일을 업로드할 폴더 문자열을 반환받음.
		String savedPath = calcpath(uploadPath); // "\2021\08\23"
		
		//파일 업로드 작업
		File target = new File(uploadPath + savedPath, savedName); //"D:\\upload\\real"+"\2021\08\23"+"a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
		
		FileCopyUtils.copy(fileData, target); // 예외 관련 작업 문법을 가지고있음.
		
		// 파일이 이미지 파일 여부에 따라서, 썸네일작업을 진행
		
		String formatName = originalName.substring(originalName.lastIndexOf(".") + 1); // "1.jpg"
		
		String uploadedFileName = null; // 썸네일 이미지 작업에 의한 파일명
		
		// 이미지 파일 또는 일반 파일을 확인하는 작업.
		if(MediaUtils.getMediaType(formatName) != null) {
			// 이미지 파일-> 썸네일 작업
			uploadedFileName = makeThumbnail(uploadPath, savedPath, savedName);
		}else {
			// 일반파일 작업
			uploadedFileName = makeIcon(uploadPath, savedPath, savedName);
		}
		
		return uploadedFileName;
	}

	private static String makeIcon(String uploadPath, String savedPath, String savedName) {
		
		String iconName = uploadPath + savedPath + File.separator + savedName;
		
		return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	/*
	 String uploadPath : "D:\\upload\\real"
	 String savedPath : "\2021\08\23"
	 String savedName : "a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
	 */
	private static String makeThumbnail(String uploadPath, String savedPath, String savedName) throws IOException {
		
		// 업로드된 이미지 파일을 썸네일 작업을 하기 위하여, 메모리상에 불러들이는 작업구문.(원본 이미지파일 참조)
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath + savedPath, savedName));
		
		// 썸네일 작업을 통한사본이미지 작업을 하기위한 준비. (파일크기 줄이고, 해상도 떨어지지 않게 하는 작업환경(
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		
		// 썸네일 이미지 파일명 : "D:\\upload\\real" + "\2021\08\23" + "\" + "s_" + "a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg"
		String thumbnailName = uploadPath + savedPath + File.separator + "s_" + savedName;
		
		String formatName = savedName.substring(savedName.lastIndexOf(".")+1);
		
		// 메모리상에 있던 썸네일 기능이 설정된 작업데이터를 파일 출력
		ImageIO.write(destImg, formatName.toUpperCase(), new File(thumbnailName));
		
		// "/2021/08/23/s_a95f633e-7fea-44a8-aada-a6504f14f05c_1.jpg" -> 클라이언트의 브라우저에게 응답
		return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	// 날짜별 폴더관리 기능
	private static String calcpath(String uploadPath) {
		// TODO Auto-generated method stub
		
		Calendar cal = Calendar.getInstance();
		
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)); //""2021-08-23"
		
		makeDir(uploadPath, yearPath, monthPath, datePath);
		
		return datePath;
	}

	//날짜별 폴더생성기능.
	private static void makeDir(String uploadPath, String... paths) {
		// TODO Auto-generated method stub
		
		if(new File(paths[paths.length-1]).exists()){
			return;
			
		}
		
		// 위의 구문이 false이면 폴더생성작업
		for(String path : paths) {
			File dirPath = new File(uploadPath + path);
			
			if(! dirPath.exists()) {
				dirPath.mkdir();
				
			}
		}
		
	}
	
	
	public static ResponseEntity<byte[]> getFileByte(String fileName, String uploadPath) throws IOException {
		
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
	
	// 파일삭제
	public static void deleteFile(String uploadPath, String fileName) {
		
		// 기본이미지, 썸네일 이미지 2개 삭제.
		
		//기본이미지 파일명 . s_ 제외하는 작업
		String front = fileName.substring(0, 12);
		String end = fileName.substring(14);
		String origin = front + end;
		
		//기본이미지 삭제
		new File(uploadPath + origin.replace('/', File.separatorChar)).delete();
		//썸네일 이미지 삭제
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
	}
	
}
