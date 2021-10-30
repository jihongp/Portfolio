package com.docmall.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CartListVO;
import com.docmall.domain.MemberVO;
import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderDetailVOList;
import com.docmall.domain.OrderVO;
import com.docmall.domain.ProductVO;
import com.docmall.domain.UserOrderDetailInfo;
import com.docmall.service.CartService;
import com.docmall.service.OrderService;
import com.docmall.service.ProductService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@RequestMapping("/order/*")
@Controller
public class OrderController {
	
	@Inject
	private OrderService o_service;
	
	@Inject
	private ProductService p_service;
	
	@Inject
	private CartService c_service;
		
	@RequestMapping("/orderInfo")
	public void orderInfo(Integer pdt_num, Model model) throws Exception{
		
		ProductVO vo = p_service.get(pdt_num);
		
		model.addAttribute("vo", vo);
		
	}
	
	@RequestMapping("/orderCartInfo")
	public void orderCartInfo(HttpSession session, Model model) throws Exception{
		
		// 장바구니테이블에서 사용자 상품정보를 가져와서 출력하는 작업.
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		List<CartListVO> list = c_service.cartList(mem_id);
		
		model.addAttribute("list", list);
		
	}
	
	// 1)바로구매 : 상품 1개일 경우  2)장바구니->구매 : 상품이 여러개일 경우(상품 1개이상일 경우)
	@PostMapping("/orderProcess")
	public String orderProcess(OrderVO vo,OrderDetailVOList orderDetailList /*OrderDetailVO vo2*/ ,HttpSession session) throws Exception{
		
		
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		vo.setMem_id(mem_id);
		
		log.info(orderDetailList); // 주문번호가 null상태
		
		o_service.orderInfoAdd(vo, orderDetailList);
		
		return "redirect:/order/orderInfoResult";
	}
	
	
	@GetMapping("/orderInfoResult")
	public void orderInfoResult()
	{
		
	}
	
	// 사용자 주문내역 주소
	@GetMapping("/userOrderInfo")
	public void userOrderInfo(HttpSession session, Model model) throws Exception{
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		List<OrderVO> list = o_service.userOrderInfo(mem_id);
		
		model.addAttribute("list", list);
		
	}
	
	// 리턴되는 List<UserOrderDetailInfo> 타입의 값을 json으로 변환시키는 라이브러리 : jackson-databin(pom.xml)
	/*
	 * 1) /userOrderDetailInfo/8 요청 -> 리턴값을 xml
	 * 2) /userOrderDetailInfo/8.json 요청 -> 리턴값을 json
	 */
	@ResponseBody
	@GetMapping(value = "/userOrderDetailInfo/{odr_code}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<UserOrderDetailInfo>> userOrderDetailInfo(@PathVariable("odr_code") Long odr_code) throws Exception{
		
		ResponseEntity<List<UserOrderDetailInfo>> entity = null;
		
		entity = new ResponseEntity<List<UserOrderDetailInfo>>(o_service.userOrderDetailInfo(odr_code), HttpStatus.OK);
				
		return entity;
	}
	
	
	

}
