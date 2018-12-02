// Runs an API request by the client when the form is submitted.
function resetNewUserForm() {

    const newuserForm = $('#newuserForm');
    newuserForm.submit(event => {

        event.preventDefault();

        $.ajax({

            url: '/user/new',
            type: 'POST',

            data: newuserForm.serialize(),
            success: response => {
                if (response === 'OK') {
                    window.location.href = "/client/html/login.html";
                } else {
                    alert(response);
                }
            }
        });
    });
}