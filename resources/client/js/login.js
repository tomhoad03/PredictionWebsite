// Runs an API request by the client when the form is submitted.
function resetLoginForm() {

    // Checks if a there is a destination that the user wants to go to after logging in.
    if (Cookies.get("destination") === undefined) {
        window.location.href = "/client/html/login.html";
    }

    // Sets up the login form API request.
    const loginForm = $('#loginForm');
    loginForm.submit(event => {

        // Stops the form from automatically submitting the data entered.
        event.preventDefault();

        $.ajax({

            // Defines the API at /user/login as the POST API request to be made.
            url: '/user/login',
            type: 'POST',

            // Gets the names of the input boxes from the form and sends them to the server.
            data: loginForm.serialize(),
            success: response => {
                if (response.startsWith('Error:')) {
                    alert(response);
                } else {

                    // Sends the user to where they want to go, with a session token.
                    Cookies.set("sessionToken", response);
                    window.location.href = Cookies.get("destination");
                }
            }
        });
    });
}

// Sends the user to create an account, to stop the button submitting the login form.
function toNewUser() {
    window.location.href="/client/html/newuser.html"
}

// The function that will check if the user has a session token needed for the page loaded.
function checkLogin() {

    // Stores the URL of the current page and gets the current session token.
    let currentPage = window.location.pathname;
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