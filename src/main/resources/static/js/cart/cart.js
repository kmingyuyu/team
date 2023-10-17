function cartOrder() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
    const formData = new FormData();

    // 체크된 체크박스만 선택
    const checkboxes = document.querySelectorAll('.checked_cart:checked');
    
    checkboxes.forEach((checkbox, index) => {
    const itemId = checkbox.dataset.itemId;
    const cartId = checkbox.dataset.cartId;
    const counts = parseInt($(".goodsCnt_0").eq(index).val(), 10); 
    formData.append("ids", itemId);
    formData.append("cartIds", cartId);
    formData.append("counts", counts);
});

	var url = '/order/cart';

 		$.ajax({
					url : url, //request URL
					type : "POST", //전송방식
					contentType : false,
					processData: false, 
					data : formData, //서버에 전송할 데이터
					beforeSend : function(xhr) {
						//데이터를 전송하기전에 헤더에 csrf 값을 설정
						xhr.setRequestHeader(header, token);
					},
					dataType : "text",
					cache : false,
					success : function(_, _, jqXHR) {
						location.href = '/order/final';
						
					},
					error : function(jqXHR) {
						alert(jqXHR.responseText);
							
					}
				})



}

	$(document).ready(function() {
	
	initialButtonState();
	
});	
	
	
	function initialButtonState() {
  let hasSoldOut = false;
  $('.checked_cart').each(function() {
    if ($(this).data('cart-status') === 'SOLD_OUT') {
      hasSoldOut = true;
      return false; // each 문을 빠져나감
    }
  });

  // SOLD_OUT 상품이 있으면 .click_disable 클래스를 제거, 없으면 추가
  if (hasSoldOut) {
    $('#del_chk_soldout').removeClass('click_disable');
  } else {
    $('#del_chk_soldout').addClass('click_disable');
  }
}
	
		
	$(document).ready(function() {
		
		recalculateTotal()
		
	function updateButtonState() {

  if ($('.checked_cart:checked').length > 0) {
    $('#del_chk').removeClass('click_disable');
    $('#btn-order').removeClass('click_disable_pay');
    $('#btn-order').text('주문하기');
  } else {
    $('#del_chk').addClass('click_disable');
    $('#btn-order').addClass('click_disable_pay');
    $('#btn-order').text('상품을 선택해주세요');
  }
}
	
	updateButtonState();
	
	  $('.checked_cart').change(function() {
    updateButtonState();
    recalculateTotal();
  });
  
 $('#chk_all').change(function() {
  if($(this).prop('checked')) {
    // 전체 선택
    $('.checked_cart').each(function() {
        $(this).prop('checked', true);
    
    });
  } else {
    // 전체 해제
    $('.checked_cart').each(function() {
        $(this).prop('checked', false);
    });
  }
  updateButtonState();
  recalculateTotal();
});
	
		
    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
    
 
    
     function recalculateTotal() {
        let totalOriPrice = 0;
        let totalSalePrice = 0;

     $('.checked_cart:checked').each(function() {
		 
		  if ($(this).data('cart-status') === 'SOLD_OUT') {
            return true; // continue to next iteration
        }
		 
        const cartId = $(this).data('cart-id');
        const oriPrice = parseInt($(`.ori_price_so[data-cart-id=${cartId}]`).text().replace(/,/g, ''), 10);
        const salePrice = parseInt($(`.sale_price_so[data-cart-id=${cartId}]`).text().replace(/,/g, ''), 10);

        totalOriPrice += oriPrice;
        totalSalePrice += salePrice;
    });

       let finalPrice = totalOriPrice - totalSalePrice;
        
       let totalDcRate = 0;
   		 if (totalOriPrice !== 0) {
        totalDcRate = totalSalePrice / totalOriPrice  * 100;
  		  }
  		  
  	    totalDcRate = Math.round(totalDcRate);
  	    
  	    let deleveryPrice = 0;

		if(finalPrice !== 0 && finalPrice <= 40000){
    		deleveryPrice = 4000;
    		finalPrice += deleveryPrice;
		}
		   
  	  
		
        $('#ori_price').text(numberWithCommas(totalOriPrice));
        $('#sale_price').text(numberWithCommas(totalSalePrice));
        $('#final_price').text(numberWithCommas(finalPrice));
        $('#total_dc_rate').text(totalDcRate); 
        $('#delevery_price').text(numberWithCommas(deleveryPrice));
    }
    


    function recalculate($goodsCnt, $oriPriceSo, $salePriceSo, $totalPriceSo) {
    const oriUnitPrice = parseInt($oriPriceSo.attr('data-ori-unit-price'), 10);
    const saleUnitPrice = parseInt($salePriceSo.attr('data-sale-unit-price'), 10);
    let cnt = parseInt($goodsCnt.val(), 10);
        
  	$oriPriceSo.text(numberWithCommas(oriUnitPrice * cnt) + '원');
	$salePriceSo.text(numberWithCommas(saleUnitPrice * cnt) + '원');
	$totalPriceSo.text(numberWithCommas((oriUnitPrice - saleUnitPrice) * cnt) + '원');
    
    recalculateTotal();
    }

    $('.up, .down').click(function() {
        const cartId = $(this).data('cart-id');
        const $goodsCnt = $(`.text[data-cart-id=${cartId}]`);
        const $oriPriceSo = $(`.ori_price_so[data-cart-id=${cartId}]`);
        const $salePriceSo = $(`.sale_price_so[data-cart-id=${cartId}]`);
        const $totalPriceSo = $(`.total_price_so[data-cart-id=${cartId}]`);

        let currentVal = parseInt($goodsCnt.val(), 10);
        let stock = parseInt($goodsCnt.attr('data-cart-stock'), 10);

        if($(this).hasClass('up')) {
            if(currentVal < stock) {
                $goodsCnt.val(currentVal + 1);
            } else {
                alert('재고보다 많습니다');
                return;
            }
        } else if($(this).hasClass('down')) {
            if(currentVal > 1) {
                $goodsCnt.val(currentVal - 1);
            }
        }

        recalculate($goodsCnt, $oriPriceSo, $salePriceSo, $totalPriceSo);
         recalculateTotal(); 
    });

    $('.text').on('input', function() {
        let stock = parseInt($(this).attr('data-cart-stock'), 10);
        let currentVal = parseInt($(this).val(), 10);

        if(isNaN(currentVal) || currentVal <= 0) {
            $(this).val(1);
            return;
        }

        if(currentVal > stock) {
            alert('재고보다 많습니다');
            $(this).val(1);
        }

        const cartId = $(this).data('cart-id');
        const $oriPriceSo = $(`.ori_price_so[data-cart-id=${cartId}]`);
        const $salePriceSo = $(`.sale_price_so[data-cart-id=${cartId}]`);
        const $totalPriceSo = $(`.total_price_so[data-cart-id=${cartId}]`);

        recalculate($(this), $oriPriceSo, $salePriceSo, $totalPriceSo);
         recalculateTotal(); 
    });

   $('.text').on('input', function() {
    const value = $(this).val();
    if(/[^0-9]/.test(value)) { // 숫자 외의 문자가 있다면
        $(this).val(value.replace(/[^0-9]/g, '')); // 숫자만 남기고 다 지움
    }
});
});
		
		function showPopup(button) {
			
			
			var id = $(button).attr("data-item-id");
			
			url =  "/popup/" + id;
			
		    window.open( url , "popupWindow", "width=620, height=860, left=100, top=50"); 
		}
		
		
		
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		
		function cartDelete_button(button){
			var id = $(button).attr("data-cart-id");
			var url = '/cart/cartDeleteButton'
			paramData = {cartId : id};
			
			if (confirm('장바구니에서 삭제하시겠습니까?')){
				
				var param = JSON.stringify(paramData);
				
					$.ajax({
						url : url, //request URL
						type : "DELETE", //전송방식
						contentType : "application/json",
						data : param, //서버에 전송할 데이터
						beforeSend : function(xhr) {
							//데이터를 전송하기전에 헤더에 csrf 값을 설정
							xhr.setRequestHeader(header, token);
						},
						dataType : "text",
						cache : false,
						success : function(_, _, jqXHR) {
							 alert(jqXHR.responseText);
							 window.location.reload(true); 
							
						},
						error : function(jqXHR) {
								if (jqXHR.status == '403') {
								alert('로그인 후 이용해주세요.');
									location.href = '/';
							} else {
								alert(jqXHR.responseText);
							}
						}
					})
			}
								
		}
		
		function cartCountUpdate_button(button){
			
			const cartId = $(button).attr("data-cart-id");
   			const goodsCntElement = $(`.text[data-cart-id=${cartId}]`);
    		const goodsCount = goodsCntElement.val();
    		
			var url = '/cart/cartCountUpdateButton'
    		
			
			const sendData = {
   			      "cartId": cartId,
      			  "count": goodsCount
  				  };
    			
    		var param = JSON.stringify(sendData);
    		
    			if (confirm('상품 수량 저장하시겠습니까?')){
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
							if (jqXHR.status == '403') {
								alert('로그인 후 이용해주세요.');
									location.href = '/';
							} else {
								alert(jqXHR.responseText);
							}
						}
					})
					
				}
    			
			
		}
		
		
		function cartDelete_checkBok(){
			
			var selectedItems = [];
			var url = '/cart/cartDeletCkeckBox'
			
			 $('.checked_cart:checked').each(function() {
    				  selectedItems.push($(this).attr('data-cart-id'));
    			});
    			
    		var param = JSON.stringify(selectedItems);
    		
    			if (confirm('선택된 상품 장바구니에서 삭제하시겠습니까?')){
						$.ajax({
						url : url, //request URL
						type : "DELETE", //전송방식
						contentType : "application/json",
						data : param, //서버에 전송할 데이터
						beforeSend : function(xhr) {
							//데이터를 전송하기전에 헤더에 csrf 값을 설정
							xhr.setRequestHeader(header, token);
						},
						dataType : "text",
						cache : false,
						success : function(_, _, jqXHR) {
							 alert(jqXHR.responseText);
							 window.location.reload(true); 
							
						},
						error : function(jqXHR) {
							if (jqXHR.status == '403') {
								alert('로그인 후 이용해주세요.');
									location.href = '/';
							} else {
								//에러메세지 출력(ResponseEntity에서 받아온 값을 출력해준다)
								alert(jqXHR.responseText);
							}
						}
					})
					
				}
    			
			
		} 

		function cartDelete_soldout_checkBok() {
			var selectedItems = [];
			var url = '/cart/cartDeleteSoldoutCheckBox'
			
    		$('.checked_cart').each(function() {
   		 if ($(this).data('cart-status') === 'SOLD_OUT') {
        selectedItems.push($(this).attr('data-cart-id'));
   		 }
  		});	
  		
  			var param = JSON.stringify(selectedItems);
    		
						$.ajax({
						url : url, //request URL
						type : "DELETE", //전송방식
						contentType : "application/json",
						data : param, //서버에 전송할 데이터
						beforeSend : function(xhr) {
							//데이터를 전송하기전에 헤더에 csrf 값을 설정
							xhr.setRequestHeader(header, token);
						},
						dataType : "text",
						cache : false,
						success : function(_, _, jqXHR) {
							 alert(jqXHR.responseText);
							 window.location.reload(true); 
							
						},
						error : function(jqXHR) {
							if (jqXHR.status == '403') {
								alert('로그인 후 이용해주세요.');
									location.href = '/';
							} else {
								//에러메세지 출력(ResponseEntity에서 받아온 값을 출력해준다)
								alert(jqXHR.responseText);
							}
						}
					})
					
				
  		
  		
		}
		
		
		

    
		



