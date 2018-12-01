package server.controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import server.Logger;
import server.models.User;
import server.models.services.UserService;

import java.util.UUID;

//States the wider API path.
@Path("/user/")
public class UserController {

    // Defines the API request at /user/login.
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Inputs the results of the input boxes and parameters and uses them to login the user.
    public String loginHandler(@FormParam("loginUsername") String loginUsername,
                               @FormParam("loginPassword") String loginPassword) {

        // Selects all of the users that exist within the database.
        UserService.selectAllInto(User.users);

        // This algorithm simply compares the entered parameters to every user within the database.
        for (User u: User.users) {

            // Checks if the username matches.
            if (u.getUsername().toLowerCase().equals(loginUsername.toLowerCase())) {

                // Checks if the password matches.
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
    public String checkLogin(@CookieParam("sessionToken") Cookie sessionCookie) {

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
}