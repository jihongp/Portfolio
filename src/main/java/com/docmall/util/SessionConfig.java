package com.docmall.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener // 서블릿3.0이상에서 지원
public class SessionConfig implements HttpSessionListener {

	// 구현할수 있는 기능?
	// 중복로그인 방지
	// 현재 접속자 수, 전체 방문자 수
	
	private static int currentCount; // 현재 접속자 수
	private static int visitTotalCount; // 전체 방문자 수
	
	
	public static int getCurrentCount() {
		return currentCount;
	}

	public static int getVisitTotalCount() {
		return visitTotalCount;
	}

	
	/*
	 세션 : 웹브라우저가 서버(사이트)에 접속하게 되면 바로 생성됨. 서버로부터 각 브라우저 사용자를 식별하기 위한 세션ID를 쿠키형태로 발급받게된다.
	 로그인을 통하여, 생성되는 것이 아니다.
	 로그인시 세션상태를 저장해두는 의미는 세션 ID를 통한 정보를 저장
	 
	 
	 
	 리스너(Listener) : 사전적인 의미는 소리를 듣는사람(청취자)
	 프로그래밍적인 의미는 특정한 이벤트가 발생이 되었을 때 대기하고 있다가 자동으로 실행되는 컴포넌트(메서드 또는 함수)
	 */
	
	// HashMap : null 사용가능 , ConcurrentHashMap : null 사용 불가능
		
	
	
	// 세션생성시 발급받은 고유한 식별의 세션아이디와 HttpSession을 저장한다.
	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();
	
	// 중복로그인 삭제 : 로그인 성공 후 세션에 저장작업 이전에 호출목적으로 존재.
	public synchronized static String getSessionCheck(String type, String compareId) {
		
		String result = "";
		
		for(String key : sessions.keySet()) {
			System.out.println("세션아이디: " + key);
			HttpSession value = sessions.get(key);
			// if문이 true면 로그인한 사용자가 존재하는 경우
			if(value != null && value.getAttribute(type) != null && value.getAttribute(type).toString().equals(compareId)) {
				System.out.println(value.getAttribute(type).toString());
				result = key.toString();
			}
		}
		
		removeSessionForDoubleLogin(result);
		
		return result;
	}
	
	
	private static void removeSessionForDoubleLogin(String result) {
		// TODO Auto-generated method stub
		System.out.println("remove result: " + result);
		if(result != null && result.length() > 0) {
			sessions.get(result).invalidate(); // 로그인 사용자의 세션을 소멸.
			sessions.remove(result); // Map컬렉션에서 세션정보 제거
		}
		
	}

	// 세션이 생성되었을 때 호출되어지는 이벤트 메서드. 로그인시 세션이 생성되는 것이 아니다.
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		System.out.println("sessionCreated: " + currentCount);
		sessions.put(se.getSession().getId(), se.getSession());
		
		currentCount++;
		visitTotalCount++;
	}

	// 세션이 소멸되었을 때 호출되어지는 이벤트 메서드 (로그아웃, 브라우저 종료하면서 설정된 세션상태시간이 초과)
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		System.out.println("sessionDestroyed: " + currentCount);
		
		if(sessions.get(se.getSession().getId()) != null) {
			sessions.get(se.getSession().getId()).invalidate(); // 현재 세션에서 생성한 세션정보를 제거
			sessions.remove(se.getSession().getId()); // map 컬렉션에 저장되어 있는 정보제거
		}
		
		currentCount--;

	}

}
