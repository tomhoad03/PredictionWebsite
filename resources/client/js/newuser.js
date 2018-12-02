// Runs an API request by the client when the form is submitted.
function resetNewUserForm() {

    // Setups up the form as a collective object.
    const newuserForm = $('#newuserForm');

    // Runs when a submission has occurred and prevents multiple submissions.
    newuserForm.unbind("submit")
    newuserForm.submit(event => {

        // Stops the form from automatically submitting the data entered.
        event.preventDefault();

        $.ajax({

            // Defines the API at /user/new as the POST API request to be made.
            url: '/user/new',
            type: 'POST',

            // Gets the names of the input boxes from the form and sends them to the server.
            data: newuserForm.serialize(),
            success: response => {
                if (response === 'OK') {

                    // Sends the user to login if the API request is successful.
                    window.location.href = "/client/html/login.html";
                } else {
                    alert(response);
                }
            }
        });
    });
}