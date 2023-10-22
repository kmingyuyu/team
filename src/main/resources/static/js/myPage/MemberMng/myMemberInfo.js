
// 토큰
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

// 회원 이미지 미리보기
function updateImagePreview(event) {
    const fileInput = event.target;
    const label = document.getElementById("imagePreviewLabel");

    if (fileInput.files && fileInput.files[0]) {
        const file = fileInput.files[0];
        
        // 파일 확장자 검사
        const fileExt = file.name.split('.').pop().toLowerCase();
        if (fileExt !== "jpg" && fileExt !== "jpeg" && fileExt !== "gif" && fileExt !== "png" && fileExt !== "bmp") {
            alert("이미지 파일만 등록 가능합니다.");
            fileInput.value = "";  
            label.style.backgroundImage = '/img/비회원이미지.jpg';  
            return;
        }

        // 파일 크기 검사
        if (file.size > 2 * 1024 * 1024) {  
            alert("이미지 파일은 2MB 이하만 가능합니다.");
            fileInput.value = "";  
            label.style.backgroundImage = '/img/비회원이미지.jpg';  
            return;  
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            label.style.backgroundImage = `url(${e.target.result})`;
        };
        reader.readAsDataURL(fileInput.files[0]);
    }
}




// 변경창 열기
document.querySelectorAll('.button-scrip-modi').forEach(function(button) {
    button.addEventListener('click', function(event) {
        var clickedButton = event.currentTarget;

        var parentDiv = clickedButton.closest('.m-center');

        if (parentDiv) {
            var mEvent = parentDiv.querySelector('.m-scrip');
            var cEvent = parentDiv.querySelectorAll('.c-scrip');

            if (mEvent) {
                mEvent.style.display = 'block';
            }

            cEvent.forEach(function(element) {
                element.style.display = 'none';
            });
        }
    });
});

// 변경창 닫기
document.querySelectorAll('.button-scrip-cancel').forEach(function(button) {
    button.addEventListener('click', function(event) {
        var clickedButton = event.currentTarget;
        var parentDiv = clickedButton.closest('.m-center');

        if (parentDiv) {
            // .m-scrip 요소를 찾고 숨기기
            var mEvent = parentDiv.querySelector('.m-scrip');
            if (mEvent) {
                mEvent.style.display = 'none';
            }

            // .c-scrip 요소들을 찾고 보이게 만들기
            var cEvent = parentDiv.querySelectorAll('.c-scrip');
            cEvent.forEach(function(element) {
                element.style.display = 'block';
            });

            // 모든 .inptext 요소를 찾아서 초기화
            var inputElements = parentDiv.querySelectorAll('.inptext');
            inputElements.forEach(function(inputElement) {
                inputElement.value = '';
            });

            // 모든 .ps-script 요소를 찾아서 숨기기
            var psScriptElements = parentDiv.querySelectorAll('.ps-script');
            psScriptElements.forEach(function(psScriptElement) {
                psScriptElement.style.display = 'none';
            });
        }
    });
});




// 전화번호 유효성
document.getElementById('phoneNumber-new').addEventListener('input', function() {
    this.value = this.value.replace(/[^0-9]/g, '');
});

// 성함 유효성
document.getElementById('name-new').addEventListener('input', function() {
    this.value = this.value.replace(/[^ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '');
});


// 활성화 전역변수
var isValidPasswordOri = false;
var isValidPasswordNew = false;
var isValidPasswordRecom = false;
var isValidNickname = false; 
var isValidIntroduce = false; 
var isValidPhoneNumber = false; 
var isValidName = false; 
var isValidAddress = false; 

// 변경버튼 활성화 (비밀번호)
function checkValidationsPassword() {
    var buttonElement = document.getElementById('pass-button');
    
    if (isValidPasswordOri && isValidPasswordNew && isValidPasswordRecom) {
        buttonElement.removeAttribute('disabled');
        buttonElement.classList.remove('disabled');
    } else {
        buttonElement.setAttribute('disabled', true);
        buttonElement.classList.add('disabled');
    }
}

// 변경버튼 활성화 (닉네임)
function checkValidationNickname() { 
    var buttonElement = document.getElementById('nickname-button');
    if (isValidNickname) {
        buttonElement.removeAttribute('disabled');
        buttonElement.classList.remove('disabled');
    } else { 
        buttonElement.setAttribute('disabled', true);
        buttonElement.classList.add('disabled');
    }
}

// 변경버튼 활성화 (자기소개)
function checkValidationIntroduce() { 
    var buttonElement = document.getElementById('introduce-button');
    if (isValidIntroduce) {
        buttonElement.removeAttribute('disabled');
        buttonElement.classList.remove('disabled');
    } else { 
        buttonElement.setAttribute('disabled', true);
        buttonElement.classList.add('disabled');
    }
}

// 변경버튼 활성화 (휴대전화)
function checkValidationPhoneNumber() { 
    var buttonElement = document.getElementById('phoneNumber-button');
    if (isValidPhoneNumber) {
        buttonElement.removeAttribute('disabled');
        buttonElement.classList.remove('disabled');
    } else { 
        buttonElement.setAttribute('disabled', true);
        buttonElement.classList.add('disabled');
    }
}

// 변경버튼 활성화 (성함)
function checkValidationName() { 
    var buttonElement = document.getElementById('name-button');
    if (isValidName) {
        buttonElement.removeAttribute('disabled');
        buttonElement.classList.remove('disabled');
    } else { 
        buttonElement.setAttribute('disabled', true);
        buttonElement.classList.add('disabled');
    }
}

// 변경버튼 활성화 (주소)
function checkValidationAddress() { 
    var buttonElement = document.getElementById('address-button');
    if (isValidAddress) {
        buttonElement.removeAttribute('disabled');
        buttonElement.classList.remove('disabled');
    } else { 
        buttonElement.setAttribute('disabled', true);
        buttonElement.classList.add('disabled');
    }
}


// 비밀번호 (기존비밀번호) 유효성 검사
document.getElementById('password-ori').addEventListener('input', function() {
    var inputValue = this.value;
    if (inputValue.length >= 1 && inputValue.length < 9) {
        document.getElementById('password-ori-p').style.display = 'flex';
        isValidPasswordOri = false;
    } else {
        document.getElementById('password-ori-p').style.display = 'none';
        isValidPasswordOri = true;
    }
    checkValidationsPassword();
});

// 비밀번호 (새로운 비밀번호) 유효성 검사
document.getElementById('password-new').addEventListener('input', function() {
    var inputValue = this.value;
    var passwordNewP = document.getElementById('password-new-p');
    if (inputValue.length >= 1 && inputValue.length < 9) {
        passwordNewP.innerText = '9자 이상 입력 해주세요.';
        passwordNewP.style.display = 'flex';
        passwordNewP.classList.remove('ps-ok');
        passwordNewP.classList.add('ps-warning');
        isValidPasswordNew = false;
    } else if (inputValue.length >= 9) {
        passwordNewP.innerText = '사용 가능한 비밀번호 입니다.';
        passwordNewP.classList.remove('ps-warning');
        passwordNewP.classList.add('ps-ok');
        isValidPasswordNew = true;
    } else {
        passwordNewP.style.display = 'none';
        isValidPasswordNew = false;
    }
    checkValidationsPassword();
});

// 비밀번호 (새로운 비밀번호 재입력) 유효성 검사
document.getElementById('password-recom').addEventListener('input', function() {
    var inputValue = this.value;
    if (inputValue.length >= 1 && inputValue.length < 9) {
        document.getElementById('password-recom-p').style.display = 'flex';
        isValidPasswordRecom = false;
    } else {
        document.getElementById('password-recom-p').style.display = 'none';
        isValidPasswordRecom = true;
    }
    checkValidationsPassword();
});


// 닉네임 유효성 검사
document.getElementById('nickname-new').addEventListener('input', function() {
    var inputValue = this.value;
    
    if (/[&<>();'"\s]/g.test(inputValue)) {
        this.value = this.value.replace(/\s/g, '');  
        return;
    }
    
    if (inputValue.length >= 1 && inputValue.length < 2) {
        document.getElementById('nickname-new-p').style.display = 'flex';
        isValidNickname = false; 
    } else {
        document.getElementById('nickname-new-p').style.display = 'none';
        isValidNickname = true; 
    }
    
    checkValidationNickname();
});

// 자기소개 유효성 검사
document.getElementById('introduce-new').addEventListener('input', function() {
    var inputValue = this.value;
    
    if (/[&<>();'"]/g.test(inputValue)) return;
    
    if (inputValue.length < 2) {
        isValidIntroduce = false; 
    } else {
        isValidIntroduce = true; 
    }
    
    checkValidationIntroduce();
});

// 휴대전화 유효성 검사
document.getElementById('phoneNumber-new').addEventListener('input', function() {
    var inputValue = this.value;
    var warningElement = document.getElementById('phoneNumber-ori-p');
    
    if (inputValue.length >= 3 && !inputValue.startsWith("010")) {
        warningElement.style.display = 'flex'; 
        isValidPhoneNumber = false;
        return;
    } else if (inputValue.length < 3 || inputValue.length > 3) {
        warningElement.style.display = 'none';
    }

    if (inputValue.length != 11) {
        isValidPhoneNumber = false;
    } else {
        isValidPhoneNumber = true;
    }
    
    checkValidationPhoneNumber();
});

// 성함 유효성 검사
document.getElementById('name-new').addEventListener('input', function() {
    var inputValue = this.value;
    
    // 한글이면서 2글자 이상일 때
    if (/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g.test(inputValue) && inputValue.length >= 2) {
        isValidName = true;
    } else {
        isValidName = false;
    }
    
    checkValidationName();
});

// 주소 유효성 검사
document.getElementById('detailAddress-new').addEventListener('input', function() {
    var inputValue = this.value;
    var addressValue = document.getElementById('address-new').value;
    var postCodeValue = document.getElementById('postCode-new').value;
    
    // 한글이면서 2글자 이상일 때
    if (inputValue.length >= 1 && addressValue && postCodeValue) {
        isValidAddress = true;
    } else {
        isValidAddress = false;
    }
    
    checkValidationAddress();
});



// 회원 정보 수정
function memberModiOk(button) {
	
	var url;
	var paramData;
	var param;
	
//	닉네임 변경
	if(button == 'nickname'){
		url = "/myPage/memberInfo/nicknameModi";
		
		var nicknameOri = document.getElementById('nickname-ori').value;
		var nicknameNew = document.getElementById('nickname-new').value;
		
		if (!nicknameNew || !nicknameNew.trim()) {
    		alert('닉네임을 입력 해주세요.');
    		return;
		}
		
		if (/[&<>();'"]/g.test(nicknameNew)){
			alert('일부 특수문자 사용 불가능합니다.'); 
			return;
		} 
		
		if (nicknameNew.length < 1 || nicknameNew.length > 11) {
			alert('닉네임은 2자 ~ 10자 사이로 입력 해주세요.'); 
			return;
		}
		
		if(nicknameOri == nicknameNew){
			alert('기존 닉네임과 동일 합니다.'); 
			return;
		}
		
		paramData = {nicknameNew : nicknameNew};
		
		param = JSON.stringify(paramData);
		
	}
	
//  비밀번호 변경
	if(button == 'password'){
		url = "/myPage/memberInfo/phoneNumberModi";
		
		var passwordOri = document.getElementById('password-ori').value;
		var passwordNew = document.getElementById('password-new').value;
		var passwordRecom = document.getElementById('password-recom').value;
		
		if (!passwordOri || !passwordOri.trim()) {
    		alert('기존 비밀번호를 입력 해주세요.');
    		return;
		}

		if (!passwordNew || !passwordNew.trim()) {
    		alert('새로운 비밀번호를 입력 해주세요.');
    		return;
		}

		if (!passwordRecom || !passwordRecom.trim()) {
    		alert('비밀번호 확인을 위해 다시 한 번 입력 해주세요.');
    		return;
		}
		
		if(passwordOri.length < 9 || passwordOri.length > 15){
			alert('기존 비밀번호를 9자 ~ 15자 사이로 입력 해주세요.'); 
			return;
		}
		if(passwordNew.length < 9 || passwordNew.length > 15){
			alert('신규 비밀번호를 9자 ~ 15자 사이로 입력 해주세요.'); 
			return;
		}
		if(passwordRecom.length < 9 || passwordRecom.length > 15){
			alert('신규 비밀번호를 9자 ~ 15자 사이로 입력 해주세요.'); 
			return;
		}
		
		if(passwordNew != passwordRecom){
			alert('신규 비밀번호와 신규 재입력 비밀번호가 불일치 합니다.'); 
			return;
		}
		
		paramData = {
			passwordOri : passwordOri,
			passwordNew : passwordNew,
			passwordRecom : passwordRecom
			
			};
			
		param = JSON.stringify(paramData);
		
	}
	
	//	휴대전화 변경
	if(button == 'phoneNumber'){
		 url = "/myPage/memberInfo/phoneNumberModi";
		
		var phoneNumberOri = document.getElementById('phoneNumber-ori').value;
		var phoneNumberNew = document.getElementById('phoneNumber-new').value;
			
		if (!phoneNumberNew || !phoneNumberNew.trim()) {
    		alert('휴대전화 정보를 입력 해주세요.');
    		return;
		}
		
		if (!phoneNumberNew.startsWith("010")) {
    		alert('휴대전화(010) 정보만 입력해주세요.');
    		return;
		}
		
		if (/[^0-9]/g.test(phoneNumberNew )) {
        	phoneNumberNew = phoneNumberNew.replace(/[^0-9]/g, '');
        	alert('숫자만 입력 해주세요.');
        	return;
    	}
		
		if(phoneNumberNew.length != 11) {
			alert('휴대전화 정보를 11자리 까지 입력 해주세요 (11자리)'); 
			return;
		}
		
		var cleanedPhoneNumber = phoneNumberOri.replace(/-/g, '');
		if(cleanedPhoneNumber == phoneNumberNew) {
			alert('기존 휴대전화 정보와 동일 합니다.'); 
			return;
		} 
		
		paramData = {phoneNumberNew : phoneNumberNew};
		
		param = JSON.stringify(paramData);
		
	}
	
	
	//	자기소개 변경
	if(button == 'introduce'){
		url = "/myPage/memberInfo/introduceModi";
		
		var introduceOri = document.getElementById('introduce-ori').value;
		var introduceNew = document.getElementById('introduce-new').value;
		
		if (!introduceNew || !introduceNew.trim()) {
    		alert('자기소개를 입력 해주세요.');
    		return;
		}
		
		if (/[&<>();'"]/g.test(introduceNew)){
			alert('일부 특수문자 사용 불가능합니다.'); 
			return;
		} 
		
		if(introduceNew.length < 2) {
			alert('자기소개를 2자 이상 입력 해주세요.'); 
			return;
		}
		
		if (introduceNew.length > 20) {
			alert('자기소개를 20자 이내로 입력 해주세요.'); 
			return;
		}
		
		if(introduceOri != '' && introduceOri == introduceNew) {
			alert('기존 자기소개와 동일 합니다.'); 
			return;
		} 
		
		paramData = {introduceNew : introduceNew};
		
		param = JSON.stringify(paramData);
		
	}
	
	//	성함 변경
	if(button == 'name'){
		 url = "/myPage/memberInfo/nameModi";
		
		var nameOri = document.getElementById('name-ori').value;
		var nameNew = document.getElementById('name-new').value;
		
		if (!nameNew || !nameNew.trim()) {
    		alert('성함을 입력 해주세요.');
    		return;
		}
		
		 if (!/^[ㄱ-ㅎㅏ-ㅣ가-힣]+$/g.test(nameNew)) {
			alert('한글만 입력 해주세요.'); 
			return; 
		}
		
		if(nameNew.length < 2) {
			alert('성함을 2자 이상 입력 해주세요.'); 
			return;
		}
		
		if (nameNew.length > 15) {
			alert('성함을 15자 이내로 입력 해주세요.'); 
			return;
		}
		
		if(nameOri != '' && nameOri == nameNew) {
			alert('기존 성함과 동일 합니다.'); 
			return;
		} 
		
		paramData = {nameNew : nameNew};
		
		param = JSON.stringify(paramData);
		
	}
	
	//	주소 변경
	if(button == 'address'){
		 url = "/myPage/memberInfo/addressModi";
		
		var postCodeOri = document.getElementById('postCode-ori').value;
		var postCodeNew = document.getElementById('postCode-new').value;
		
		var addressOri = document.getElementById('address-ori').value;
		var addressNew = document.getElementById('address-new').value;
		
		var detailAddressOri = document.getElementById('detailAddress-ori').value;
		var detailAddressNew = document.getElementById('detailAddress-new').value;
		
		if (!addressNew || !addressNew.trim() || !postCodeNew || !postCodeNew.trim()) {
    			alert('우편번호 찾기로 주소를 설정 해주세요.'); 
   				 return;
			}

		if (!detailAddressNew || !detailAddressNew.trim()) {
   				 alert('상세주소를 입력 해주세요.'); 
    			return;
			}
		
		if (postCodeOri && addressOri && detailAddressOri) {
   			 if (postCodeOri === postCodeNew && addressOri === addressNew && detailAddressOri === detailAddressNew) {
        	alert('기존 주소와 동일 합니다.');
        	return;
    		}
		}
		
		paramData = {
			postCodeNew : postCodeNew ,
			addressNew : addressNew ,
			detailAddressNew : detailAddressNew 
			};
		
		param = JSON.stringify(paramData);
		
	}
			
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

// 회원정보수정(이미지)
function imgModiOk(){
	
	var url = '/myPage/memberInfo/imgModi';
	
	var fileInput = document.getElementById('fileInput');
	var file = fileInput.files[0]; 
	
	if(!file) {
		alert("이미지를 등록 해주세요.");
        return; 
	}
	
	if(file) {
		
	var fileExt = file.name.split('.').pop().toLowerCase();
            if (fileExt != "jpg" && fileExt != "jpeg" && fileExt != "gif" && fileExt != "png" && fileExt != "bmp") {
                alert("이미지 파일만 등록 가능합니다.");
                return; 
            }
            
            if (file.size > 2 * 1024 * 1024) {  
                alert("이미지 파일은 2MB 이하만 가능합니다.");
                return;  
            }
            
            
		var formData = new FormData();
		formData.append('imgFile', file);
	
		if(confirm('이미지 저장 하시겠습니까?')){
			
			$.ajax({
				url : url, //request URL
				type : "PATCH", //전송방식
				data : formData, //서버에 전송할 데이터
				processData: false,
				contentType: false,
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
	
}


// 선택 정보 삭제
function memberDeleteOk(button) {
	
	var url = "/myPage/memberInfo/memberInfoDelete";
	var paramData;
	var param;
	var text;
	
	//자기소개
	if(button == 'introduce') {
		
		paramData = {delete : 'introduce'};
		text = '등록된 자기소개를 삭제 하시겠습니까?'
		param = JSON.stringify(paramData);
		
	}
	
	//성함
	if(button == 'name') {
		
		paramData = {delete : 'name'};
		text = '등록된 성함 정보를 삭제 하시겠습니까?'
		param = JSON.stringify(paramData);
		
	}
	
	//주소
	if(button == 'address') {
		
		paramData = {delete : 'address'};
		text = '등록된 주소 정보를 삭제 하시겠습니까?'
		param = JSON.stringify(paramData);
		
	}
	
	//이미지
	if(button == 'img') {
		
		paramData = {delete : 'img'};
		text = '이미지를 기본이미지로 변경 하시겠습니까?'
		param = JSON.stringify(paramData);
		
	}
	
	if(confirm(text)){
		
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




// 닉네임/자기소개 특수문자 방지
document.addEventListener('DOMContentLoaded', function() {
    var restrictedChars = /[&<>();'"]/g;

    function restrictInput() {
    if (restrictedChars.test(this.value)) {
        this.value = this.value.replace(restrictedChars, '');
    }
	}

    var nicknameInput = document.getElementById('nickname-new');
    var introduceInput = document.getElementById('introduce-new');

    nicknameInput.addEventListener('input', restrictInput);
    introduceInput.addEventListener('input', restrictInput);
});



// 닉네임 공백 방지
document.getElementById('nickname-new').addEventListener('input', function() {
    this.value = this.value.replace(/\s/g, '');  // 공백 제거
});

// 이름 공백 방지
document.getElementById('name-new').addEventListener('input', function() {
    this.value = this.value.replace(/\s/g, '');  // 공백 제거
});


// 주소 키보드 엔터 방지
document.addEventListener('DOMContentLoaded', function() {
    var addr2Textarea = document.getElementById('detailAddress-new');
    
    addr2Textarea.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            return false;
        }
    });
});

// 주소 클릭
document.addEventListener('DOMContentLoaded', function() {
    var postcode = document.getElementById('postCode-new');
    var postcodeBt = document.getElementById('postcodeBt');
    
    postcode.addEventListener('click', function() {
        postcodeBt.click();
    });
});

// 주소 팝업
function sample6_execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var addr = ''; // 주소 변수
                    var extraAddr = ''; // 참고항목 변수

                    //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        addr = data.roadAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        addr = data.jibunAddress;
                    }

                    // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                    if(data.userSelectedType === 'R'){
                        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                        if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                            extraAddr += data.bname;
                        }
                        // 건물명이 있고, 공동주택일 경우 추가한다.
                        if(data.buildingName !== '' && data.apartment === 'Y'){
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                        if(extraAddr !== ''){
                            extraAddr = ' (' + extraAddr + ')';
                        }
                        // 조합된 참고항목을 해당 필드에 넣는다.
                        document.getElementById("add_extra").value = extraAddr;
                    
                    } else {
                        document.getElementById("add_extra").value = '';
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('postCode-new').value = data.zonecode;
                    document.getElementById("address-new").value = addr;
                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById("detailAddress-new").focus();
                }
            }).open();
        }