	
	
		function checkLogin() {
		
		var loginOkValue = document.getElementById('loginOk').value;
		var loginOk = (loginOkValue === 'true');
	
    	if (!loginOk) {
        		alert('로그인 후 이용해주세요.');
				window.opener.location.reload();
				window.close();
        		return false;
    		}
    
   		 return true;
		}
	
		document.querySelector('.form_search').addEventListener('keydown', function(e) {
   			 if (e.key === 'Enter' || e.keyCode === 13) {
      		  e.preventDefault();
        
        		var currentPath = window.location.pathname;
       			var pathSegments = currentPath.split('/');
        
        		var segmentOfInterest = pathSegments[2];
        
        	dynamicSearch(segmentOfInterest);  
   		 }
		});	
		
		
		window.onload = function() {
   			 var url = window.location.href;
   			 var invoiceNumber = url.split('delivery_popup/')[1];
   			 if (invoiceNumber) {
       			 document.getElementById('delivery_search').value = invoiceNumber;
    			}
		};
		
		
		function dynamicSearch(){
			
			  if (!checkLogin()) return;
			
			  var invoice = document.querySelector('.form_search').value;
				
				if (!invoice.trim()) {
        			alert('송장번호를 입력해주세요.');
       				 return;  
    			}
				
			  var regex = /^[A-Za-z0-9]+$/;
				
    			if (!regex.test(invoice)) {
       				 alert('송장번호는 숫자14자 + 알파벳12자 입니다.');
        			return;  
  				}
  				
  				if (invoice.length != 26) {
        			alert('송장번호는 숫자14자 + 알파벳12자 입니다.');
        			return;
    			}
  						
			  
			  location.href = "/myPage/order/delivery_popup/" + invoice;
		}
		
		function inqCancle() {
        	window.close(); 
		}