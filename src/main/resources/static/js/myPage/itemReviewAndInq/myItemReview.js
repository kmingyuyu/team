

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function checkLogin() {
		
	var loginOkValue = document.getElementById('loginOk').value;
	var loginOk = (loginOkValue === 'true');
	
    if (!loginOk) {
        location.href = '/members/login';
        return false;
    }
    
    return true;
}



function itemReivewPage(page) {
	
	var data = document.getElementById('data').value;
    var startTime = document.querySelector('.st_date').value;
    var endTime = document.querySelector('.end_date').value;
    
	
	location.href = '/myPage/item_review/' + page 
					+ '?data=' + data 
					+ '&startTime=' + startTime
					+ '&endTime=' + endTime;
}



$(document).ready(function() {
	
	 $(".review-content-photo__item").click(function() {
		 
        const reviewId = $(this).data("review-id");
        const target = $(".review_info").find(`.review-content-photo[data-review-id='${reviewId}']`);
        
        target.toggleClass("review-contents--active");
    });
    
	
	})
	
$(document).ready(function() {
    $(".review-content-text").each(function() {
        var lineHeight = parseFloat($(this).css('line-height'));
        var maxHeight = lineHeight * 3; 

        if ($(this).height() > maxHeight) {
            $(this).addClass("over-flow");
           $(this).closest(".review-content").on("click", function() {
                $(this).toggleClass("review-content-active");
            });
        }
    });
});


document.addEventListener("DOMContentLoaded", function() {
    var reviewAnswerButtons = document.querySelectorAll('.review-answer-bt p');

    reviewAnswerButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var reviewAnswer = this.parentElement.nextElementSibling; // 다음 형제 요소인 .review-answer 선택
            
            if (reviewAnswer.style.display === 'block' || reviewAnswer.style.display === '') {
                reviewAnswer.style.display = 'none';
                this.textContent = '답변 열기';
            } else {
                reviewAnswer.style.display = 'block';
                this.textContent = '답변 닫기';
            }
        });
    });
});


function itemReviewModi(button){
	
		if (!checkLogin()) return;
	
	var itemReviewId = button.getAttribute("data-review-id");
	
    var _width = '640';
    var _height = '860';
    var _left = Math.ceil(( window.screen.width - _width )/2);
    var _top = Math.ceil(( window.screen.height - _height )/2); 
    
 	var url =  "/myPage/item_review/itemReview_popup_modi/" + itemReviewId;
 
    window.open(url, 'itemReviewPopupModiWindow', 'width='+ _width +', height='+ _height +', left=' + _left + ', top='+ _top );
	
}

function itemReviewDelete(button){
	
	
	if(confirm('리뷰 삭제 하시겠습니까? 삭제시 받은 포인트가 차감됩니다.')){
			
		var itemReviewId = button.getAttribute("data-review-id");
			
			var url = '/myPage/item_review/itemReviewDelete'
			
			paramData = {itemReviewId : itemReviewId};
			
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



	

