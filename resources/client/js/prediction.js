function pageLoad() {

    // Stores the URL of the current page.
    let currentPage = window.location.href;
    Cookies.set("destination", currentPage);

    checkLogin(currentPage);
    loadPredictions();
    setActive();
}

// Will logout the user if they hit the button by removing the existing session token.
function logout() {
    Cookies.set("sessionToken", null);
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
            success: userId => {
                if (userId === -1) {

                    // Forces the user to try to login again.
                    if (currentPage !== '/client/html/login.html') {
                        window.location.href = '/client/html/login.html';
                    }
                } else {
                    // Returns the username and stores it in a cookie.
                    Cookies.set("idCookie", userId);
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

// Sets a drop-down items state to active, if it is selected.
function setActive() {

    // Gets a list of all the drop-down items on the webpage.
    let dropdownItems = document.getElementsByClassName("dropdown-item");

    // Cycles through every item and adds an event listener to them.
    for (let i = 0; i < dropdownItems.length; i++) {

        // The event listener runs the following function when clicked.
        dropdownItems[i].addEventListener("click", function(){
            // Uses an anonymous function to run the actual function to run, so that parameters can be passed in.
            activate(i, false);
        });
    }
}

// The function that will run if the event listener on a button is activated.
function activate(itemNum, loadingState) {

    // Returns the card that the item was clicked in.
    let itemsCard = null;
    let img = null;

    // Returns the card that choice is made in. Also, using jQuery, it selects the image for the related card.
    switch (true) {
        case (itemNum >= 0 && itemNum < 20):
            itemsCard = 1;
            img = $('#card1img');
            break;
        case (itemNum >= 20 && itemNum < 40):
            itemsCard = 2;
            img = $('#card2img');
            break;
        case (itemNum >= 40 && itemNum < 60):
            itemsCard = 3;
            img = $('#card3img');
            break;
        case (itemNum >= 60 && itemNum < 70):
            itemsCard = 4;
            img = $('#card4img');
            break;
        case (itemNum >= 70 && itemNum < 74):
            itemsCard = 5;
            break;
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

    // Sets the question number as a cookie.
    Cookies.set("questionCookie", itemsCard);

    // Arithmetically returns the ID of the choice made and displays the image of the choice.
    if (itemsCard < 5) {

        // Sets the choice ID as a cookie.
        let choiceId = (itemNum + 1) - ((itemsCard - 1) * 20);
        Cookies.set("choiceCookie", choiceId);

        // Makes the API request to ChoiceController.
        $.ajax({
            url: '/choice/name',
            type: 'GET',

            success: response => {
                if (response !== null) {

                    // Gets the path of the image location for a specific driver.
                    let imagePath = "/client/images/" + response + ".jpg";

                    // Sets the image for the prediction made and makes it visible.
                    img.attr('src', imagePath);
                    img.attr('class', 'card-img-top');
                }
            }
        });
    }
    else {

        // Sets the choice ID as a cookie.
        let choiceId = (itemNum - 69);
        Cookies.set("choiceCookie", choiceId);
    }

    // Checks to see if the webpage is loading or making new predictions.
    if (loadingState === false) {
        // Runs a function to make a prediction.
        makePrediction();
    }

}

function loadPredictions() {
    for (let i = 0; i < 5; i++) {
        $.ajax({
            url: '/predict/load',
            type: 'GET',

            success: response => {
                if (response !== -1) {
                    activate(response, true);
                }
            }
        });
    }
}

// Makes the API request to PredictionController.
function makePrediction(){
    $.ajax({
        url: '/predict/make',
        type: 'POST',
    });
}
