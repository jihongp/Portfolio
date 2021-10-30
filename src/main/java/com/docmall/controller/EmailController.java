package com.docmall.controller;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.EmailDTO;
import com.docmall.domain.MemberVO;
import com.docmall.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@AllArgsConstructor
@Log4j
@RequestMapping("/email/*")
public class EmailController {
	
	private BCryptPasswordEncoder cryptPassEnc;
	
	private MemberService service;	
	
	private JavaMailSender mailSender;
	
	@ResponseBody
	@PostMapping("/send")
	public ResponseEntity<String> send(String mem_id, String mem_email) throws Exception{
		
		ResponseEntity<String> entity = null;
		

		
		MemberVO vo = service.mailConfirm(mem_id, mem_email);
		
		String message = "";
		
		if(vo != null) {
			message = "success";
			

			String impw = MakeImsiPassword();
			// 임시 비밀번호 암호화 작업하여, DB에 반영
			service.pwChange(mem_id, cryptPassEnc.encode(impw));
			
			//사용자에게 임시비밀번호를 메일주소로 발급
			String subject = "임시비밀번호 hongmall 발송";
			String body = String.format("발급한 비밀번호는 %s 입니다.", impw);
			
			EmailDTO dto = new EmailDTO("hongmall", "qkrwlghd2002@naver.com", vo.getMem_email(), subject, body);
			sendMail(dto);

			
		}else {
			message = "fail";
			
		}
		
		entity = new ResponseEntity<String>(message, HttpStatus.OK);
		
		return entity;
	}
	
	// 임시비밀번호 랜덤생성
	private String MakeImsiPassword() {
		
		String pw = "";
		
		for(int i=0; i<6; i++) {
			pw += String.valueOf((int)(Math.random() * 9) + 1);
		}
		
		return pw;
	}
	
	// 메일보내기 기능
	public void sendMail(EmailDTO dto) {
		
		MimeMessage msg = mailSender.createMimeMessage();
		

		try {
			// 받는사람 설정(이메일)
			msg.addRecipient(RecipientType.TO, new InternetAddress(dto.getReceiveMail()));
			// 보내는 사람 설정(이메일, 이름)
			msg.addFrom(new InternetAddress[] {new InternetAddress(dto.getSenderMail(), dto.getSenderName())});
			// 제목
			msg.setSubject(dto.getMessage(), "utf-8");
			// 본문내용
			msg.setText(dto.getMessage(), "utf-8");
			
			mailSender.send(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
