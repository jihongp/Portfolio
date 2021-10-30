package com.docmall.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.docmall.service.ProductService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/*
톰캣이 시작(구동)되면서,  아래 message()메서드가 호출되어, 실행된다.

com.docmall.controller 패키지의 Controller에서 진행되는 모든 jsp는 아래 model작업한 내용을 참조하게 된다.
*/
@Log4j
@AllArgsConstructor
@ControllerAdvice(basePackages = {"com.docmall.controller"})
public class GlobalControllerAdvice {
	
	private ProductService service;
	
	@ModelAttribute
	public void message(Model model) {
		log.info("톰캣시작과함께 호출(실행됨)");
		
		model.addAttribute("globalMainCategory", service.mainCategory());
		// 1차카테고리 model작업
		
	}

}
