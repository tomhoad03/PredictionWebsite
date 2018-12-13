function pageLoad() {

    // Stores the URL of the current page.
    let currentPage = window.location.href;
    Cookies.set("destination", currentPage);

    checkLogin(currentPage);
}

// The function that will check if the user has a session token needed for the page loaded.
function checkLogin(currentPage) {

    // Gets the current session token.
    let token = Cookies.get("sessionToken");

    // If the token is not undefined, a user is logged in, so the users details are returned.
    if (token !== undefined) {

        $.ajax({

            // Defines the API at /user/validate as the GET API request to be made.
            url: '/user/validate',
            type: 'GET',

            // If the API request is made, it'll check if a valid user exists.
            success: username => {
                if (username === "") {

                    // Forces the user to try to login again.
                    if (currentPage !== '/client/html/login.html') {
                        window.location.href = '/client/html/login.html';
                    }
                } else {
                    // Returns the username and stores it in a cookie.
                    Cookies.set("username", username);
                }
            }
        });

        // If the token is undefined, no user is logged in.
    } else {

        // Sends the user to go and login if not already there.
        if (currentPage !== '/client/html/login.html') {
            window.location.href = '/client/html/login.html';
        }
    }
}

function logout() {
    Cookies.set("sessionToken", null);
}