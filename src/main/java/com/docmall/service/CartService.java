package com.docmall.service;

import java.util.List;

import com.docmall.domain.CartListVO;
import com.docmall.domain.CartVO;

public interface CartService {

	public void cartAdd(CartVO vo);
	
	public List<CartListVO> cartList(String mem_id);
	
	public void cartEdit(Integer cart_code,int cart_amount);
	
	public void cartdelete(Integer cart_code);
	
	public void cartdeletebyUserID(String mem_id);
}
