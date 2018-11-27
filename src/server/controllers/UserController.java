package server.controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import server.Logger;
import server.models.User;
import server.models.services.UserService;

import java.util.UUID;

@Path("user/")
public class UserController {

    //Defines the HTTP request as post.
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    //Inputs the parameters of the form and creates a session token if they match an existing user.
    public String loginHandler(@FormParam("loginUsername") String loginUsername,
                               @FormParam("loginPassword") String loginPassword) {

        UserService.selectAllInto(User.users);
        for (User a: User.users) {
            if (a.getUsername().toLowerCase().equals(loginUsername.toLowerCase())) {
                if (!a.getPassword().equals(loginPassword)) {
                    return "Error: Incorrect Password!";
                }

                String token = UUID.randomUUID().toString();
                a.setSessionToken(token);

                String success = UserService.update(a);

                if (success.equals("OK")) {
                    return token;
                } else {
                    return "Error: Can't create a session token.";
                }
            }
        }
        return "Error: User does not yet exist!";
    }

    @GET
    @Path("check")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkLogin(@CookieParam("sessionToken") Cookie sessionCookie) {

        String sessionUser = validateSessionCookie(sessionCookie);

        if (sessionUser == null) {
            Logger.log("Error: Invalid user session token");
            return "";
        } else {
            return sessionUser;
        }
    }

    public static String validateSessionCookie(Cookie sessionCookie) {
        if (sessionCookie != null) {
            String token = sessionCookie.getValue();
            String result = UserService.selectAllInto(User.users);
            if (result.equals("OK")) {
                for (User a : User.users) {
                    if (a.getSessionToken().equals(token)) {
                        Logger.log("Valid session token received.");
                        return a.getUsername();
                    }
                }
            }
        }
        return null;
    }

}