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
<script>
  
  $(function(){

	    // 장바구니 추가작업
	    $(".btnCartAdd").on("click", function(){

	      let pdt_num = $(this).data("pdt_num");   // EL문법
	      let cart_amount = 1;  // jQuery() -> $()

	      $.ajax({
	        url : '/cart/cartAdd',
	        type: 'post',
	        dataType: 'text',
	        data: {pdt_num : pdt_num, cart_amount : cart_amount},
	        success: function(data){
	          let result = confirm("장바구니에 추가되었습니다.\n 지금 확인하시겠습니까?");
	          if(result){
	            location.href = "/cart/list"; // 장바구니 목록
	          }
	        }
	      });

	    });

	    // 바로구매 추가작업
	    $(".btnDirectOrder").on("click", function(){

	      let pdt_num = $(this).data("pdt_num");   // EL문법
	      
	      location.href = "/order/orderInfo?pdt_num=" + pdt_num;

	    });


	  });


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
                  <button type="button" class="btn btn-sm btn-outline-secondary btnDirectOrder" data-pdt_num="${vo.pdt_num }">buy</button>
                  <button type="button" class="btn btn-sm btn-outline-secondary btnCartAdd" data-pdt_num="${vo.pdt_num }">cart</button>
                </div>
                <!-- <small class="text-muted">테스트</small> -->
              </div>
            </div>
          </div>
        </div>
  </c:forEach>
  

 </div>
   <!-- 페이징기능 -->
   <div class="row">
  	<div class="col-sm-5">
		<div class="dataTables_info" id="example2_info" role="status" aria-live="polite">&nbsp;</div>
		</div>
		<div class="col-sm-7">
		<div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
		<ul class="pagination">
		
		
		<c:if test="${pageMaker.prev }">
			<li class="paginate_button previous" id="example2_previous">
				<a data-pagenum="${pageMaker.startPage - 1 }" class="page-link btnPagePrev" href="${pageMaker.startPage-1}" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a>
			</li>
		</c:if>
		
		<c:forEach var="num" begin="${pageMaker.startPage }" end="${pageMaker.endPage }">
			<li class="paginate_button ${pageMaker.cri.pageNum == num ? "active" : ""} "><a data-pagenum="${num }" href="${num}" aria-controls="example2" data-dt-idx="1" tabindex="0">${num }</a></li>
		</c:forEach>
		
		<c:if test="${pageMaker.next }">
			<li class="paginate_button next" id="example2_next"><a class="page-link btnPageNext" href="${pageMaker.endPage+1}" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>
		</c:if>
		</ul>
		</div>
  </div>
   
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
</div>


    

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
  		
  		// 이전, 다음, 페이지번호 클릭시
		//페이지번호 클릭 작업  prev 1 2 3 4 5 next
		 $(".paginate_button  a").on("click", function(e){

			e.preventDefault(); // <a>태그의 링크기능을 비활성화
			actionForm.append("<input type='hidden' name='subCategory' value='" + ${subCategory}  +"'>" );
			actionForm.find("input[name='pageNum']").val($(this).attr("href"));  // 사용자가 선택한 페이지번호 변경
			actionForm.attr("action", "/product/list");
			actionForm.submit();	

			//console.log("click");
		 });
  		
  	});
  </script>
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // 우편번호 찾기 화면을 넣을 element
    var element_layer = document.getElementById('layer');

    function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function sample2_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    document.getElementById("sample2_extraAddress").value = extraAddr;
                
                } else {
                    document.getElementById("sample2_extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample2_postcode').value = data.zonecode;
                document.getElementById("sample2_address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("sample2_detailAddress").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_layer.style.display = 'none';
            },
            width : '100%',
            height : '100%',
            maxSuggestItems : 5
        }).embed(element_layer);

        // iframe을 넣은 element를 보이게 한다.
        element_layer.style.display = 'block';

        // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
        initLayerPosition();
    }

    // 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
    // resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
    // 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
    function initLayerPosition(){
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        // 위에서 선언한 값들을 실제 element에 넣는다.
        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';
        // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
    }
</script>
  
    </body>
  
  
</html>
