package com.docmall.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CartListVO;
import com.docmall.domain.CartVO;
import com.docmall.domain.MemberVO;
import com.docmall.service.CartService;
import com.docmall.util.UploadFileUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@RequestMapping("/cart/*")
@Controller
public class CartController {

	
	private CartService service;
	
	@Resource(name = "uploadPath")
	private String uploadPath;
	
	@ResponseBody
	@PostMapping("/cartAdd")
	public ResponseEntity<String> cartAdd(Integer pdt_num, Integer cart_amount, HttpSession session ) throws Exception{
				
		 ResponseEntity<String> entity = null;
		 
		 String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		 
		 CartVO vo = new CartVO();
		 vo.setPdt_num(pdt_num);
		 vo.setMem_id(mem_id);
		 vo.setCart_amount(cart_amount);
		 
		 service.cartAdd(vo);
		 
		 entity = new ResponseEntity<String>("success", HttpStatus.OK);
		 
		 return entity;
	}
	
	// 장바구니 목록
	@GetMapping("/list")
	public void list(HttpSession session, Model model) throws Exception{
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		List<CartListVO> list = service.cartList(mem_id);
		model.addAttribute("list", list);
	}
	
	@ResponseBody // ajax라 사용
	@PostMapping("/edit")
	public ResponseEntity<String> cartedit(Integer cart_code, Integer cart_amount) throws Exception{
		
		ResponseEntity<String> entity = null;
		
		// db연동작업구문
		
		service.cartEdit(cart_code, cart_amount);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	
	@GetMapping("/edit")
	public String cartedit2(Integer cart_code, Integer cart_amount) throws Exception{
		
		
		// db연동작업구문
		
		service.cartEdit(cart_code, cart_amount);
		
		return "redirect:/cart/list"; // list.jsp파일의 실행된 html code
	}
	
	@ResponseBody // ajax라 사용
	@PostMapping("/delete")
	public ResponseEntity<String> cartdelete(Integer cart_code) throws Exception{
		
		ResponseEntity<String> entity = null;
		
		// db연동작업구문
		
		service.cartdelete(cart_code);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	// 장바구니 비우기
	@GetMapping("/delete")
	public String cartdeletebyUserID(HttpSession session) throws Exception{
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		service.cartdeletebyUserID(mem_id);
		
		return "redirect:/cart/list"; // list.jsp파일의 실행된 html code
	}
	
	@ResponseBody
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception{
		
		ResponseEntity<byte[]> entity = null;
		
		
		entity = UploadFileUtils.getFileByte(fileName, uploadPath);
		
		return entity;
	}
}
