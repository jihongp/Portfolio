<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<script id="userOrderDetailTemplate" type="text/x-handlebars-template">
  {{#each.}}
  <tr class="addOrderDetail">
    <td scope="row">
      <a href="${pdt_num }" class="detail">
        <img src="/product/displayFile?fileName={{pdt_img }}" alt="이미지" height="200px">
      </a>
      상품명{{pdt_num}}<br>
      가격{{odr_price}}<br>
      수량{{odr_amount}}
    </td>
    <td>테스트1</td>
    <td>테스트2</td>
    <td>테스트3</td>     
  </tr>
  {{/each}}
</script>
<script>

  $(function(){


    // 주문번호 선택한 행을 가리키는 tr태그를 참조.
    $("table td span.userOrderDetail").on("click", function(){
      let cur_tr = "";
      let odr_code = $(this).data("odr_code");

      console.log(odr_code);
      cur_tr = $(this).parent().parent();

      $.ajax({
        url:'/order/userOrderDetailInfo/' + odr_code + ".json",
        //data: {odr_code : odr_code},
        type: 'get',
        success: function(data){
        	
          //console.log(data[0].pdt_num);
          fn_userOrderDetail(data, cur_tr, $("#userOrderDetailTemplate"));
        }
      });

    });
  
  });

  let fn_userOrderDetail = function(userOrderDetails, target, templateObj) {
		

		
		let template = Handlebars.compile(templateObj.html());
		let html = template(userOrderDetails);

    console.log(html);

    target.siblings(".addOrderDetail").remove();

		target.after(html);
		
	}


</script>


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
<h3>주문내역</h3>
<form action="/order/orderProcess" method="post">
 <table class="table table-striped">
  <thead>
    <tr>
      <th scope="col">상품정보(주문번호)</th>
      <th scope="col">주문일</th>
      <th scope="col">주문금액</th>
      <th scope="col">주문상태</th>
    </tr>
  </thead>
  <tbody>
  
  	<c:forEach items="${list }" var="vo">
  	<!-- item은 ordercontroller에 model구문 리스트 이고 var는 ordervo다. -->
    <tr>
      <td scope="row">주문번호 : <c:out value="${vo.odr_code }" />  <c:out value="${vo.odr_count }" /> 건<span class="userOrderDetail" data-odr_code="${vo.odr_code }" style="cursor:pointer;">[상세보기]</span></td>
      <td><fmt:formatDate value="${vo.odr_date }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
 	  <td><c:out value="${vo.odr_total_price }" /></td>
 	  <td>배송준비중</td>     
    </tr>
    </c:forEach>
  </tbody>
 </table>
 

 
</form>
 
  <!-- 하단 -->
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
</div>



    
  </body>
</html>
