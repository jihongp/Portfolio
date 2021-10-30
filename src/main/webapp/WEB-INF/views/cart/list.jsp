<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.80.0">
    <title>Pricing example · Bootstrap v4.6</title>

    

    

    <!-- Bootstrap core CSS, JQuery -->
<%@ include file="/WEB-INF/views/includes/common.jsp"  %>


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
  <h3 style="text-align: center;">CART</h3> 
  <table class="table table-bordered">
  <colgroup>
  	<col width="40%">
	<col width="20%">
	<col width="20%">
	<col width="20%">  	
  </colgroup>
  
  <thead>
    <tr>
      <th scope="col">제품</th>
      <th scope="col">수량</th>
      <th scope="col">가격</th>
      <th scope="col">비고</th>
    </tr>
  </thead>
  <tbody>
   <c:if test="${empty list }">
   <tr>
   	<td colspan="4">장바구니가 비어있습니다.</td>
   </tr>
   </c:if>
   
   <c:if test="${not empty list }">
   <c:forEach items="${list }" var="vo">
    <tr class="cartrow">
      <td scope="row">
      	<input type="hidden" name="cart_code" value="${vo.cart_code }" >
      	<img src="/cart/displayFile?fileName=${vo.pdt_img}" alt="">
      	${vo.pdt_name}</td>
      <td><input type="number" name="cart_amount_${vo.cart_code }" value="${vo.cart_amount}" style="width: 70px;"></td>
      <td>
      	${vo.pdt_price}
      	<input type="hidden" name="pdt_price_${vo.cart_code }" value="${vo.pdt_price }" >
      </td>
      <td>
		<input type="button" name="btnEdit" value="수정(ajax)" data-cart-code="${vo.cart_code }">
		<input type="button" name="btnEdit2" value="수정(non-ajax)" data-cart-code="${vo.cart_code }">
		<input type="button" name="btnDel" value="삭제" data-cart-code="${vo.cart_code }">
	  </td>
    </tr>
   </c:forEach>
   </c:if>
  </tbody>
</table>
<c:if test="${not empty list }">
<p id="cartTotalView">총 가격:<span id="cartTotal"></span> </p>
</c:if>
  </div>
  <c:if test="${not empty list }">
  <div class="form-group text-center" id="cartOperationView">
                <button type="button" id="btnOrder" class="btn btn-primary">
                    주문하기<i class="fa fa-check spaceLeft"></i>
                </button>
                <button type="button" id="btnCartDel" class="btn btn-warning">
                    장바구니 비우기<i class="fa fa-times spaceLeft"></i>
                </button>
            </div>
  </c:if>
  <!-- 하단 -->
  <%@ include file="/WEB-INF/views/includes/footer.jsp" %>

</div>
<script>
	$(function(){
		
    cartTotalPrice();

    //수량변경 (ajax사용)
    
    $("input[name=btnEdit]").on("click", function(){

      curBtn = $(this);
      let cart_code = $(this).data("cart-code");
      let cart_amount = $(this).parent().prev().prev().find("input[type=number]").val();

      console.log(cart_code);
      console.log(cart_amount);


      $.ajax({
        url: '/cart/edit',
        type: 'post',
        dataType: 'text',
        data: {cart_code: cart_code, cart_amount: cart_amount},
        success:function(data){
          if(data == "success"){
        	  alert("수량이 변경되었습니다.");
            // $(this)가 인식이 안된다. 전역변수로 선택한 버튼을 참조를 미리해둔다.
            //curBtn.parent().parent().remove();
            location.href = "/cart/list";
          }
        }
      });

    });
    
    // non-ajax
    $("input[name=btnEdit2]").on("click", function(){
    	
    	let cart_code = $(this).data("cart-code");
        let cart_amount = $(this).parent().prev().prev().find("input[type=number]").val();
    	
    	location.href = "/cart/edit?cart_code=" + cart_code + "&cart_amount=" + cart_amount;
    });
    
    // 개별삭제(ajax)
    let curBtn;
    $("input[name=btnDel]").on("click", function(){

        curBtn = $(this);
        let cart_code = $(this).data("cart-code");
        
        $.ajax({
          url: '/cart/delete',
          type: 'post',
          dataType: 'text',
          data: {cart_code: cart_code},
          success:function(data){
            if(data == "success"){
          	  alert("삭제가 되었습니다.");
              // $(this)가 인식이 안된다. 전역변수로 선택한 버튼을 참조를 미리해둔다.
              curBtn.parent().parent().remove();
              //totalPrice = 0;
              cartTotalPrice();

              //tr데이터 유무를 가지고 작업판단

              if($("table tr.cartrow").length == 0){

                 // console.log($("table tr.cartrow").length);

                $("#cartTotalView").hide();
                $("#cartOperationView").hide();
            
                
              }


              
            }
          }
        });

      });

	  //주문하기
      $("#btnOrder").on("click", function(){

        location.href = "/order/orderCartInfo";
      });
	  
      // 장바구니 비우기
      $("#btnCartDel").on("click", function(){

        location.href = "/cart/delete";
      });

		
	});


  // 장바구니 목록 전체금액함수
  let totalPrice = 0;
  let cartTotalPrice = function(){

    $("input[name=cart_code]").each(function(){

      let cart_code = $(this).val();
      let cart_price = $("input[name=pdt_price_" + cart_code + "]").val();
      let cart_amount = $("input[name=cart_amount_" + cart_code + "]").val();

      let unitTotalPrice = parseInt(cart_price) * parseInt(cart_amount) ;

      totalPrice += unitTotalPrice;
    });

    console.log(totalPrice);

    $("#cartTotal").text(totalPrice);

  }
</script>
    
  </body>
</html>
