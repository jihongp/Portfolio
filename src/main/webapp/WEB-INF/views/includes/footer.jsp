<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<script>
	// ajax 전송되기 전에 호출되는 메서드
   
	/*
	$(document).ajaxSend(function(event, request, settings){

        console.log("ajaxSend");
        request.setRequestHeader("AJAX", "true");

    });
	*/

    // ajax 에러처리. 서버에서 500 상태코드가 클라이언트에게 보내지면
    $(document).ajaxError(function(event, request, settings, thrownError){

        if (request.status == 500 || request.status == 0){
        	console.log("ajaxError");
        	
        	alert("ajax error: " + request.status);
            location.href = "/member/login";
        }else{
            alert("다음 위치에서 에러가 발생됨. 관리자에 문의요망\n" + settings.url);
        }
    });
</script>    
<footer class="pt-4 my-md-5 pt-md-5 border-top">
	<h3>footer</h3>  
</footer>