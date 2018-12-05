package server.controllers;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import server.Logger;

import server.models.User;
import server.models.services.UserService;

import java.util.UUID;

//States the wider API path.
@Path("user/")
public class UserController {

    // Defines the API request at /user/login.
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Inputs the results of the input boxes and parameters and uses them to login the user.
    public String login(@FormParam("loginUsername") String loginUsername,
                        @FormParam("loginPassword") String loginPassword) {

        // Selects all of the users that exist within the database.
        UserService.selectAllInto(User.users);

        // This algorithm simply compares the entered parameters to every user within the database.
        for (User u: User.users) {

            // Checks if the username matches to an existing user.
            if (u.getUsername().toLowerCase().equals(loginUsername.toLowerCase())) {

                // Checks if the password matches to an existing user.
                if (!u.getPassword().equals(loginPassword)) {
                    return "Error: Incorrect Password";
                }

                // Creates a session token, unique to the current user.
                String sessionToken = UUID.randomUUID().toString();
                u.setSessionToken(sessionToken);

                String updateSuccess = UserService.update(u);

                // If the server makes a successful change, it will return the session token.
                if (updateSuccess.equals("OK")) {
                    return sessionToken;
                } else {
                    return "Error: Can't create a session token.";
                }
            }
        }
        return "Error: Incorrect Username";
    }

    // Defines the API request at /user/validate.
    @GET
    @Path("validate")
    @Produces(MediaType.TEXT_PLAIN)

    // Takes the existing session token stored in a cookie and returns the users credentials.
    public String validate(@CookieParam("sessionToken") Cookie sessionCookie) {

        // Sets the session users details if the session token is valid.
        String sessionUser = validateSessionCookie(sessionCookie);

        // Returns the username of the user if one has a valid session token.
        if (sessionUser == null) {
            Logger.log("Error: Invalid user session token.");
            return "";
        } else {
            return sessionUser;
        }
    }

    // Checks if the session token in the session cookie is valid.
    public static String validateSessionCookie(Cookie sessionCookie) {

        // If the session cookie stores a value, it proceeds, else it returns null.
        if (sessionCookie != null) {

            // Gets the value stored within the cookie.
            String sessionToken = sessionCookie.getValue();

            // Gets all of the users from the database.
            String result = UserService.selectAllInto(User.users);

            if (result.equals("OK")) {

                // Checks the sessionToken against the sessionToken of every users stored token.
                for (User u: User.users) {

                    // If a user has that session token, the username is returned.
                    if (u.getSessionToken().equals(sessionToken)) {
                        Logger.log("Valid session token received.");
                        return u.getUsername();
                    }
                }
            }
        }
        return null;
    }

    // Defines the API request at /user/create.
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Inputs that values from the form and creates a new user if validated.
    public String create(@FormParam("newUsername") String newUsername,
                         @FormParam("newEmail") String newEmail,
                         @FormParam("newPassword") String newPassword,
                         @FormParam("confirmPassword") String confirmPassword) {

        // Checks if the form has been completely filled out.
        if (newUsername.equals("") || newEmail.equals("") || newPassword.equals("") || confirmPassword.equals("")){
            return "Error: Missing credentials.";

        // Checks if the password and confirm password match up.
        } else if (!newPassword.toLowerCase().equals(confirmPassword.toLowerCase())) {
            return "Error: The passwords don't match.";
        }

        // Inputs all the values from the database table.
        UserService.selectAllInto((User.users));

        // Comparing against every user, it checks if a username or email is taken.
        for (User u: User.users) {
            if (u.getUsername().toLowerCase().equals(newUsername.toLowerCase())) {
                return "Error: An existing user already has this username.";
            } else if (u.getEmail().toLowerCase().equals(newEmail.toLowerCase())) {
                return "Error: An existing user already has this email address.";
            }
        }

        // Instantiates a new user as an object and adds it to the database.
        return UserService.insert(new User(User.nextId(), newUsername, newEmail, newPassword, null));
    }


    // Defines the API request at /user/edit.
    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    public String edit(@FormParam("checkUsername") String checkUsername,
                       @FormParam("checkEmail") String checkEmail,
                       @FormParam("newPassword") String newPassword,
                       @FormParam("confirmPassword") String confirmPassword) {

        // Inputs all the values from the database table.
        UserService.selectAllInto((User.users));

        for (User u: User.users) {

            // Checks if the username matches to an existing user.
            if (u.getUsername().toLowerCase().equals(checkUsername.toLowerCase())) {

                // Checks if the email matches to an existing user.
                if (u.getEmail().toLowerCase().equals(checkEmail.toLowerCase())) {

                    // Checks if the password and confirm password match up.
                    if (newPassword.toLowerCase().equals(confirmPassword.toLowerCase())) {

                        // Checks if the new password matches the existing password.
                        if (!newPassword.toLowerCase().equals(u.getPassword().toLowerCase())) {

                            // Sets the password to the new one.
                            u.setPassword(newPassword);

                            // Returns the status of a change in the database.
                            String updateSuccess = UserService.update(u);

                            // Checks if the change has been successful.
                            if (!updateSuccess.equals("OK")) {
                                return "Error: Can't change the password.";
                            }
                        }

                        return "Error: The passwords matches the existing password";
                    }

                    return "Error: The passwords don't match.";
                }

                return "Error: A user with this email address does not exist.";
            }
        }

        return "Error: A user with this username does not exist.";
    }
}
