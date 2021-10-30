package com.docmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.AdminVO;
import com.docmall.mapper.AdminMapper;

import lombok.Setter;


@Service // adminServiceimpl bean 생성
public class AdminServiceImpl implements AdminService {

	@Setter(onMethod_ = @Autowired)
	private AdminMapper mapper; // setter을 통한 메서드 주입
	
	@Override
	public AdminVO adminLogin(AdminVO vo) {
		// TODO Auto-generated method stub
		return mapper.adminLogin(vo);
	}

	@Override
	public void loginTimeUpdate(AdminVO vo) {
		// TODO Auto-generated method stub
		mapper.loginTimeUpdate(vo);
	}

	@Override
	public void changePW(AdminVO vo, String changepw) {
		// TODO Auto-generated method stub
		mapper.changePW(vo, changepw);
	}

		

	

}
