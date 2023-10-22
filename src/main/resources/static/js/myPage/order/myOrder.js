
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");


function orderPage(page) {
	
	var data = document.getElementById('data').value;
    var startTime = document.querySelector('.st_date').value;
    var endTime = document.querySelector('.end_date').value;
    var searchQuery = document.querySelector('.inp_nm').value;
    
	
	location.href = '/myPage/order/'+ page 
					+ '?data=' + data 
					+ '&startTime=' + startTime
					+ '&endTime=' + endTime
					+ '&searchQuery=' + searchQuery;
}

		
function deliveryPopup(button) {
	
 	
 	var invoiceNumber = button.getAttribute("data-invoiceNumber");
 	
    var _width = '780';
    var _height = '700';
    var _left = Math.ceil(( window.screen.width - _width )/2);
    var _top = Math.ceil(( window.screen.height - _height )/2); 
    
 	var url =  "/myPage/order/deliveryPopup/" + invoiceNumber;
 
    window.open(url, 'deliveryPopupWindow', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
 	
}

function itemReviewPopupReg(button) {
	
	
	var orderItemId = button.getAttribute("data-orderItemId");
	
    var _width = '640';
    var _height = '860';
    var _left = Math.ceil(( window.screen.width - _width )/2);
    var _top = Math.ceil(( window.screen.height - _height )/2); 
    
 	var url =  "/myPage/order/itemReviewPopupReg/" + orderItemId;
 
    window.open(url, 'itemReviewPopupWindow', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
 	
}


function orderConfirmed(button){
	
	if(confirm('구매확정 하시겠습니까? 구매확정시 반품/환불이 불가합니다.')){
			
			var orderItemId = button.getAttribute("data-orderItemId");
			
			var url = '/myPage/order/orderConfirmed'
			
			paramData = {orderItemId : orderItemId};
			
			var param = JSON.stringify(paramData);
			
			$.ajax({
				url : url, //request URL
				type : "PATCH", //전송방식
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

function orderCancel(button){
	
	if(confirm('주문취소 하시겠습니까? 주문취소시 철회 불가능합니다.')){
		
			var orderItemId = button.getAttribute("data-orderItemId");
			
			var url = '/myPage/order/orderCancel'
			
			paramData = {orderItemId : orderItemId};
			
			var param = JSON.stringify(paramData);
			
			$.ajax({
				url : url, //request URL
				type : "PATCH", //전송방식
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



