package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderDetailVOList;
import com.docmall.domain.OrderVO;
import com.docmall.domain.UserOrderDetailInfo;
import com.docmall.mapper.CartMapper;
import com.docmall.mapper.OrderMapper;

import lombok.Setter;

@Service
public class OrderServiceImpl implements OrderService {

	@Setter(onMethod_ = @Autowired)
	private OrderMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private CartMapper mapper2;

	// 트랜잭션 적용을 해줘야 한다.
	@Transactional
	@Override
	public void orderInfoAdd(OrderVO vo, OrderDetailVOList orderDetailList) {
		
		Long odr_code = mapper.getOrderSeq(); // 주문번호 시퀀스 호출
		vo.setOdr_code(odr_code); // 주문번호 시퀀스 대입
		
		//주문상세정보: 상품이 여러개일 경우
		List<OrderDetailVO> list = orderDetailList.getOrderDetailList();
		int count = list.size();
		
		vo.setOdr_count(count);
		
		//주문자 정보
		mapper.orderInfoAdd(vo);
		
		
	
		for(int i=0; i<list.size(); i++) {
			OrderDetailVO orderDetail = list.get(i);
			orderDetail.setOdr_code(odr_code); // 주문번호 시퀀스 대입
			mapper.orderDetailAdd(orderDetail);
		}
		
		// 사용자 장바구니 삭제
		mapper2.cartdeletebyUserID(vo.getMem_id());
	}

	@Override
	public List<OrderVO> userOrderInfo(String mem_id) {
		// TODO Auto-generated method stub
		return mapper.userOrderInfo(mem_id);
	}

	@Override
	public List<UserOrderDetailInfo> userOrderDetailInfo(Long odr_code) {
		// TODO Auto-generated method stub
		return mapper.userOrderDetailInfo(odr_code);
	}
}
