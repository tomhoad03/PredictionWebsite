function pageLoad() {

    // Stores the URL of the current page.
    let currentPage = window.location.href;
    Cookies.set("destination", currentPage);

    checkLogin(currentPage);
    setActive();
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

// Sets a drop-down items state to active, if it is selected.
function setActive() {
    // Gets a list of all the drop-down items on the webpage.
    let dropdownItems = document.getElementsByClassName("dropdown-item");

    // Cycles through every item and adds an event listener to them.
    for (let i = 0; i < dropdownItems.length; i++) {

        // The event listener runs the following function when clicked.
        dropdownItems[i].addEventListener("click", function() {

            // Returns the card that the item was clicked in.
            let itemsCard = null;

            if (i >= 0 && i <= 20) {
                itemsCard = 1;
            }
            else if (i >= 20 && i <= 40) {
                itemsCard = 2;
            }
            else if (i >= 40 && i <= 60) {
                itemsCard = 3;
            }
            else if (i >= 60 && i <= 80) {
                itemsCard = 4;
            }
            else if (i >= 80 && i <= 100) {
                itemsCard = 5;
            }

            // Gets the card that the item exists in.
            let selectedCard = document.getElementById("dropmenu" + itemsCard);

            // Gets a list of all the drop-down items from that one card.
            let selectedItems = selectedCard.getElementsByClassName("dropdown-item");

            // Sets the state of every item in the card to inactive.
            for (let j = 0; j < selectedItems.length; j++) {
                selectedItems[j].className = selectedItems[j].className.replace(" active", "");
            }

            // Sets the state of the item that was clicked on to active.
            this.className += " active";

            // Stores the choiceId and questionNum in a cookie.
            Cookies.set("choiceID", selectedCard.getElementsByClassName("dropdown-item active"));
            Cookies.set("questionNum", selectedCard);

            // Runs a function to make a prediction.
            makePrediction();
        })
    }
}

function makePrediction(){

    $.ajax({

        url: '/predict/make',
        type: 'POST',

        success: response => {

        }
    });

}
