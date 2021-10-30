<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom shadow-sm">
  <h5 class="my-0 mr-md-auto font-weight-normal"><a class="p-2 text-dark" href="/">DOCMALL</h5>
  <p style="color: blue; font-weight: bold;"><c:if test="${sessionScope.loginStatus != null }">${sessionScope.loginStatus.mem_name } 님</c:if></p>
  <p>${sessionScope.loginStatus.mem_email}</p>
  <nav class="my-2 my-md-0 mr-md-3">
    <c:if test="${sessionScope.loginStatus == null }">
    <a class="p-2 text-dark" href="/member/login">LOGIN</a>
    </c:if>
    <c:if test="${sessionScope.loginStatus != null }">
    <a class="p-2 text-dark" href="/member/logout">LOGOUT</a>
    </c:if>
    
    <a class="p-2 text-dark" href="/member/mypage">MYPAGE</a>
    <a class="p-2 text-dark" href="#">ORDER</a>
    <a class="p-2 text-dark" href="#">CART</a>
  </nav>
  <c:if test="${sessionScope.loginStatus == null }">
  <a class="btn btn-outline-primary" href="/member/join">JOIN</a>
  </c:if>
  <c:if test="${sessionScope.loginStatus != null }">
  <a class="btn btn-outline-primary" href="/member/modify">MODIFY</a>
  </c:if>
</div>