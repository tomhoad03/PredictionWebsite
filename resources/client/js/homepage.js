function pageLoad() {

    let currentPage = window.location.href;
    Cookies.set("destination", currentPage);

    $.ajax({

        url: '/user/get',
        type: 'GET',

        success: response => {
            Cookies.set("username", response);
            $('h5').text("CurrentUser");
        }
    });
}

/*function getUser() {
    $.ajax({

        url: '/user/get',
        type: 'GET',

        success: username => {
            Cookies.set("username", username);
            $('h5').text("CurrentUser");
        }
    });
}*/