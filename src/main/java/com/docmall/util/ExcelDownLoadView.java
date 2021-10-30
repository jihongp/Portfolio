package com.docmall.util;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

// 엑셀파일로 다운로드 받는 핵심적인 기능
// bean으로 등록되는 view(jsp)를 대신하여 사용하는 클래스는 AbstractView 추상클래스를 상속받아야 한다는 규칙이 있다.
public class ExcelDownLoadView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		String workbookName = "상품목록";
		
		Date date = new Date();
		SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat hourformat = new SimpleDateFormat("hhmmss");
		String day = dayformat.format(date);
		String hour = dayformat.format(date);
		
		String fileName = workbookName + "_" + day + "_" + hour + ".xlsx";
		
		// 여기서부터는 각 브라우저에 따른 파일이름 인코딩작업
		String browser = request.getHeader("User-Agent");
		if (browser.indexOf("MSIE") > -1) {
			fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else if (browser.indexOf("Trident") > -1) { // IE11
				fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else if (browser.indexOf("Firefox") > -1) {
				fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (browser.indexOf("Opera") > -1) {
				fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (browser.indexOf("Chrome") > -1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fileName.length(); i++) {
				char c = fileName.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			fileName = sb.toString();
			} else if (browser.indexOf("Safari") > -1){
				fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
			} else {
				fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
			}
		
		//서버에서 클라이언트에게서 보내는 파일에 대한 정보를 설정하는 헤더작업
		
		response.setContentType("application/download;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		
		//출력스트림작업
		OutputStream os = null;
		SXSSFWorkbook workbook = null;
		
		try {
			workbook = (SXSSFWorkbook) model.get("workbook");
			os = response.getOutputStream();
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(workbook != null) {
				try {
					workbook.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(os != null) {
				try {
					os.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
