function pageLoad() {

    let currentPage = window.location.href;
    Cookies.set("breadcrumb", currentPage);

}