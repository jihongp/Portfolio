package com.docmall.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.service.ProductService;
import com.docmall.util.Criteria;
import com.docmall.util.PageDTO;
import com.docmall.util.UploadFileUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@RequestMapping("/product/*")
@Controller
public class ProductController {
	
	private ProductService service;
	
	@Resource(name = "uploadPath")
	private String uploadPath;
	
	// 2차 카테고리를 가져오는 구문
	@ResponseBody
	@GetMapping(value = "/subCategory/{mainCategory}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<CategoryVO>> subCategory(@PathVariable("mainCategory") Integer category){
		
		ResponseEntity<List<CategoryVO>> entity = null;
		
		log.info(category);
		
		// cate_code_pk값이 존재하지 않아, 예외가 발생되는 경우 처리하는 구문
		try {
			entity = new ResponseEntity<List<CategoryVO>>(service.subCategory(category), HttpStatus.OK);
		}catch(Exception e) {
			entity = new ResponseEntity<List<CategoryVO>>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	//2차 카테고리에 해당하는 상품리스트 출력
	@GetMapping(value = "/list")
	public String list(Criteria cri,@ModelAttribute("subCategory") Integer subCategory, Model model) throws Exception{
		
		cri.setAmount(8);
		
		model.addAttribute("list", service.getListWithPaging(cri, subCategory));
		
		int total = service.getTotalCount(subCategory);
		 
		model.addAttribute("pageMaker", new PageDTO(total, cri));
		
		return "/product/list";
	}
	
	// 상품 상세설명
	@GetMapping("/get")
	public void productDetailView(@ModelAttribute("cri") Criteria cri, @RequestParam("pdt_num") Integer pdt_num , Model model) throws Exception{
		
		
		ProductVO vo = service.get(pdt_num);
		
		model.addAttribute("vo", vo);
		
		
	}
	

	@ResponseBody
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception{
		
		ResponseEntity<byte[]> entity = null;
		
		
		entity = UploadFileUtils.getFileByte(fileName, uploadPath);
		
		return entity;
	}
	
}
