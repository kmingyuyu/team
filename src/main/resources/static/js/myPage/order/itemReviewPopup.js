var selectedImages = [];		

var oriImages = [];

var oriDeleteImages = [];
		
// 별점 css
$(document).ready(function() {
    $('.n-score.score-lg .score').click(function(e) {
        e.preventDefault();

        $('.n-score.score-lg .score').removeClass('is-active');

        $(this).addClass('is-active');
    });
});

	$(document).ready(function() {
    	$('.element-image').on('change', function() {
        	var file = this.files[0];
        
        if (file) {
			
			var isAlreadySelected = selectedImages.some(function(selectedImage) {
            return selectedImage.file.name === file.name;
            });
            
            if (oriImages.length !== 0) {
    			var fileInput = document.getElementById('file');
   				var selectedFileNames = Array.from(fileInput.files).map(file => file.name);

    			var isAlreadySelectedOriImages = selectedFileNames.some(function(fileName) {
       			 return oriImages.includes(fileName);
    			});

    			if (isAlreadySelectedOriImages) {
       				 alert("같은 이미지가 등록되어 있습니다.");
       				 return;
    			}
			}
            
            
            if (isAlreadySelected) {
                alert("같은 이미지가 등록되어 있습니다.");
                return;  
            }
			
            var fileExt = file.name.split('.').pop().toLowerCase();
            if (fileExt != "jpg" && fileExt != "jpeg" && fileExt != "gif" && fileExt != "png" && fileExt != "bmp") {
                alert("이미지 파일만 등록 가능합니다.");
                return; 
            }
            
            if (file.size > 2 * 1024 * 1024) {  
                alert("이미지파일은 2MB 이하만 가능합니다.");
                return;  
            }

            var reader = new FileReader();
            reader.onload = function(e) {
                var imgSrc = e.target.result;
				
				selectedImages.push({
                    file: file 
                });
				
                // 현재 .show-box와 .show-plus-box의 display 상태 확인
                var currentShowBoxDisplay = ($('.show-box').css('display') === 'none') ? 'none' : 'block';
                var currentShowPlusBoxDisplay = (currentShowBoxDisplay === 'none') ? 'block' : 'none';

               var showImgDiv = '<div class="show-box" style="display:' + currentShowBoxDisplay + ';" >'
   								 + '<div class="show-img" title="확대">'
    							 + '<img class="img-cl" src="' + imgSrc + '" data-filename="' + file.name + '">'
   								 + '</div>'
    							 + '<p class="delete-bt" title="삭제">삭제하기</p>'
    							 + '</div>';

				var showImgPlusDiv = '<div class="show-plus-box" style="display:' + currentShowPlusBoxDisplay + ';" title="축소">'
   								   	  + '<div class="show-img-plus">'
   								   	  + '<img class="img-cl" src="' + imgSrc + '" data-filename="' + file.name + '">'
   								      + '</div>';

                $('.fvdiv').append(showImgDiv + showImgPlusDiv);
            }
            reader.readAsDataURL(file);
            $(this).val('');
        }
    });

});


$(document).on('click', '.show-img', function() {
        $('.show-box').hide();
        $('.show-plus-box').show();
        $('.fvdiv').css('display', 'block');
    });

$(document).on('click', '.show-img-plus', function() {
        $('.show-box').show();
        $('.show-plus-box').hide();
        $('.fvdiv').css('display', 'flex');
    });

// 이미지 삭제
 $(document).on('click', '.delete-bt', function() {
        var $showBox = $(this).closest('.show-box');
        var fileName = $showBox.find('.img-cl').data('filename');

    selectedImages = selectedImages.filter(function(image) {
        return image.file.name !== fileName;  // 파일 이름으로 비교
    });
    
    if (oriImages.length !== 0) {
    oriImages = oriImages.filter(function(oriImageName) {
        return oriImageName !== fileName;
    });
	}

        var $showPlusBox = $showBox.next('.show-plus-box');
        $showBox.remove();
        $showPlusBox.remove();
        $('.element-image').val('');
    });
    
document.getElementById('content').addEventListener('input', function() {
    if (this.value.length > 350) {
        alert('후기 내용은 350자 이하로 작성해주세요.');
        this.value = this.value.substring(0, 350);
    }
});    

// 나가기
function reviewCancel() {
         window.close();
}

function reviewReg(){
	var orderItemValue =  $('#oi').val();
	var starValue = $('#startScoreWrap a.is-active').data('star');
	var originalContent = $('#content').val().trim();
	var contentValue = originalContent.replace(/\n/g, '\\n');
    var formData = new FormData();
	
	if(!starValue) {
    alert("별점을 등록 해주세요.");
    return;
	}

	if(!originalContent) {
    alert("후기 내용을 작성해주세요.");
    return; 
	}

	if(originalContent.length > 350) {
    alert("후기 내용은 350자 이하로 작성 해주세요.");
    return; 
	}
	
    formData.append("star", starValue);
    formData.append("orderItemId", orderItemValue);
    formData.append("content", contentValue);

    for (var i = 0; i < selectedImages.length; i++) {
        formData.append("imgFiles", selectedImages[i].file);
    }
    
			var url = '/myPage/order/itemReview_popup_reg/regOk'
			
			if (confirm('후기 등록 하시겠습니까?')){
				
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
				
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


function reviewModi(){
	var itemReviewValue =  $('#ri').val();
	var itemReviewStatus =  $('#rs').val();
	var starValue = $('#startScoreWrap a.is-active').data('star');
	var originalContent = $('#content').val().trim();
	var contentValue = originalContent.replace(/\n/g, '\\n');
    var formData = new FormData();
	
	if(!starValue) {
    alert("별점을 등록 해주세요.");
    return;
	}

	if(!originalContent) {
    alert("리뷰 내용을 작성해주세요.");
    return; 
	}

	if(originalContent.length > 350) {
    alert("리뷰 내용은 350자 이하로 작성 해주세요.");
    return; 
	}
	
	
	
    formData.append("star", starValue);
    formData.append("itemReviewId", itemReviewValue);
    formData.append("content", contentValue);

    for (var i = 0; i < selectedImages.length; i++) {
        formData.append("imgFiles", selectedImages[i].file);
    }
    
	for (var i = 0; i < oriImages.length; i++) {
        formData.append("oriImgNames", oriImages[i]);
    } 
    			
  	 var imagesToDelete = oriDeleteImages.filter(function(image) {
    	return oriImages.indexOf(image) === -1;
	});

	for (var i = 0; i < imagesToDelete.length; i++) {
   		 formData.append("oriImgDeleteNames", imagesToDelete[i]);
	}
    
			var url = '/myPage/item_review/itemReview_popup_modi/modiOk'
			
			var text='후기 수정 하시겠습니까?'
			
			if(itemReviewStatus == '포토'){
				if(selectedImages.length == 0 && oriImages.length == 0){
    				text = '이미지 삭제시 포인트가 차감됩니다. 수정 진행하시겠습니까?'
				}
			}
			
			if (confirm(text)){
				
				
				var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
				
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


