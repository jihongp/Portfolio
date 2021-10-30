package com.docmall.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.domain.StatChartVO;
import com.docmall.service.ChartService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/admin/chart/*")
public class ChartController {
	
	
	private ChartService service;
	
	@GetMapping("/statistics")
	public void demo01(Model model) {
		
		//db에서 정보를 읽어와서, 아래와 같이 데이터 포맷을 구성해야 한다.
		
		/*
		[
	      ['Year', 'Sales'],
	      ['2010', 10],
	      ['2020', 14],
	      ['2030', 16],
	      ['2040', 22],
	      ['2050', 28]
	    ]
		*/
		
		List<StatChartVO> yearSalesList = service.getYearSales();
		
		String dataStr = "[['Year', 'Sales'],";
		
		int k = 0;
		
		for(StatChartVO vo : yearSalesList) {
			dataStr += "['" + vo.getYear() + "'," + vo.getTotal() + "]";
			
			k++;
			
			// 마지막 데이터 처리시 false가 되어  ,를 포함하지 않는다.
			if(k < yearSalesList.size()) {
				dataStr += ",";
			}
		}
		
		dataStr += "]";
		
		model.addAttribute("data", dataStr);
		
	}

}
