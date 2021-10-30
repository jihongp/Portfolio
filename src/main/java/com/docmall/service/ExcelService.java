package com.docmall.service;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.docmall.domain.ProductVO;

public interface ExcelService {

	public List<ProductVO> getProductExcel();
	
	public SXSSFWorkbook excelFileDownloadProcess(List<ProductVO> list);
}
