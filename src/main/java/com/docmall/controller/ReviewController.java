package com.docmall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docmall.domain.MemberVO;
import com.docmall.domain.ReviewVO;
import com.docmall.service.ReviewService;
import com.docmall.util.Criteria;
import com.docmall.util.PageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@RequestMapping("/review/*")
@RestController
public class ReviewController {
	
	private ReviewService service;
	
	@PostMapping("/write")
	public ResponseEntity<String> write(ReviewVO vo, HttpSession session) throws Exception{
		
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		vo.setMem_id(mem_id);
		
		log.info(vo);
		
		ResponseEntity<String> entity = null;
		
		service.write(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		
		return entity;
	}
	
	@PostMapping("/modify")
	public ResponseEntity<String> modify(ReviewVO vo) throws Exception{
		
		// 후기내용, 별점 수정내용, 조건식 : 후기 번호
		
		log.info(vo);
		
		ResponseEntity<String> entity = null;
		
		service.modify(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		
		return entity;
	}
	
	@PostMapping("/delete")
	public ResponseEntity<String> delete(int rv_num) throws Exception{
						
		ResponseEntity<String> entity = null;
		
		service.delete(rv_num);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		
		return entity;
	}
	
	@GetMapping(value = "/{pdt_num}/{page}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<Map<String, Object>> listPage(@PathVariable("pdt_num") int pdt_num, @PathVariable("page") Integer page)
	{
		ResponseEntity<Map<String, Object>> entity = null;
	
		Map<String, Object> map = new HashMap<String, Object>();
	
		//1) 댓글목록데이터
		Criteria cri = new Criteria(page, 5);
		//cri.setPageNum(page);
		
		List<ReviewVO> list = service.getListWithPagin(cri, pdt_num);
		
		//2) 페이징정보
		int total = service.getCountBypdtnum(pdt_num);
		PageDTO pageMaker = new PageDTO(total, cri); 
		
		// map컬렉션에 2개의 정보추가
		map.put("list", list); // 1) 댓글목록
		map.put("pageMaker", pageMaker); // 2) 페이징정보
		
		entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
		return entity;	
	}	
	

}
