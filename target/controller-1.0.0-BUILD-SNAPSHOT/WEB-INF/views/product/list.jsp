<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.80.0">
    <title>Pricing example · Bootstrap v4.6</title>

<!--     <link rel="canonical" href="https://getbootstrap.com/docs/4.6/examples/pricing/"> -->

    

    <!-- Bootstrap core CSS, JQuery -->
<%@ include file="/WEB-INF/views/includes/common.jsp" %>


<meta name="theme-color" content="#563d7c">


    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>

    

  </head>
  <body>
    
<!-- 상단메뉴 -->
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<%@ include file="/WEB-INF/views/includes/mainCategory.jsp" %>


<div class="container">
 <div class="row">
 <c:forEach items="${list }" var="vo">
 <div class="col-md-3">
          <div class="card mb-4 shadow-sm">
          <a href="${vo.pdt_num }" class="detail">
            <img src="/product/displayFile?fileName=${vo.pdt_img }" alt="이미지" height="200px">
          </a>
            <div class="card-body">
              <p class="card-text">${vo.pdt_name }</p>
              <div class="d-flex justify-content-between align-items-center">
                <div class="btn-group">
                  <button type="button" class="btn btn-sm btn-outline-secondary">buy</button>
                  <button type="button" class="btn btn-sm btn-outline-secondary">cart</button>
                </div>
                <!-- <small class="text-muted">테스트</small> -->
              </div>
            </div>
          </div>
        </div>
  </c:forEach>
  

 </div>
   <p>페이징기능 적용바람</p>
  <!-- 현재 페이징,검색정보의 목록상태에서 다른 페이지 주소(수정,삭제)로 작업을 하고, 다시 목록으로 돌아올 때 원래 출발시 페이징, 검색상태의 모습을 만들기 위하여 사용하는 정보 -->
	<form id="actionForm" action="" method="get">
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
		<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
		<input type="hidden" name="type" value="${pageMaker.cri.type }">
		<input type="hidden" name="keyword" value="${pageMaker.cri.keyword }">
	</form>

  <!-- 하단 -->
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
</div>


    
  </body>
  <script>
  	$(function(){
  		
  		let actionForm = $("#actionForm");
  		
  		$("a.detail").on("click", function(e){
  			e.preventDefault();
  			let pdt_num = $(this).attr("href");
  			//location.href = "/product/get" + pdt_num;
  			actionForm.append("<input type='hidden' name='pdt_num' value='" + pdt_num + "'>");
  			actionForm.attr("action","/product/get");
  			actionForm.submit();
  		});
  	});
  </script>
  
  
</html>
