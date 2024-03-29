package server.controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import server.Logger;

import server.models.User;
import server.models.services.UserService;

import server.models.Leaderboard;
import server.models.services.LeaderboardService;

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
        for (User u : User.users) {

            // Checks if the username matches to an existing user.
            if (u.getUsername().toLowerCase().equals(loginUsername.toLowerCase())) {

                // Checks if the password matches to an existing user.
                if (!u.getHashedPassword().equals(generateHash((loginPassword + u.getSalt())))) {
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
    public int validate(@CookieParam("sessionToken") Cookie sessionCookie) {

        // Sets the session users details if the session token is valid.
        int sessionUserId = validateSessionCookie(sessionCookie);

        // Returns the username of the user if one has a valid session token.
        if (sessionUserId == -1) {
            Logger.log("Error: Invalid user session token.");
            return -1;
        } else {
            return sessionUserId;
        }
    }

    // Checks if the session token in the session cookie is valid.
    private static int validateSessionCookie(Cookie sessionCookie) {

        // If the session cookie stores a value, it proceeds, else it returns null.
        if (sessionCookie != null) {

            // Gets the value stored within the cookie.
            String sessionToken = sessionCookie.getValue();

            // Gets all of the users from the database.
            String result = UserService.selectAllInto(User.users);

            if (result.equals("OK")) {

                // Checks the sessionToken against the sessionToken of every users stored token.
                for (User u : User.users) {

                    // If a user has that session token, the username is returned.
                    if (u.getSessionToken().equals(sessionToken)) {
                        Logger.log("Valid session token received.");
                        return u.getUserID();
                    }
                }
            }
        }
        return -1;
    }

    // Defines the API request at /user/create.
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Inputs the values from the form and creates a new user if validated.
    public String create(@FormParam("newUsername") String newUsername,
                         @FormParam("newEmail") String newEmail,
                         @FormParam("newPassword") String newPassword,
                         @FormParam("confirmPassword") String confirmPassword) {

        // Checks if the form has been completely filled out.
        if (newUsername.equals("") || newEmail.equals("") || newPassword.equals("") || confirmPassword.equals("")) {
            return "Error: Missing credentials.";

        // Checks if the password and confirm password match up.
        } else if (!newPassword.toLowerCase().equals(confirmPassword.toLowerCase())) {
            return "Error: The passwords don't match.";
            
        // Checks if the credentials contain a space, which makes them invalid. This also covers lazy SQL injections.    
        } else if (newUsername.contains(" ") || newEmail.contains(" ") || newPassword.contains(" ") || confirmPassword.contains(" ")) {
            return "Error: The credentials have been entered in the incorrect format.";
                
        // Checks if an email contains the @ symbol.
        } else if (!newEmail.contains("@")) {
            return "Error: An invalid email address has been entered.";
        }

        // Inputs all the values from the database table.
        UserService.selectAllInto(User.users);

        // Comparing against every user, it checks if a username or email is taken.
        for (User u : User.users) {
            if (u.getUsername().toLowerCase().equals(newUsername.toLowerCase())) {
                return "Error: An existing user already has this username.";
            } else if (u.getEmail().toLowerCase().equals(newEmail.toLowerCase())) {
                return "Error: An existing user already has this email address.";
            }
        }

        // Generates the salt for the end of the password.
        String salt = UUID.randomUUID().toString();

        // Creates the hashed password by using the generateHash() algorithm.
        String hashedPassword = generateHash(newPassword + salt);

        int userId = User.nextId();

        // Instantiates the new user as an object and adds it to the database leaderboard.
        UserService.insert(new User(userId, newUsername, newEmail, hashedPassword, salt, null));

        // Instantiates a new user as an object and adds it to the database.
        return LeaderboardService.insert(new Leaderboard(userId, 0, 0));
    }

    // Hashes the password with the added salt.
    private String generateHash(String saltedPassword) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = hasher.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte hash : encodedHash) {
                String hex = Integer.toHexString(0xff & hash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException nsae) {
            return nsae.getMessage();
        }
    }

    // Defines the API request at /user/edit.
    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Inputs the values from the form and changes the users password.
    public String edit(@FormParam("checkUsername") String checkUsername,
                       @FormParam("checkEmail") String checkEmail,
                       @FormParam("newPassword") String newPassword,
                       @FormParam("confirmPassword") String confirmPassword) {

        // Checks if the password and confirm password match up.
        if (!newPassword.toLowerCase().equals(confirmPassword.toLowerCase())) {
            return "Error: The passwords don't match.";
        }

        // Inputs all the values from the database table.
        UserService.selectAllInto((User.users));

        // Searches through every user in the database to find a match.
        for (User u : User.users) {

            // Checks if the username or email matches to an existing user.
            if ((u.getUsername().toLowerCase().equals(checkUsername.toLowerCase()))
                    || u.getEmail().toLowerCase().equals(checkEmail.toLowerCase())) {

                // Hashes the new password.
                String newHashedPassword = generateHash(newPassword + u.getSalt());

                // Checks if the new password matches the existing password.
                if (!u.getHashedPassword().equals(newHashedPassword)) {

                    // Sets the password to the new one.
                    u.setHashedPassword(newHashedPassword);

                    // Returns the status of a change in the database.
                    String updateSuccess = UserService.update(u);

                    // Checks if the change has been successful.
                    if (!updateSuccess.equals("OK")) {
                        return "Error: Can't change the password.";
                    } else {
                        return updateSuccess;
                    }
                } else {
                    return "Error: The password matches the existing password.";
                }
            }
        }

        return "Error: A user with this username or email does not exist.";
    }

}
