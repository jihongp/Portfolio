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
    <meta name="theme-color" content="#563d7c">
    <title>Pricing example · Bootstrap v4.6</title>

<!--     <link rel="canonical" href="https://getbootstrap.com/docs/4.6/examples/pricing/"> -->

    <!-- Bootstrap core CSS, JQuery -->
<%@ include file="/WEB-INF/views/includes/common.jsp" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>

<script id="reviewListTemplate" type="text/x-handlebars-template">
	<h5>Review List</h5>
	{{#each .}}
		<div class="reply-row">
		<b>No.<span class="reply-rno">{{rv_num}}</span></b><strong><p class='rv_score'>{{checkRating rv_score}}</p></strong>
		<br>
		<b class="reply-replyer">{{mem_id}}</b> 작성일 : <b>{{prettifyDate rv_date_reg}}</b><br>
		<span class="reply-reply">{{rv_content}}</span>
		<button class="btnModalEdit" data-rv_num="{{rv_num}}" data-rv_score="{{rv_score}}">수정</button>
    	<button class="btnModalDel"  data-rv_num="{{rv_num}}" data-rv_score="{{rv_score}}">삭제</button>
		<hr>
		</div>
	{{/each}}
</script>
<script>
	

	// 상품후기목록 출력작업
	// replyArr : 댓글데이터를 받을 파라미터, target : 댓글목록이 삽입될 위치 , templateObj : 템플릿을 참조할 파라미터
	let printData = function(replyArr, target, templateObj) {
		
		console.log("printData");

		
		let template = Handlebars.compile(templateObj.html());
		let html = template(replyArr);
		target.empty(); // 기존상품후기목록 지우기 
		target.append(html);
		
	}
	
	//페이징 출력작업 pagenation
	let printPaging = function(pageMaker, target){
		let str = "";

		if(pageMaker.prev){
			str += "<li class='page-item'><a class='page-link' href='" + (pageMaker.startPage - 1) + "'>[prev]</a></li>";
		}

		// 1 2 3 4 5
		for(let i=pageMaker.startPage, len = pageMaker.endPage; i <= len; i++){
			let strClass = pageMaker.cri.pageNum == i ? 'active' : '';
			str += "<li class='page-item " + strClass + "'><a class='page-link' href='" + i + "'>" + i + "</a></li>";
		}

		if(pageMaker.next){
			str += "<li class='page-item'><a class='page-link' href='" + (pageMaker.endPage + 1) + "'>[next]</a></li>";
		}


		target.html(str);
	}

	
	// /review/상품코드/페이지 
	let pdt_num = ${vo.pdt_num }; // 상품코드
	let page = 1; // 클릭한 댓글 페이지번호.  상품후기 수정, 삭제시 현재 페이지번호의 목록상태로 돌아갈려면 이변수를 사용해야한다.
	let pageInfo = "/review/" + pdt_num + "/" + page;
	getPage(pageInfo);

	//상품후기 목록과 페이징 정보를 요청하는 함수.
	function getPage(pageInfo){
		// 댓글정보와 페이징정보를 json으로 받아오는 구문
		$.getJSON(pageInfo, function(data){
			// alert(data.List.length);

		 	printData(data.list, $("#reviewListView"), $("#reviewListTemplate"));
			printPaging(data.pageMaker, $("#pagination"));

		});

	}
	
	
</script>

<script>
	$(function(){
		
    $("#star_grade a").click(function(e){
      e.preventDefault();
      // 별 5개를 on 선택자 모두제거(클릭시 on선택자를 모두 제거하는 초기화)
      $(this).parent().children("a").removeClass("on");
      
      $(this).addClass("on").prevAll("a").addClass("on");
    })

    

    //상품후기등록 클릭시.
    $("#btnReview").on("click", function(){

      let rv_score = 0;
      let rv_content = $("#reviewContent").val();

      $("#star_grade a").each(function(){

        if($(this).attr('class') == 'on'){
          rv_score += 1;
        }
      });

      //console.log(rv_score);

      if(rv_score == 0){
        alert("별점 선택해주세요");
        return;
      }else if(rv_content == ""){
        alert("상품후기 입력해주세요");
        return;
      }

      $.ajax({
        url :'/review/write',
        type:'post',
        dataType:'text',
        data : {rv_score : rv_score, rv_content : rv_content, pdt_num : ${vo.pdt_num }},
        success: function(data){
        	if(data == "success"){
            	alert("상품후기 등록완료");
        		getPage(pageInfo); // /review/상품코드/1
            	// 상품후기 리스트 작업
        }
        }
      });

    });

    // 별점 변경시 클릭작업
    var scoreState = function(){
      let point = 0;

      $("#star_grade_modal a").each(function(){
        
        if($(this).attr('class') == 'on'){
          point += 1;

        }
      });

      $("#rv_score").val(point); //   모달대화상자의 평점점수 hidden태그에 값을 할당
    }




      // 모달 대화상자 작업
    // 동적인 태그를 대상으로 하는 이벤트 설정
    $("#star_grade_modal a").click(function(e){
      e.preventDefault();
      // 별 5개를 on 선택자 모두제거(클릭시 on선택자를 모두 제거하는 초기화)
      $(this).parent().children("a").removeClass("on");
      
      $(this).addClass("on").prevAll("a").addClass("on");
      scoreState();
    })

    //상품 후기 대화상자가 표시되면서, 선택한 상품후기에 대한정보를 대화상자로 가져와야한다.
    //상품후기 글번호(pk), 상품후기내용, 상품별 점수를 이용하여, 별점 표시작업, 상품후기 작성자 표시
    $("#reviewListView").on("click", ".btnModalEdit", function(){

      $("#modalMethod").html("상품후기수정");
      $("button[data-modal=btnCommon]").hide();
      $("#btnReviewModify").show();
      $("#reviewModal").modal('show');

        let rv_num = $(this).data("rv_num"); // 글번호
        $("#rv_num").val(rv_num);

        let rv_content = $(this).prev().html(); // 후기내용
        $("#rv_content").val(rv_content);

        let mem_id = $(this).parent().find(".reply-replyer").html(); // 작성자
        $("#mem_id").val(mem_id);

        let score = $(this).data("rv_score"); // 별점점수
        $("#rv_score").val(score);

        /*
        console.log("글번호: " + rv_num);
        console.log("글후기: " + rv_content);
        console.log("작성자: " + mem_id);
        console.log("별점: " + rv_score);
        */
        // 별점점수를 이용하여, 별점상태 표시작업
        $("#star_grade_modal a").each(function(index, item){
          console.log(index<score);
          if(index<score){
            $(item).addClass("on"); //별표시 선택자 추가작업
          }else{
            $(item).removeClass("on"); // 별표시 선택자 제거작업
          }
        })
        
        

   });

   //모달대화상자에서 수정클릭시
   $("#btnReviewModify").on("click", function(){

    //수정 ajax구문작업
    $.ajax({
      url:'/review/modify',
      data: {rv_num : $("#rv_num").val(), rv_content : $("#rv_content").val(), rv_score : $("#rv_score").val()},
      type:'post',
      dataType:'text',
      success: function(data) {
        if(data == "success"){
       	  $("#reviewModal").modal('hide');
          alert("수정이 완료됨");
  		getPage("/review/" + ${vo.pdt_num} + "/" + page);
        }
      }

    });

   });


   // 상품후기목록에서 삭제버튼클릭시 삭제대화상자보기
   $("#reviewListView").on("click", ".btnModalDel", function(){
    
    $("#modalMethod").html("상품후기삭제");
      $("button[data-modal=btnCommon]").hide();
      $("#btnReviewDelete").show();
      $("#reviewModal").modal('show');

      let rv_num = $(this).data("rv_num"); // 글번호
        $("#rv_num").val(rv_num);

        let rv_content = $(this).prev().prev().html(); // 후기내용
        $("#rv_content").val(rv_content);

        let mem_id = $(this).parent().find(".reply-replyer").html(); // 작성자
        $("#mem_id").val(mem_id);

        let score = $(this).data("rv_score"); // 별점점수
        $("#rv_score").val(score);

        /*
        console.log("글번호: " + rv_num);
        console.log("글후기: " + rv_content);
        console.log("작성자: " + mem_id);
        console.log("별점: " + rv_score);
        */
        // 별점점수를 이용하여, 별점상태 표시작업
        $("#star_grade_modal a").each(function(index, item){
          console.log(index<score);
          if(index<score){
            $(item).addClass("on"); //별표시 선택자 추가작업
          }else{
            $(item).removeClass("on"); // 별표시 선택자 제거작업
          }
        })

   	  });
   
    //삭제대화상자에서 삭제클릭시
    $("#btnReviewDelete").on("click", function(){
      //삭제 ajax구문작업
      $.ajax({
        url:'/review/delete',
        type:'post',
        data: {rv_num : $("#rv_num").val()},
        dataType:'text',
        success: function(data) {
          if(data == "success"){
        	$("#reviewModal").modal('hide');
            alert("삭제가 완료됨");
    		getPage("/review/" + ${vo.pdt_num} + "/" + page);
          }
        }

      });
    });
    
 // 상품후기 페이지 번호 클릭작업. 동적태그로 구성된 태그에 대하여, 이벤트 설정하는 기능을 제공
	$(".pagination").on("click", "li a", function(e){
		e.preventDefault();
		//console.log("댓글페이지번호 클릭");

		page = $(this).attr("href");

		getPage("/review/" + ${vo.pdt_num} + "/" + page);
	});
   	
});

  


</script>




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
      
      /* 상품후기 별평점 스타일이 적용됨 */
      #star_grade a {
      	font-size: 22px;
      	text-decoration: none;
      	color: lightgray;
      }
      
      /* 클릭 이벤트설정에 의하여  적용 */
      #star_grade a.on{
      	color: black;
      }


      #star_grade_modal a {
      	font-size: 22px;
      	text-decoration: none;
      	color: lightgray;
      }
      
      /* 클릭 이벤트설정에 의하여  적용 */
      #star_grade_modal a.on{
      	color: black;
      }
      
    </style>

    

  </head>
  <body>
    
<!-- 상단메뉴 -->
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<%@ include file="/WEB-INF/views/includes/mainCategory.jsp" %>


<div class="container">
 <div class="row">
 	<div class="col-md-12">
      <div class="row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
        <div class="col p-4 d-flex flex-column position-static">
          <strong class="d-inline-block mb-2 text-primary">${vo.pdt_name }</strong>
          <b>가격 : ${vo.pdt_price }</b><br>
          <b>주문수량 : <input type="number" id="cartAmount" value="1"></b><br>
          <b>제조사 : ${vo.pdt_company }</b><br>
          <button type="button" id="btnDirectOrder" class="btn btn-primary" style="width: 100px;">구매하기</button>
          <button type="button" id="btnCartAdd" class="btn btn-primary" style="width: 100px;">장바구니</button>
        </div>
        <div class="col-auto d-none d-lg-block">
          <img src="/product/displayFile?fileName=${vo.pdt_img }" class="bd-placeholder-img" width="200" height="250" alt="">

        </div>
    </div>
 	</div>
 </div>
 <div class="row">
  <div class="col-md-12">
   ${vo.pdt_detail }
  </div>
 </div>
 
 
 <!-- 상품후기작업  -->
 <div>
 	<label for="review">Review</label><br>
 	<div class="rating">
 		<p id="star_grade">
 			<a href="#">★</a>
 			<a href="#">★</a>
 			<a href="#">★</a>
 			<a href="#">★</a>
 			<a href="#">★</a>
 		</p>
 	</div>
 	<textarea id="reviewContent" rows="3" style="width:100%;"></textarea><br>
 	<button id="btnReview">상품후기등록</button>
 </div>
 <!-- 상품후기 목록위치 -->
 <div class="row">
  <div class="col-md-12" id="reviewListView">
   	
  </div>
  
 </div>
 <!-- 페이징 출력위치 -->
 <div class="row">
 	<div class="col-md-12">
 		<ul id="pagination" class="pagination"></ul>
 	</div>
 </div>
  

  <!-- 하단 -->
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
</div>

<script>

  $(function(){

    // 장바구니 추가작업
    $("#btnCartAdd").on("click", function(){

      let pdt_num = ${vo.pdt_num };   // EL문법
      let cart_amount = $("#cartAmount").val();  // jQuery() -> $()

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
    $("#btnDirectOrder").on("click", function(){

      let pdt_num = $(this).data("pdt_num");   // EL문법
      
      location.href = "/order/orderInfo?pdt_num=" + ${vo.pdt_num};

    });
  });
  
//함수가 작성된 위치에 따라서 동작이 안되는 경우가 나오게 됨(주의사항)

Handlebars.registerHelper("prettifyDate", function(timeValue){
	let dateObj = new Date(timeValue);
	let year = dateObj.getFullYear();
	let month = dateObj.getMonth() + 1;
	let date = dateObj.getDate();
	
	return year + "/" + month + "/" + date;

});

Handlebars.registerHelper("checkRating", function(rv_score){
	
  let star = "";
  switch(rv_score){
    case 1:
      star = "★☆☆☆☆"; // ★☆☆☆☆
      break;
    case 2:
      star = "★★☆☆☆";
      break;
    case 3:
      star = "★★★☆☆";
      break;
    case 4:
      star = "★★★★☆";
      break;
    case 5:
      star = "★★★★★";
      break;

  }

  return star;

});

</script>
<!-- 상품후기수정, 삭제 모달대화상자. -->
<div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalMethod">상품후기 수정</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <label for="replytext" class="col-form-label">Review:</label>
            NO. <input type="text" id="rv_num" readonly="readonly">
            <p id="star_grade_modal">
              <a href="#">★</a>
              <a href="#">★</a>
              <a href="#">★</a>
              <a href="#">★</a>
              <a href="#">★</a>
            </p>
            <input type="hidden" id="rv_score">
            <textarea id="rv_content" rows="3" style="width:100%;"></textarea>
						
          </div>
          <div class="form-group">
            <label for="replyer" class="col-form-label">Replyer:</label>
            <input type="text" class="form-control" id="mem_id" readonly="readonly">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <!-- <button data-modal="btnCommon" type="button" class="btn btn-primary" id="btnReviewSave">Save</button>  -->
		<button data-modal="btnCommon" type="button" class="btn btn-info" id="btnReviewModify">Modify</button>
		<button data-modal="btnCommon" type="button" class="btn btn-danger" id="btnReviewDelete">Delete</button>
      </div>
    </div>
  </div>
</div>

    
  </body>
</html>
