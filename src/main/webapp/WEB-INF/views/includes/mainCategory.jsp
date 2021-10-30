<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 1차 카테고리 -->
<div style="text-align: center;">
<ul class="nav" id="mainCategory">
	<c:forEach items="${globalMainCategory }" var="category">
	<li class="nav-item">
	 <a class="nav-link main" href="${category.cate_code_pk}">${category.cate_name}</a>
 	</li>
 	</c:forEach>
</ul>
<!--  2차 카테고리 -->
<ul class="nav" id="subCategory" style="height: 40px;"></ul>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.3/handlebars.js"></script>
<script>
  $(function () {
	  
	  let actionForm = $("#actionForm"); // 페이징, 검색정보를 가지고 있는 폼
	  
	  // 1차 카테고리 선택.  on/off, one(이벤트가 단1회만 허용)
	  $("#mainCategory li a.main").on("mouseover click", function(e){
			
      		e.preventDefault();
			// 1차카테고리를 참조하는 2차카테고리를, ajax를 이용하여 서버로 응답받는 작업
			let mainCategory = $(this).attr("href");
			let url = "/product/subCategory/" + mainCategory;

			$.getJSON(url, function(data){

				// 2차카테고리 data를 가지고 작업
				//console.log(data[0]);
				//$("#subCategory").empty();
				subCategoryView(data, $("#subCategory"), $("#subCateTemplate"));
			});
			
		});
		
	    // 2차 카테고리 선택. 동적으로 추가된 이벤트 연결작업
		$("#subCategory").on("click", "li a.sub",  function(e){
			
      		e.preventDefault();
      		
      		let subCategory = $(this).attr("href");
      		
      		console.log(subCategory);
      		
      		location.href = "/product/list?subCategory=" + subCategory;
      			
      		
		});
  })
</script>

<script id="subCateTemplate" type="text/x-handlebars-template">
{{#each .}}
<li class="nav-item" value="{{cate_code_pk}}">
	<a class="nav-link sub" href="{{cate_code_pk}}">{{cate_name}}</a>
</li>
{{/each}}
</script>

<script>
	let subCategoryView = function(subCategory, target, template){

	let templateObj = Handlebars.compile(template.html());
	let options = templateObj(subCategory);
	$("#subCategory").empty(); // 기존것 제거하기.
	target.append(options);
	}

</script>