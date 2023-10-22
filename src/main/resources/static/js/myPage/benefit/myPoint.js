	function goList(data,url){
	
		window.location.href = '/myPage/'+ url 
					+ '?data=' + data;
	}
		
		
	function pointPage(page) {
	
		var data = document.getElementById('data').value;
    
	
		location.href = '/myPage/point/'+ page 
					+ '?data=' + data;
	}