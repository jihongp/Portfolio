package com.docmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.log4j.Log4j;


@Log4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

	// 특정주소 요청시 컨트롤러의 메서드 동작이전에 먼저 호출되는 메서드
	// 인증된 사용자만 접근하는 주소를 인터셉터로 설정하여, 인증여부를 체크하는 목적으로 아래 메서드를 사용한다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 롬복에서 제공하는 log객체가 작동안함.
		//log.info("테스트");
		//System.out.println("테스트");
		
		HttpSession session = request.getSession();
		
		
		
		if(session.getAttribute("loginStatus") == null) {
			
			
			if(isAjaxRequest(request)) {
				//log.info("AjaxRequest");
				System.out.println("AjaxRequest");
				response.sendError(500); // 오류를 의미하는 http상태코드를 설정해야. 클라이언트의 ajax에서 오류로 인식하여  $(document).ajaxError()동작된다.
				
				/*
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println(-1);
				out.flush();
				*/
				return false;
			}else {
			
			//log.info("인증된 상태가 아님");
			saveDest(request);
			System.out.println("인증된 상태가 아님");
			
			
			
			
			response.sendRedirect("/member/login");
			return false; // 컨트롤러의 메서드로 진행이 안됨
			}
						
			
		}else {
			log.info("인증된 상태");
			return true;
		}
		

	}

	// 클라이언트의 주소요청이 ajax요청인지 판단하는 기능.
	private boolean isAjaxRequest(HttpServletRequest request) {
		
		String header = request.getHeader("AJAX");
		if("true".equals(header)) {
			return true;
		}else {
		
			return false;
		}
	}

	// 요청주소 일경우   /board/list?idx=1, /board/write
	// 비로그인 상태에서 get방식으로 요청한 주소를 세션으로 저장하여, 로그인 후 리다이렉트 하기위한 용도
	private void saveDest(HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		
		if(query == null) {
			query = "";
		}else {
			query = "?" + query;
		}
		
		if(request.getMethod().equals("GET")) {
			// log.info("dest: " + (uri + query));
			System.out.println("dest: " + (uri + query));
			request.getSession().setAttribute("dest", uri + query);
			
			
		}
	}

}
