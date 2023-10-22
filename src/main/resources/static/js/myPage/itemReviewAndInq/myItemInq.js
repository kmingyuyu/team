
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function itemInqPage(page) {
	
	var data = document.getElementById('data').value;
    var startTime = document.querySelector('.st_date').value;
    var endTime = document.querySelector('.end_date').value;
    var searchQuery = document.querySelector('.inp_nm').value;
	
	location.href = '/myPage/itemInq/' + page 
					+ '?data=' + data 
					+ '&startTime=' + startTime
					+ '&endTime=' + endTime
					+ '&searchQuery=' + searchQuery;
}


$(document).ready(function() {
    $(".inq-content-tap").on("click", function() {
        var closestInqContent = $(this).closest("tr").next(".inq-content");

        if (closestInqContent.css("display") === "none") {
            closestInqContent.css("display", "table-row");
        } else {
            closestInqContent.css("display", "none");
        }
    });
});

function itemInqModi(button){
		
		var itemInqId = button.getAttribute("data-inq-id");
	
			
		var _width = '640';
    	var _height = '860';
    	var _left = Math.ceil(( window.screen.width - _width )/2);
   		var _top = Math.ceil(( window.screen.height - _height )/2); 
			
		url =  "/myPage/itemInq/itemInqPopupModi/" + itemInqId;
			
		window.open( url , "itemInqWindow", 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top ); 
		
}

function itemInqDelete(button){
	
	if(confirm('문의글 삭제 하시겠습니까?')){
			
			var itemInqId = button.getAttribute("data-inq-id");
			
			var url = '/myPage/itemInq/itemInqDelete'
			
			paramData = {itemInqId : itemInqId};
			
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
