	
	
	
	
		document.querySelector('.form_search').addEventListener('keydown', function(e) {
   			 if (e.key === 'Enter' || e.keyCode === 13) {
      		  e.preventDefault();
        
        		var currentPath = window.location.pathname;
       			var pathSegments = currentPath.split('/');
        
        		var segmentOfInterest = pathSegments[2];
        
        	dynamicSearch(segmentOfInterest);  
   		 }
		});	
		
		
		function dynamicSearch(){
			
			  var invoice = document.querySelector('.form_search').value;
				
				if (!invoice.trim()) {
        			alert('송장번호를 입력해주세요.');
       				 return;  
    			}
				
  				  var regex = /^(\d{14})([a-zA-Z]{10})([Kk][Rr])$/;
				
    			if (!regex.test(invoice)) {
       				 alert('송장번호는 숫자14자 + 알파벳10자 + KR 입니다.');
        			return;  
  				}
  				
  				if (invoice.length != 26) {
        			alert('송장번호는 숫자14자 + 알파벳10자 + KR 입니다.');
        			return;
    			}
  					
			  
			  location.href = "/myPage/order/deliveryPopup/" + invoice;
		}
		
		function inqCancle() {
        	window.close(); 
		}