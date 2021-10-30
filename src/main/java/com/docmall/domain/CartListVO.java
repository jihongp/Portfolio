package com.docmall.domain;

import lombok.Data;

@Data
public class CartListVO {

	//c.cart_code, c.pdt_num, c.mem_id, c.cart_amount, p.pdt_img, p.pdt_name
	private Integer cart_code;
	private Integer pdt_num;
	private String mem_id;
	private int cart_amount;
	private String pdt_img;
	private String pdt_name;
	private int pdt_price;
	
}
