package com.docmall.mapper;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.AdminVO;

public interface AdminMapper {

	public AdminVO adminLogin(AdminVO vo);
	
	public void loginTimeUpdate(AdminVO vo);
	
	// mapper인터페이스의 메서드가 파라미터가 2개 이상일경우 @Param 어노테이션을 사용해야한다.
	public void changePW(@Param("vo") AdminVO vo, @Param("changepw") String changepw); // mapper인터페이스에서만 파라미터 2개면 @Param 사용
}
