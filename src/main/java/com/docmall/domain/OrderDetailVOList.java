package com.docmall.domain;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetailVOList {

	// 클라이언트에서 사용할 파라미터명은 orderDetailList[0].odr_code, orderDetailList[0].pdt_num, orderDetailList[0].odr_amount , orderDetailList[0].odr_price
	private List<OrderDetailVO> orderDetailList;
}
