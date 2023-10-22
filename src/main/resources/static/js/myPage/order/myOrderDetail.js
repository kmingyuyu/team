function orderDelete(orderNumber){
	
	if(confirm('주문내역 삭제 하시겠습니까?\n삭제시 복구가 불가하며 주문/환불/배송 조회가 불가합니다.')){
		
		var url = '/myPage/order/detail/orderDelete'
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		paramData = {orderNumber : orderNumber};
			
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
					location.href = '/myPage/order';
							
					},
				error : function(jqXHR) {
					alert(jqXHR.responseText);
					
					}
					
					})
	}
	
}