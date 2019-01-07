function pageLoad() {
    setColours();

    // Checks if a session token is available.
    checkLogin();
}

function calculatePosition() {
    $.ajax({
        url: '/leaderboard/position',
        type: 'POST',
    });
}

// Will logout the user if they hit the button by removing the existing session token.
function logout() {
    Cookies.remove("sessionToken");
    window.location.href = '/client/html/login.html';
}

// The function that will check if the user has a session token needed for the page loaded.
function checkLogin() {

    // Gets the current session token.
    let token = Cookies.get("sessionToken");

    // If the token is not undefined, a user is logged in, so the users details are returned.
    if (token !== undefined) {

        $.ajax({

            // Defines the API at /user/validate as the GET API request to be made.
            url: '/user/validate/',
            type: 'GET',

            // If the API request is made, it'll check if a valid user exists.
            success: userId => {
                if (userId === -1) {

                    // Forces the user to try to login again.
                    window.location.href = '/client/html/login.html';
                } else {

                    // Returns the username and stores it in a cookie.
                    Cookies.set("idCookie", userId);

                    // Calls the function to load the leaderboard.
                    loadLeaderboard()
                }
            }
        });

    } else {

        // If the token is undefined, no user is logged in.
        logout();
    }
}

// Loads the leaderboard on the webpage.
function loadLeaderboard() {

    // The API request to display the leaderboard.
    $.ajax({
        url: '/leaderboard/display',
        type: 'GET',

        success: leaderboardList => {

            // Checks if there is an error in the leaderboard.
            if (leaderboardList.hasOwnProperty('error')){
                alert(leaderboardList.error);
            }
            else {
                // Creates the beginning of a table.
                let leaderboardHTML = `<div class="container">`
                                        + `<table class="table table-bordered shadow">`
                                            + `<thead class="thead-dark">`
                                                + `<tr>`
                                                    + `<th scope="col">Position:</th>`
                                                    + `<th scope="col">Username:</th>`
                                                    + `<th scope="col">TotalPoints:</th>`
                                                + `</tr>`
                                            + `</thead>`
                                        + `<tbody>`;

                // For every user on the leaderboard, their details are added in this format.
                for (let leaderboard of leaderboardList) {

                    // Gets the UserID, stored in the list.
                    let userId = leaderboard.userID;

                    // Checks if the record matches that of the existing user.
                    if (userId === parseInt(Cookies.get("idCookie"))) {

                        // Highlights the row.
                        leaderboardHTML += `<tr class="table-active">`
                    }
                    else {

                        // Creates a normal row.
                        leaderboardHTML += `<tr>`
                    }

                    // Finishes of the rest of the row.
                    leaderboardHTML += `<th scope="row">${leaderboard.position}</th>`
                                    + `<td>${leaderboard.username}</td>`
                                    + `<td>${leaderboard.totalPoints}</td>`
                                    + `</tr>`;
                }

                // Adds the final remains of the table.
                leaderboardHTML +=      `</tbody>`
                                    + `</table>`
                                + `</div>`;

                // Adds the contents to the div with id #leaderboard.
                $('#leaderboardDisplay').html(leaderboardHTML);


            }
        }
    });

}