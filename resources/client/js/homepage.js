function homepageLoad() {

    // Adds event listeners to all the webpage's drop-down items.
    setActive();

    // Changes the colours of all the elements.
    setColours();
}

// Sets a drop-down items state to active if it is selected.
function setActive() {

    // Gets a list of all the drop-down items on the webpage.
    let dropdownItems = document.getElementsByClassName("dropdown-item");

    // Cycles through every item and adds an event listener to them.
    for (let i = 0; i < dropdownItems.length; i++) {

        // The event listener runs the following function when clicked.
        dropdownItems[i].addEventListener("click", function() {

            // Sets the state of every item in the card to inactive.
            for (let j = 0; j < dropdownItems.length; j++) {
                dropdownItems[j].className = dropdownItems[j].className.replace(" active", "");
            }

            // Sets the selected drop-down item to active.
            this.className += " active";

            // Gets the ID of the item selected.
            let teamColour = this.id;
            let colour = null;

            // Assigns the hex value for each team to the variable called colour.
            switch (teamColour) {
                case "colour1":
                    colour = "#00D2BE";
                    break;
                case "colour2":
                    colour = "#FF2800";
                    break;
                case "colour3":
                    colour = "#00327D";
                    break;
                case "colour4":
                    colour = "#D2DD24";
                    break;
                case "colour5":
                    colour = "#F596C8";
                    break;
                case "colour6":
                    colour = "#5A5A5A";
                    break;
                case "colour7":
                    colour = "#FF8700";
                    break;
                case "colour8":
                    colour = "#9B0000";
                    break;
                case "colour9":
                    colour = "#0032FF";
                    break;
                case "colour10":
                    colour = "#87CEEB";
                    break;
            }

            // Stores the colour in a cookie.
            Cookies.set("colour", colour);

            // Updates the colours on the webpage.
            setColours();
        });
    }
}

function setColours() {
    // Gets the colour from the cookie storing it.
    let colour = Cookies.get("colour");

    // Sets the default colour to Ferrari red.
    if (colour === undefined || colour === null) {
        colour = "#FF2800";

        Cookies.set("colour", colour);
    }

    // Changes the default colours in the CSS file using jQuery.
    $('h1').css('color', colour);

    $('nav').css('background-color', colour);
    $('.btn').css('background-color', colour);

    $('.form-group').css('border', '2px solid ' + colour);
    $('#intro').css('border', '2px solid ' + colour);
}