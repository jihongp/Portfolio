package com.docmall.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.LoginDTO;
import com.docmall.domain.MemberVO;
import com.docmall.service.MemberService;
import com.docmall.util.SessionConfig;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller // memberController bean 생성
@Log4j
//@AllArgsConstructor
@RequestMapping("/member/*")
public class MemberController {
	
	@Inject // BCryptPasswordEncoder bean이 대입
	private BCryptPasswordEncoder cryptPassEnc; // 스프링시큐리티에서 제공하는 암호화 기능을 담당하는 클래스  // 이 구문은 객체를 선언한 것이지 생성한것은 아니다. 
	
	@Inject
	private MemberService service;	// DI 진행된다. memberServiceImpl bean
	/* MmemberServiceImpl service = new MemberServiceImpl() */
	
	// 회원가입 폼
	@GetMapping("/join") // join 이름을 disptcherservlet객체에 제어를 넘긴다.
	public void join() {
		
	}
	
	
	// 회원가입저장
	@PostMapping("/join")
	public String joinOk(MemberVO vo, RedirectAttributes rttr) throws Exception {
		
		// 비밀번호를 암호화 하면, 원래 일반비밀번호로 디코딩 할수가 없다.
		vo.setMem_pw(cryptPassEnc.encode(vo.getMem_pw()));
		
		service.join(vo);
		
		return "redirect:/member/login";
	}
	
	// 회원수정폼
	@GetMapping(value = { "/modify", "/mypage"})
	public void modify(HttpSession session, Model model) throws Exception{
		
		 String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		 String mem_pw = ((MemberVO)session.getAttribute("loginStatus")).getMem_pw();
		
		 LoginDTO dto = new LoginDTO();
		 dto.setMem_id(mem_id);
		 dto.setMem_pw(mem_pw);
		 
		 MemberVO vo = service.login(dto);
		 
		 model.addAttribute("memberVO", vo);
		 
		
	}
	
	
	// 회원수정저장
	@PostMapping("/modify")
	public String modify(MemberVO vo, RedirectAttributes rttr) throws Exception{
		
		//아이디와 비번을 확인하는 작업
		LoginDTO dto = new LoginDTO();
		dto.setMem_id(vo.getMem_id());
		dto.setMem_pw(vo.getMem_pw());
		 
		String returnUrl = "";
		
		MemberVO dvo = service.login(dto);
		
		if(dvo != null)
		{
			//회원수정작업
			vo.setMem_pw(dvo.getMem_pw());
			service.modify(vo);
			rttr.addFlashAttribute("msg", "modifySuccess");
			
			returnUrl = "/";
		}else {
			// 입력 비번이 틀린경우
			returnUrl = "/member/modify";
			rttr.addFlashAttribute("msg", "modifyFail");
		}
		
		return "redirect:" + returnUrl;
	}
	
	
	// 회원삭제 비밀번호 입력폼
	@GetMapping("/deleteConfirm")
	public void deleteConfirm() {
		
	}
	
	
	// 회원삭제
	@PostMapping("/delete")
	public String delete(HttpSession session, @RequestParam("mem_pw") String mem_pw, RedirectAttributes rttr) throws Exception{
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		LoginDTO dto = new LoginDTO();
		dto.setMem_id(mem_id);
		dto.setMem_pw(mem_pw);
		
		MemberVO vo = service.login(dto); // 실제는 아이디만 비교를 함.
		
		
		String returnUrl = "";
		// 아이디와 비밀번호가 일치여부 확인
		if(vo != null) {
			// 회원삭제구문
			
			dto.setMem_pw(vo.getMem_pw());
			service.delete(dto);
			session.invalidate();
			rttr.addFlashAttribute("msg", "success");
			
		}else {
			// 입력한 비밀번호가 다른경우
			rttr.addFlashAttribute("msg", "fail");
			
		}
		
		
		
		return "redirect:/member/deleteConfirm";
	}
	
	
	// 로그인 폼
	@GetMapping("/login")
	public void login() {
		
	}
	
	
	// 로그인
	// HttpSession 인터페이스 : 로그인 상태를 서버측에 저장하여, 사용자가 로그인 상태인지 체크목적으로 사용
	@PostMapping("/login")
	public String login(LoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception{
		
		MemberVO vo = service.login(dto); // 아이디만으로 회원정보를 가지고옴
		
		String returnUrl = "";
		
		// 아이디에 해당하는 정보가 존재
		if(vo != null) {
			// cryptPassEnc.matches("입력한 비밀번호", "암호화된 비밀번호")
			if(cryptPassEnc.matches(dto.getMem_pw(), vo.getMem_pw())) {
			
			returnUrl = "/";
			if(session.getAttribute("dest") != null) returnUrl = (String) session.getAttribute("dest");
			
			SessionConfig.getSessionCheck("loginDuplicateStatus", vo.getMem_id());
			
			session.setAttribute("loginStatus", vo);
			
			session.setAttribute("loginDuplicateStatus", vo.getMem_id()); // 로그인상태 서버측메모리에 저장
			
			// 세션유효시간 : 30초.  현재 접속자 확인목적..
			//session.setMaxInactiveInterval(30);
			
			}else {
				//입력한 비밀번호가 다를경우 진행되는 부분
				returnUrl = "/member/login";
				
				rttr.addFlashAttribute("msg", "loginFail"); // login.jsp에서 msg키값을 사용이 가능.
			}
		}else {
			returnUrl = "/member/login";
			
			rttr.addFlashAttribute("msg", "loginFail"); // login.jsp에서 msg키값을 사용이 가능.
		}
		
		// String.format("redirect:{0}", vo != null ? "/" : "/member/login");
		
		return "redirect:" + returnUrl; // 메인페이지 주소
	}
	
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		
		session.invalidate(); // 사용자에 의하여 생성된 세션정보는 모두소멸
		
		return "redirect:/";
	}
	
	
	// 아이디중복체크
	@ResponseBody  // ajax요청이기때문에 달아줘야한다.
	@PostMapping("/checkUserID")
	public ResponseEntity<String> checkUserID(@RequestParam("mem_id") String mem_id) throws Exception{
		
		ResponseEntity<String> entity = null;
	
		String msg = "yes";
		
		if(service.checkUserID(mem_id) != null) msg = "no";
		
		entity = new ResponseEntity<String>(msg, HttpStatus.OK);
		
		return entity;
	}
	
	
	// Mypage
//	@GetMapping("/mypage")
//	public void mypage(HttpSession session, Model model) throws Exception{
//		
//	}
	
	
	// 아이디찾기
	
	
	// 비밀번호 찾기
	@GetMapping("/forgotPW")
	public void forgotPW() {
		
	}
	
	// 비밀번호 변경 폼
	@GetMapping("/changePW")
	public void changePWForm() {
		
	}
	
	// 비밀번호 변경
	@PostMapping("/changePW")
	public String changePW(String mem_pw, String mem_newpw, HttpSession session, RedirectAttributes rttr) {
			
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		//아이디와 비번을 확인하는 작업
		LoginDTO dto = new LoginDTO();
		dto.setMem_id(mem_id);
		dto.setMem_pw(mem_pw);
		
		MemberVO vo = service.login(dto); // 아이디만으로 회원정보를 가지고옴
				
		
		// 아이디와 비밀번호가 일치여부 확인
		if(vo != null) {
			if(cryptPassEnc.matches(dto.getMem_pw(), vo.getMem_pw())) {
				
				// 새로운 비밀번호 변경작업
				service.pwChange(mem_id,cryptPassEnc.encode(mem_newpw));
				rttr.addFlashAttribute("status", "success");
				
			}else {
				rttr.addFlashAttribute("status", "fail");
			}
		}
		
		return "redirect:/member/changePW";
	}

}
