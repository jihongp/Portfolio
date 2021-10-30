package com.docmall.service;

import com.docmall.domain.AdminVO;

public interface AdminService {

	public AdminVO adminLogin(AdminVO vo);
	
	public void loginTimeUpdate(AdminVO vo);
	
	public void changePW(AdminVO vo,String changepw); // 서비스에서는 파라미터 2개면 @Param 사용안해도 됨
}
