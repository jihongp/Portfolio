package com.docmall.domain;

import java.util.Date;

import lombok.Data;

@Data
public class AdminVO {
	
	//admin_id, admin_pw, logintime
	private String admin_id;
	private String admin_pw;
	private Date logintime;

}
