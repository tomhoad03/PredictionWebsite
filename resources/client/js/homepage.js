function pageLoad() {

    let currentPage = window.location.href;
    Cookies.set("destination", currentPage);


    $('h5').text(Cookies.get("destination"));
}