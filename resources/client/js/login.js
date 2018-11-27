function resetLoginForm() {

    if (Cookies.get("destination") === undefined) {
        window.location.href = "/client/html/login.html";
    }

    const loginForm = $('#loginForm');
    loginForm.submit(event => {
        event.preventDefault();
        $.ajax({
            url: '/user/login',
            type: 'POST',
            data: loginForm.serialize(),
            success: response => {
                if (response.startsWith('Error:')) {
                    alert(response);
                } else {
                    Cookies.set("sessionToken", response);
                    window.location.href = Cookies.get("destination");
                }
            }
        });
    });

}