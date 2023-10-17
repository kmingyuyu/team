	
	// 엔터키 적용
	document.querySelector('.inp_nm').addEventListener('keydown', function(e) {
    if (e.key === 'Enter' || e.keyCode === 13) {
        e.preventDefault();
        
        var currentPath = window.location.pathname;
        var pathSegments = currentPath.split('/');
        
        var segmentOfInterest = pathSegments[2];
        
        dynamicSearch(segmentOfInterest);  
    }
	});	
	
	function dynamicSearch(url) {
		
    const currentDate = new Date();
 	let adjustedStartDate = stDateElement.value ? formatDate(new Date(stDateElement.value)) : formatDate(currentDate);
	let adjustedEndDate = endDateElement.value ? formatDate(new Date(endDateElement.value)) : formatDate(currentDate);
    
	// 상품명 값 가져오기
	const searchQuery = document.querySelector('.inp_nm').value;
	
    // 선택된 버튼의 값을 가져오기
    const buttons = document.querySelectorAll('button[type="button"]');
    let selectedValue = null;
    for (let i = 0; i < buttons.length; i++) {
        if (buttons[i].classList.contains('on')) {
            selectedValue = buttons[i].value;
            break;
        }
    }

    if (selectedValue) {
        const newStartDate = new Date(currentDate);
        newStartDate.setDate(newStartDate.getDate() - selectedValue);
        
        adjustedStartDate = formatDate(newStartDate); 
    } 

    let queryString = `?startTime=${adjustedStartDate}&endTime=${adjustedEndDate}&searchQuery=${encodeURIComponent(searchQuery)}`;
	
	const dataElement = document.getElementById('data');
	
    if (dataElement) {
        const dataValue = dataElement.value;
        queryString = `?data=${dataValue}&startTime=${adjustedStartDate}&endTime=${adjustedEndDate}&searchQuery=${encodeURIComponent(searchQuery)}`;
    }
	
	
    window.location.href = '/myPage/'+ url + `${queryString}`;
}

function goList(data,url){
	
	var startTime = document.querySelector('.st_date').value;
    var endTime = document.querySelector('.end_date').value;
    var searchQuery = document.querySelector('.inp_nm').value;
	
	window.location.href = '/myPage/'+ url 
					+ '?data=' + data
					+ '&startTime=' + startTime
					+ '&endTime=' + endTime
					+ '&searchQuery=' + searchQuery;
}


	document.addEventListener('DOMContentLoaded', function() {
		
    const startTimeElement = document.querySelector('.st_date');
    const endTimeElement = document.querySelector('.end_date');

    const startTime = new Date(startTimeElement.value);
    const endTime = new Date(endTimeElement.value);

    const diffDays = Math.floor((endTime - startTime) / (1000 * 60 * 60 * 24));

    const buttons = document.querySelectorAll('button[type="button"]');
    for (let i = 0; i < buttons.length; i++) {
        const btnValue = parseInt(buttons[i].value, 10);
        if (btnValue === diffDays) {
            buttons[i].classList.add('on');
            break;
        }
    }
	});
	
	const currentDate = new Date();

function formatDate(date) {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

const stDateElement = document.querySelector('.st_date');
const endDateElement = document.querySelector('.end_date');

stDateElement.max = formatDate(currentDate);
endDateElement.max = formatDate(currentDate);

endDateElement.value = formatDate(currentDate);

stDateElement.style.visibility = 'visible';
endDateElement.style.visibility = 'visible';

document.querySelector('.date_check_list').addEventListener('click', function(event) {
    if (event.target.tagName === 'BUTTON') {
        const value = parseInt(event.target.value, 10);

        const buttons = document.querySelectorAll('.date_check_list button');
        buttons.forEach(btn => btn.classList.remove('on'));

        event.target.classList.add('on');

        if (value === 0) {
            stDateElement.value = formatDate(currentDate);
        } else {
            const targetDate = new Date(currentDate.getTime() - value * 24 * 60 * 60 * 1000);
            stDateElement.value = formatDate(targetDate);
        }
    }
});

[stDateElement, endDateElement].forEach(element => {
    element.addEventListener('change', function() {
        const buttons = document.querySelectorAll('.date_check_list button');
        buttons.forEach(btn => btn.classList.remove('on'));
    });
});


	
	