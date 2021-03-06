<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  
</head>
<!--
BODY TAG OPTIONS:
=================
Apply one or more of the following classes to get the
desired effect
|---------------------------------------------------------|
| SKINS         | skin-blue                               |
|               | skin-black                              |
|               | skin-purple                             |
|               | skin-yellow                             |
|               | skin-red                                |
|               | skin-green                              |
|---------------------------------------------------------|
|LAYOUT OPTIONS | fixed                                   |
|               | layout-boxed                            |
|               | layout-top-nav                          |
|               | sidebar-collapse                        |
|               | sidebar-mini                            |
|---------------------------------------------------------|
-->
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
        Admin Page
        <small>Product Insert</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> ????????????</a></li>
        <li class="active">?????? ??????</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content container-fluid">

      <!-- ???????????? -->
        
        <div class="box box-primary">
            <div class="box-header with-border">
              <h3 class="box-title">Quick Example</h3>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form role="form" id="productForm" action="/admin/product/edit" method="post" enctype="multipart/form-data">
              <div class="box-body">
              <div class="row">
	              <div class="col-md-6">
		              <div class="form-group">
		              <input type="hidden" name="pageNum" value="${cri.pageNum}">
					  <input type="hidden" name="amount" value="${cri.amount}">
					  <input type="hidden" name="type" value="${cri.type}">
					  <input type="hidden" name="keyword" value="${cri.keyword}">
		              
		                <label>1??? ????????????</label>
		                <select id="mainCategory" name="cate_code_prt" class="form-control select2 select2-hidden-accessible" style="width: 100%;">
		                  <option selected="selected">1??????????????? ??????</option>
		                  <c:forEach items="${mainCategory }" var="vo">
		                  <option value="${vo.cate_code_pk }" <c:out value="${vo.cate_code_pk == productVO.cate_code_prt ? 'selected' :'' }" />>${vo.cate_name }</option>
		                  </c:forEach>
		                </select>
		              </div>
	              </div>
	              <div class="col-md-6">
		              <div class="form-group">
		                <label>2???????????????</label>
		                <select id="subCategory" name="cate_code" class="form-control select2 select2-hidden-accessible" style="width: 100%;">
		                  <option selected="selected">2??? ???????????? ??????</option>
		                  <c:forEach items="${subCategory }" var="vo">
		                  <option value="${vo.cate_code_pk }" <c:out value="${vo.cate_code_pk == productVO.cate_code ? 'selected' :'' }" />>${vo.cate_name }</option>
		                  </c:forEach>
		                </select>
		              </div>
	              </div>
              </div>
              <div class="row">
              	<div class="col-md-6">
	              	<div class="form-group">
	                  <label for="pdt_name">?????????</label>
	                  <input type="text" id="pdt_name" name="pdt_name" class="form-control" id="exampleInputEmail1" value='<c:out value="${productVO.pdt_name}" />'>
	                  <input type="hidden" id="pdt_num" name="pdt_num" class="form-control" id="exampleInputEmail1" value='<c:out value="${productVO.pdt_num}" />'>
	                </div>
	            </div>
	            <div class="col-md-6">
	                <div class="form-group">
	                  <label for="pdt_company">?????????</label>
	                  <input type="text" id="pdt_company" name="pdt_company" class="form-control" id="exampleInputEmail1" value='<c:out value="${productVO.pdt_company}" />'>
	                </div>
	          	</div>
              </div>
                <div class="form-group">
                  <label for="pdt_detail">?????? ????????????</label>
                  <div class="box box-info">
		            <div class="box-header">
		              <h3 class="box-title">CK Editor
		                <small>Advanced and full of features</small>
		              </h3>
		              <!-- tools box -->
		              <div class="pull-right box-tools">
		                <button type="button" class="btn btn-info btn-sm" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse">
		                  <i class="fa fa-minus"></i></button>
		                <button type="button" class="btn btn-info btn-sm" data-widget="remove" data-toggle="tooltip" title="" data-original-title="Remove">
		                  <i class="fa fa-times"></i></button>
		              </div>
		              <!-- /. tools -->
		            </div>
		            <!-- /.box-header -->
		            <div class="box-body pad">
		                    <textarea id="pdt_detail" name="pdt_detail" rows="10" cols="80" style="visibility: hidden; display: none;"><c:out value="${productVO.pdt_detail}" /></textarea>
		            </div>
	           	  </div>
				</div>
				<div class="row">
					<div class="col-md-3">
		                <div class="form-group">
		                  <label for="pdt_price">????????????</label>
		                  <input type="text" id="pdt_price" name="pdt_price" class="form-control" id="exampleInputEmail1" value='<c:out value="${productVO.pdt_price}" />'>
		                </div>
		            </div>
		            <div class="col-md-3">
		                <div class="form-group">
		                  <label for="pdt_discount">??????</label>
		                  <input type="text" id="pdt_discount" name="pdt_discount" class="form-control" id="exampleInputEmail1" value='<c:out value="${productVO.pdt_discount}" />'>
		                </div>
		            </div>
		            <div class="col-md-3">
		                <div class="form-group">
		                  <label for="pdt_amount">??????</label>
		                  <input type="text" id="pdt_amount" name="pdt_amount" class="form-control" id="exampleInputEmail1" value='<c:out value="${productVO.pdt_amount}" />'>
		                </div>
		            </div>
		            <div class="col-md-3">
		            	<div class="form-group">
		                <label for="pdt_buy">????????????</label>
		                  <div class="checkbox">
		                    <label>
		                      <input type="checkbox" id="pdt_buy" name="pdt_buy" <c:out value="${productVO.pdt_buy == 'Y' ? 'checked':''}" />>
		                    	  ????????????
		                    </label>
		                  </div>
		                </div>
		             </div>
                </div>          
                
                <div class="row">
                	<div class="col-md-2">
		                <div class="form-group">
		                  <label for="file1">???????????????</label>
		                  <input type="file" id="file1" name="file1">
						  <input type="hidden" name="pdt_img" value='<c:out value="${productVO.pdt_img }"/>'>
		                  <p class="help-block">Example block-level help text here.</p>
		                </div>
                	</div>
                	<div class="col-md-10">
                		<div class="form-group">
                			<img src="/admin/product/displayFile?fileName=<c:out value="${productVO.pdt_img }"/>" alt="">
                		</div>
                	</div>
                </div>
              </div>
              <!-- /.box-body -->

              <div class="box-footer">
                <button type="submit" class="btn btn-primary">Submit</button>
              </div>
            </form>
          </div>

    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

  <!-- Main Footer -->
  <%@ include file="/WEB-INF/views/admin/includes/bottom.jsp" %>

 
</div>
<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->


<%@ include file="/WEB-INF/views/admin/includes/common.jsp" %>


<!-- CK Editor -->
<script src="/bower_components/ckeditor/ckeditor.js"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>


<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<script>
  $(function () {
	  // ckeditor ????????????
	  var ckeditor_config = {
				resize_enabled : false,
				enterMode : CKEDITOR.ENTER_BR,
				shiftEnterMode : CKEDITOR.ENTER_P,
				toolbarCanCollapse : true,
				removePlugins : "elementspath", 
				// ?????? ????????? ?????? ??????
				// CKEditor??? ????????? ????????? ?????? ??? ?????? ????????? ????????? ???
				filebrowserUploadUrl: '/admin/product/imgUpload'
		};

    // Replace the <textarea id="editor1"> with a CKEditor
    // instance, using default configuration.
    CKEDITOR.replace('pdt_detail', ckeditor_config);
    //bootstrap WYSIHTML5 - text editor
    $('.textarea').wysihtml5();
    
    // 4.12.1 (Standard)??????
    //alert(CKEDITOR.virsion);


			$("#mainCategory").on("change",function(){

				// 1?????????????????? ???????????? 2?????????????????? ajax??? ???????????? ????????? ???????????? ??????
				let mainCategory = $(this).val();
				let url = "/admin/product/subCategory/" + mainCategory;

				$.getJSON(url, function(data){

					// 2??? ???????????? data??? ????????? ??????
				//	console.log(data[0]);
					subCategoryView(data, $("#subCategory"), $("#subCateTemplate"));
				});
			});

  })
</script>
<script id="subCateTemplate" type="text/x-handlebars-template">
	<option value="">2??? ???????????? ??????</option>
	{{#each.}}
		<option value="{{cate_code_pk}}">{{cate_name}}</option>
	{{/each}}

</script>

<script>
	let subCategoryView = function(subCategory, target, template){

		let templateObj = Handlebars.compile(template.html());
		let options = templateObj(subCategory);
		$("#subCategory option").remove(); // ????????? ????????????
		target.append(options);
	}

</script>
</body>
</html>