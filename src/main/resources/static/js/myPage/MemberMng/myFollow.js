
	// 엔터키 적용
	document.querySelector('.inp_nm_follow').addEventListener('keydown', function(e) {
    if (e.key === 'Enter' || e.keyCode === 13) {
        e.preventDefault();
        
        var currentPath = window.location.pathname;
        var pathSegments = currentPath.split('/');
        
        var segmentOfInterest = pathSegments[2];
        
        dynamicSearch(segmentOfInterest);  
    }
	});	


function dynamicSearch(url) {
	
	var searchQuery = document.querySelector('.inp_nm_follow').value;
	var data = document.getElementById('data').value;
	
	window.location.href = '/myPage/'+ url 
							+ '?data=' + data 
							+ '&searchQuery=' + searchQuery;
 }



function goList(data,url){
	
		window.location.href = '/myPage/'+ url 
					+ '?data=' + data;
	}
		
		
	function followPage(page) {
	
		var data = document.getElementById('data').value;
    
	
		location.href = '/myPage/follow/'+ page 
					+ '?data=' + data;
	}





// 토큰
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

// 팔로우 취소
function followDelete(button) {
		var nickname = button.getAttribute("data-nickname");
		var followId = button.getAttribute("data-follow");
	if(confirm(nickname+' 님 팔로우를 취소 하시겠습니까?')){
			
			
			var url = '/myPage/follow/followDelete'
			
			paramData = {followId : followId};
			
			var param = JSON.stringify(paramData);
			
			$.ajax({
				url : url, //request URL
				type : "DELETE", //전송방식
				contentType : "application/json",
				data : param, //서버에 전송할 데이터
				beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
				dataType : "text",
				cache : false,
				success : function(_, _, jqXHR) {
					alert(jqXHR.responseText);
					window.location.reload(true); 
							
					},
				error : function(jqXHR) {
					alert(jqXHR.responseText);
							
					}
					
					})
		
	}
}

// 팔로우 등록
function followReg(button) {
		var nickname = button.getAttribute("data-nickname");
	if(confirm(nickname+' 님 팔로우 등록 하시겠습니까?')){
			
			
			var url = '/myPage/follow/followReg'
			
			paramData = {nickname : nickname};
			
			var param = JSON.stringify(paramData);
			
			$.ajax({
				url : url, //request URL
				type : "POST", //전송방식
				contentType : "application/json",
				data : param, //서버에 전송할 데이터
				beforeSend : function(xhr) {
						xhr.setRequestHeader(header, token);
					},
				dataType : "text",
				cache : false,
				success : function(_, _, jqXHR) {
					alert(jqXHR.responseText);
					window.location.reload(true); 
							
					},
				error : function(jqXHR) {
					alert(jqXHR.responseText);
							
					}
					
					})
		
	}
}