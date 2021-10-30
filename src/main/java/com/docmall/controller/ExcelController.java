package com.docmall.controller;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.domain.ProductVO;
import com.docmall.service.ExcelService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@AllArgsConstructor
@RequestMapping("/excel/*")
@Log4j
public class ExcelController {
	
	private ExcelService service;
	
	/// 엑셀파일로 다운로드 받기 위한 주소
	@PostMapping("/downLoadExcelFile")
	public String downLoadExcelFile(Model model) {
		
		// 엑셀 데이터로 사용할 DB에서 가져올 데이터작업
		
		List<ProductVO> list = service.getProductExcel();
		SXSSFWorkbook workbook = service.excelFileDownloadProcess(list);
		
		model.addAttribute("workbook", workbook); //메모리상에 작업이 되어있는 통합문서객체.
		
		// jsp에 해당하는 View파일명이 아니라, servlet-context.xml의 BeanNameViewResolver클래스 bean설정에 의하여 bean이름으로 해석됨.
		// bean (객체라고 표현)
		return "excelDownLoadView"; 
	}

}
