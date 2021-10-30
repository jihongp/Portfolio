<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>AdminLTE 2 | Starter</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  
  <%@ include file="/WEB-INF/views/admin/includes/config.jsp" %>
  <script>
  	let msg = "${msg}";
  	if (msg == "insertOk"){
  		alert("상품등록이 되었습니다.");
  	}else if (msg == "editOk"){
  		alert("상품수정이 되었습니다.");
  	}else if (msg == "deleteOk"){
  		alert("상품삭제가 되었습니다.");
  	}
  </script>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <!-- Main Header -->
  <%@ include file="/WEB-INF/views/admin/includes/top.jsp" %>
  
  <!-- Left side column. contains the logo and sidebar -->
  <%@ include file="/WEB-INF/views/admin/includes/nav.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        Page Header
        <small>Optional description</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content container-fluid">

	<h3>상품목록</h3>
	
      <!--------------------------
        | 기능에 따른 실제 내용			 |
        -------------------------->
	<div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
		
		<!-- 검색기능 화면 출력 -->
					<div class="float-left">
					<div class="row">
						<div class="col-lg-12">
							<form id="searchForm" action="/admin/product/list" method="get">
								<select name="type">
									<option value="" <c:out value="${pageMaker.cri.type == null ? 'selected' : '' }" />>---</option>
									<option value="N" <c:out value="${pageMaker.cri.type eq 'N' ? 'selected' : '' }" />>상품명</option>
									<option value="D" <c:out value="${pageMaker.cri.type eq 'D' ? 'selected' : '' }" />>내용</option>
									<option value="C" <c:out value="${pageMaker.cri.type eq 'C' ? 'selected' : '' }" />>제조사</option>
									<option value="ND" <c:out value="${pageMaker.cri.type eq 'ND' ? 'selected' : '' }" />>상품명 or 내용</option>
									<option value="NC" <c:out value="${pageMaker.cri.type eq 'NC' ? 'selected' : '' }" />>상품명 or 제조사</option>
									<option value="NDC" <c:out value="${pageMaker.cri.type eq 'NDC' ? 'selected' : '' }" />>상품명 or 내용 or 제조사</option>
								</select>
								<input type="text" name="keyword" value="<c:out value="${pageMaker.cri.keyword }" />">
								<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
								<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
								<button type="submit" class="btn btn-default">검색</button>
							</form>
						</div>

					</div>
					</div>
		
		<div class="row"><div class="col-sm-6"><button type="button" id="btnChkDel">선택삭제</button></div>
			<div class="col-sm-6">
			</div>
			<div class="col-sm-6">
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
			       <thead>
				       <tr role="row">
					       <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">
					       		<input type="checkbox" id="checkAll">
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">
					       	상품코드
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">
					       	상품명
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">
					       	등록일
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">
					       	가격
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">
					       	재고
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">
					       	진열
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">
					       	수정
					       </th>
					       <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">
					       	삭제
					       </th>
				       </tr>
			       </thead>
			       <tbody> 
			       <c:forEach items="${list }" var="vo">
				       <tr role="row" class="odd">
				         <td class="sorting_1">
				         	<input type="checkbox" class="check" value="<c:out value="${vo.pdt_num }" />">
				         	<input type="hidden" class="pdt_img" value="<c:out value="${vo.pdt_img }" />">
				         </td>
				         <td>[<c:out value="${vo.pdt_num }" />]</td>
				         <td><img src="/admin/product/displayFile?fileName=<c:out value="${vo.pdt_img }" />" alt=""><c:out value="${vo.pdt_name }" /></td>
				         <td><fmt:formatDate value="${vo.pdt_date_up }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				         <td><input type="text" value='<c:out value="${vo.pdt_price }" />'></td>
				         <td><input type="text" value='<c:out value="${vo.pdt_amount }" />'></td>
				         <td><input type="checkbox" <c:out value="${vo.pdt_buy == 'Y' ? 'checked':''}" />></td>
				         <td><input type="button" value="수정" class="btnEdit" data-pdt-num="<c:out value="${vo.pdt_num }" />"></td>
				         <td><input type="button" value="삭제" class="btnDelete" data-pdt-num="<c:out value="${vo.pdt_num }" />"></td>
				       </tr>
				   </c:forEach>
				       <!-- 
				       <tr role="row" class="even">
				         <td class="sorting_1">Gecko</td>
				         <td>Firefox 1.5</td>
				         <td>Win 98+ / OSX.2+</td>
				         <td>1.8</td>
				         <td>A</td>
				       </tr>
				        -->
				   </tbody>
			     </table>
			  
		     </div>
	     </div>
		 <div class="row">
	     	<div class="col-sm-5">
	     	<div class="dataTables_info" id="example2_info" role="status" aria-live="polite">Showing 1 to 10 of 57 entries</div>
	     	</div>
		    <div class="col-sm-7">
			    <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
			    <ul class="pagination">
			    
			    	<c:if test="${pageMaker.prev }">
				    	<li class="paginate_button previous" id="example2_previous">
				    		<a data-pagenum="${pageMaker.startPage - 1 }" class="page-link btnPagePrev" href="${pageMaker.startPage - 1}" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a>
				    	</li>
				    </c:if>
				    
				    <c:forEach var="num" begin="${pageMaker.startPage }" end="${pageMaker.endPage }">
					    <li class="paginate_button ${pageMaker.cri.pageNum == num ? "active" : ""} " >
					    	<a data-pagenum="${num }" class="page-link btnPageNum" href="${num}" aria-controls="example2" data-dt-idx="1" tabindex="0">${num }</a>
					    </li>
				    </c:forEach>
				  
				  	<c:if test="${pageMaker.next }">
					    <li class="paginate_button next" id="example2_next">
					    	<a class="page-link btnPageNext" href="${pageMaker.endPage + 1}" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a>
					    </li>
				    </c:if>
			    </ul>
				</div>

           <!-- 현재 페이징,검색정보의 목록상태에서 다른 페이지 주소(수정,삭제)로 작업을 하고, 다시 목록으로 돌아올 때 원래 출발시 페이징, 검색상태의 모습을 만들기 위하여 사용하는 정보 -->
				<form id="actionForm" action="" method="get">
					<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
					<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
					<input type="hidden" name="type" value='<c:out value="${pageMaker.cri.type }" />'>
					<input type="hidden" name="keyword" value='<c:out value="${pageMaker.cri.keyword }" />'>
				</form>

	    	</div>
	    </div>
    </div>

    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

  <!-- Main Footer -->
  <%@ include file="/WEB-INF/views/admin/includes/bottom.jsp" %>

<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->

<%@ include file="/WEB-INF/views/admin/includes/common.jsp" %>

<script>
  // $(document).ready(funtion(){});
  $(function(){

	let actionForm = $("#actionForm");
	  
	// 상품수정폼 페이지 
    $(".btnEdit").on("click",function(){

      let pdt_num = $(this).data("pdt-num");

      console.log("edit: " + pdt_num);
      
      // 브라우저- 검사로 폼정보 태그를 확인하자.
      actionForm.append("<input type='hidden' name='pdt_num' value='" + pdt_num + "'>");
      actionForm.attr("action", "/admin/product/edit"); // /admin/product/list -> /admin/product/edit 주소변경
	 	  actionForm.submit();
      
    });
		
		// 상품삭제
    $(".btnDelete").on("click",function(){

			let pdt_num = $(this).data("pdt-num");

      if(confirm("상품코드 " + pdt_num + "를 삭제하겠습니까? ")){

      console.log("delete: " + pdt_num);
      
      // 브라우저- 검사로 폼정보 태그를 확인하자.
      	actionForm.append("<input type='hidden' name='pdt_num' value='" + pdt_num + "'>");
	  	actionForm.attr("method", "post");
      	actionForm.attr("action", "/admin/product/delete"); // /admin/product/list -> /admin/product/edit 주소변경
	  	actionForm.submit();
      }
    });
    	let isCheck = true;
		// attr(), prop() : 속성을 제어하는 기능
		//전체선택 checkbox
		$("#checkAll").on("click", function(){
			$(".check").prop("checked", this.checked);
			
			isCheck = this.checked;
		});

		//개별선택 checkbox
		$(".check").on("click", function(){
			$("#checkAll").prop("checked", false);
			$(".check").each(function(){
				if(!$(this).checked){
					isCheck = false
				}
			});

			if(isCheck) $("#checkAll").prop("checked", isCheck);
		});

		// 선택삭제 버튼 클릭시
		$("#btnChkDel").on("click", function(){
			if($(".check:checked").length == 0){
				alert("삭제할 상품을 선택해주세요.");
				return;
			};


			var result = confirm("선택한 상품을 삭제하시겠습니까?");
		if(result){

			// 선택한 상품코드, 이미지 정보를 배열로 저장
			let pdtnumArr = [];
			let imgArr = [];

			// 삭제하고자 하는 선택된 체크박스의 개수.(상품)
			$(".check:checked").each(function(){
				let pdt_num = $(this).val();
				let pdt_img = $(this).next().val();

				pdtnumArr.push(pdt_num);
				imgArr.push(pdt_img);

			
			});

			console.log("선택상품: " + pdtnumArr);
			console.log("선택상품이미지: " + imgArr);

			$.ajax({
				url: '/admin/product/deleteChecked', // 선택된상품삭제주소
				type: 'post',
				dataType: 'text', // ''를 쓰든 ""를 쓰든 상관없다.
				data: {
					pdtnumArr : pdtnumArr, // 배열
					imgArr : imgArr // 배열. 왼쪽에 있는게 스프링으로 넘어가는 데이터 . 왼쪽이 파라미터 오른쪽이 값.
				},
				success : function(data){
					alert("선택한 상품이 삭제가 되었습니다.");

					// 폼전송
				actionForm.attr("action", "/admin/product/list"); 
				actionForm.submit();
				}

			});

		}
		});
		
		// 이전, 다음, 페이지 번호 클릭시
		// 페이지번호 클릭 작업 prev 1 2 3 4 5 next
		$(".paginate_button a").on("click", function(e){

			e.preventDefault(); // <a>태그의 링크기능을 비활성화

				actionForm.find("input[name='pageNum']").val($(this).attr("href")); // 사용자가 선택한 페이지 번호 변경
				actionForm.submit();

			console.log("click");
		});

		
  });
</script>

</body>
</html>