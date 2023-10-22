	
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
	
	$(document).ready(function() {
    $("#content").on("input", function() {
        var content = $(this).val();
        if (content.length > 350) {
            $(this).val(content.substr(0, 350)); 
            alert("350자 이상 입력할 수 없습니다.");
        }
    });
	});
	
	
	$('#title').keydown(function(event) {
    if (event.keyCode === 13) {
        event.preventDefault();
    }
	});
	
	function inqCancel() {
        window.close(); 
	}
	
	function inqOk() {
	 	
		var itemId = $("#itemId").val();
		var itemInqBoardEnum = $("input[name='itemInqBoardEnum']:checked").val() || 1;
        var title = $("#title").val().trim();
        var originalContent = $('#content').val().trim();
		var content = originalContent.replace(/\n/g, '\\n');
        var itemInqEnum = $("input[name='itemInqEnum']:checked").val() ;
			
			if (!itemInqEnum) {
       			 alert("문의유형을 선택해주세요.");
      			  return;
   				 }
   			 if (!title) {
      			  alert("제목을 입력 해주세요.");
        		 return;
   				 }
    		if (!originalContent) {
       			 alert("문의 내용을 입력 해주세요.");
      			  return;
   				 }
   			
   			if(title.lenth  > 15){
				 alert("제목은 15자 이내로 입력해주세요.");
      			  return;  
			   }
   			
   			if(originalContent.lenth > 350){
				 alert("내용은 350자 이내로 입력해주세요.");
      			  return;   
			   }
		
		if (confirm("문의글 등록 하시겠습니까?")) {
	  
		var url = "/inqReq"
		
		var paramData = {
				id : itemId,
				itemInqBoardEnum : itemInqBoardEnum,
				title : title,
				content : content,
				itemInqEnum : itemInqEnum
			}
			
			var param = JSON.stringify(paramData);

			$.ajax({
				url : url, //request URL
				type : "POST", //전송방식
				contentType : "application/json",
				data : param, //서버에 전송할 데이터
				beforeSend : function(xhr) {
					//데이터를 전송하기전에 헤더에 csrf 값을 설정
					xhr.setRequestHeader(header, token);
				},
				dataType : "text",
				cache : false,
				success: function(_, _, jqXHR) {
  				  	alert(jqXHR.responseText);
  				  	window.opener.location.reload();
       				 window.close();
				},
				error : function(jqXHR) {
							if (jqXHR.status == '403') {
								alert('로그인 후 이용해주세요.');
								 window.opener.location.reload();
								 window.close();
						} else {
								alert(jqXHR.responseText);
						}
				}
			})
		}
	}
	
	
	function inqModiOk(button){
		
		var itemInqId = button.getAttribute("data-inq-id");
		var itemInqBoardEnum = $("input[name='itemInqBoardEnum']").is(":checked") ? 2 : 1;
        var title = $("#title").val().trim();
        var originalContent = $('#content').val().trim();
		var content = originalContent.replace(/\n/g, '\\n');
        var itemInqEnum = $("input[name='itemInqEnum']:checked").val() ;
			
			if (!itemInqEnum) {
       			 alert("문의유형을 선택해주세요.");
      			  return;
   				 }
   			 if (!title) {
      			  alert("제목을 입력 해주세요.");
        		 return;
   				 }
    		if (!originalContent) {
       			 alert("문의 내용을 입력 해주세요.");
      			  return;
   				 }
   			
   			if(title.lenth  > 15){
				 alert("제목은 15자 이내로 입력해주세요.");
      			  return;  
			   }
   			
   			if(originalContent.lenth > 350){
				 alert("내용은 350자 이내로 입력해주세요.");
      			  return;   
			   }
			   
			if (confirm("문의글 수정 하시겠습니까?")) {
	  
		var url = "/myPage/itemInq/itemInqPopupModi/modiOk"
		
		var paramData = {
				itemInqId : itemInqId,
				itemInqBoardEnum : itemInqBoardEnum,
				title : title,
				content : content,
				itemInqEnum : itemInqEnum
			}
			
			var param = JSON.stringify(paramData);

			$.ajax({
				url : url, //request URL
				type : "PATCH", //전송방식
				contentType : "application/json",
				data : param, //서버에 전송할 데이터
				beforeSend : function(xhr) {
					//데이터를 전송하기전에 헤더에 csrf 값을 설정
					xhr.setRequestHeader(header, token);
				},
				dataType : "text",
				cache : false,
				success: function(_, _, jqXHR) {
  				  	alert(jqXHR.responseText);
  				  	window.opener.location.reload();
       				 window.close();
				},
				error : function(jqXHR) {
							if (jqXHR.status == '403') {
								alert('로그인 후 이용해주세요.');
								 window.opener.location.reload();
								 window.close();
						} else {
								alert(jqXHR.responseText);
						}
				}
			})
		}
	}
	
	
	