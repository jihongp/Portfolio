package com.docmall.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.AdminVO;
import com.docmall.service.AdminService;
import com.docmall.util.SessionConfig;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller // adminController bean 생성
@AllArgsConstructor
@Log4j
@RequestMapping("/admin/*")
public class AdminController {
	
	private AdminService service; // 생성자 메서드를 이용한 주입
	
	//관리자 로그인 폼
	@GetMapping("/main")
	public String adminLoginForm() {
		
		return "/admin/adminLogin";
	}
	
	//관리자 로그인
	@PostMapping("/adminLogin")
	public String adminLoginCheck(AdminVO vo, HttpSession session, RedirectAttributes rttr) throws Exception{
		
		String returnUrl = "";
		
		AdminVO adLoginVO = service.adminLogin(vo);
		
		if(adLoginVO != null) {
			returnUrl = "/admin/adminMenu";
			
			SessionConfig.getSessionCheck("loginDuplicateStatus", adLoginVO.getAdmin_id());
			
			session.setAttribute("adminStatus", adLoginVO); //adminStatus 세션 이름으로 세션저장.			
			session.setAttribute("loginDuplicateStatus", adLoginVO.getAdmin_id()); // 로그인상태 서버측메모리에 저장
			
			
			//현재 관리자 로그인한 시간을 업데이트 처리.
			service.loginTimeUpdate(adLoginVO);
			
		}else {
			returnUrl = "/admin/main";
			rttr.addFlashAttribute("msg", "loginFail");
		}
		
		return "redirect:" + returnUrl;
	}
	
	
	//관리자 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		
		session.invalidate();
		
		rttr.addFlashAttribute("msg", "logout");
		
		return "redirect:/admin/main";
	}
	
	
	//관리자 비번변경 폼
	@GetMapping("/adminPwChange")
	public void adminPwChange(HttpSession session) throws Exception{
		
	}
	
	
	//관리자 비밀번호 변경
	@PostMapping("/adminPwChange")
	public String adminPwChange(HttpSession session, @RequestParam("currentpw") String currentpw, @RequestParam("changepw") String changepw, RedirectAttributes rttr) throws Exception {
		
		String admin_id = ((AdminVO) session.getAttribute("adminStatus")).getAdmin_id();
		
		AdminVO vo = new AdminVO();
		vo.setAdmin_id(admin_id);
		vo.setAdmin_pw(currentpw); // 관리자가 입력한 확인용 비밀번호
				
		//관리자 비번변경
		AdminVO adminVO = service.adminLogin(vo);
		if(adminVO != null) {
			
			service.changePW(adminVO, changepw);
			rttr.addFlashAttribute("msg", "changeSuccess");
			
		}else { // 현재 비밀번호가 일치하지 않는 경우
			rttr.addFlashAttribute("msg", "changeFail");
		}
		
		
		return "redirect:/admin/adminPwChange";
	}
	
	
	// 관리자 메뉴페이지
	@GetMapping("/admin/adminMenu")
	public void adminMenu() throws Exception{
		
	}
	
	
	@GetMapping("/admin/starter")
	public void starter() {
		
	}
	
	
}
