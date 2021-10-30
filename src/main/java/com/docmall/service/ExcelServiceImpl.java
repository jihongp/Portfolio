package com.docmall.service;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.ProductVO;
import com.docmall.mapper.ExcelMapper;

import lombok.Setter;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Setter(onMethod_ = @Autowired)
	private ExcelMapper mapper;
	
	@Override
	public List<ProductVO> getProductExcel() {
		// TODO Auto-generated method stub
		return mapper.getProductExcel();
	}

	// SXSSFWorkbook클래스 : 엑셀의 통합문서 의미로 바라봄. 
	@Override
	public SXSSFWorkbook excelFileDownloadProcess(List<ProductVO> list) {
		// TODO Auto-generated method stub
		return this.makeProductVOExcelWorkbook(list);
	}
	
	
	// db에서 가져온 ProductVO list컬렉션 데이터를 엑셀문서의 시트에 작업하는 기능.(메모리상)
	public SXSSFWorkbook makeProductVOExcelWorkbook(List<ProductVO> list) {
		
		//통합문서생성
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
		//통합문서내의 시트생성
		SXSSFSheet sheet = workbook.createSheet("상품목록");
		
		sheet.setColumnWidth(0, 2500);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 2500);
		
		//시트내의 새로운 행 생성
		SXSSFRow titleRow = sheet.createRow(0);
		
		// 행의 셀 3개 생성
		SXSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("상품코드");
		
		titleCell = titleRow.createCell(1);
		titleCell.setCellValue("상품이름");
		
		titleCell = titleRow.createCell(2);
		titleCell.setCellValue("상품가격");
		
		// 시트내의 데이터행 생성
		
		SXSSFRow dataRow = null;
		SXSSFCell dataCell = null;
		
		for(int i=0; i<list.size(); i++) {
			ProductVO vo = list.get(i);
			
			dataRow = sheet.createRow(i+1);
			
			dataCell = dataRow.createCell(0);
			dataCell.setCellValue(vo.getPdt_num());
			
			dataCell = dataRow.createCell(1);
			dataCell.setCellValue(vo.getPdt_name());
			
			dataCell = dataRow.createCell(2);
			dataCell.setCellValue(vo.getPdt_price());
		}
		
		
		
		return workbook;
	}

}
