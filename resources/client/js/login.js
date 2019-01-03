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

function toNewUser() {
    window.location.href = "/client/html/newuser.html";
}