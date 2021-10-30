package com.docmall.domain;

import lombok.Data;

@Data
public class BoardAttachVO {

	// uuid, uploadPath, fileName, fileType, bno
	
	private String uuid;
	private String uploadPath;
	private String fileName;
	private boolean fileType;
	
	private Long bno;
}
