
/*사이드바 클래스추가 */
document.addEventListener('DOMContentLoaded', function() {
    var currentURL = window.location.pathname;

    var listItems = document.querySelectorAll('.mypage-list-li'); 

    listItems.forEach(function(listItem) {
        for (var i = 0; i < listItem.classList.length; i++) {
            var className = listItem.classList[i];

            if (currentURL.includes('/' + className)) {
                var anchor = listItem.querySelector('a');
                if (anchor) {
                    anchor.classList.add('mypage-side-active');
                }
                break;  
            }
        }
    });
});




