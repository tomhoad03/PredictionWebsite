package server.controllers;

import server.Logger;
import server.models.Prediction;
import server.models.services.PredictionService;

import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("predict/")
public class PredictionController {

    // Defines the API request at /predict/make.
    @POST
    @Path("make")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Takes the choice, question and user details to make a prediction for that user.
    public String make(@CookieParam("sessionToken") Cookie sessionToken,
                       @CookieParam("choiceCookie") Cookie choiceCookie,
                       @CookieParam("questionCookie") Cookie questionCookie) {

        // Returns the userId of the user.
        int userId = getUserId(sessionToken.getValue());

        // Converts the values of the cookies into integer values.
        int questionNum = Integer.parseInt(questionCookie.getValue());
        int choiceId = Integer.parseInt(choiceCookie.getValue());

        // Searches through every prediction in the database to find ones made by the user.
        PredictionService.selectAllInto(Prediction.predictions);

        for (Prediction p : Prediction.predictions) {

            // Finds all the current predictions made by this user.
            if (p.getUserID() == userId) {

                // Deletes the existing prediction for the same question.
                if (p.getQuestionNum() == questionNum) {
                    PredictionService.deleteById(p.getPredictionID());
                }
            }
        }

        // Makes a prediction in the database.
        return PredictionService.insert(new Prediction(Prediction.nextId(), userId, questionNum, choiceId));
    }

    // The private function to get the userId of the user from a session token.
    private static int getUserId(String sessionToken) {
        UserService.selectAllInto(User.users);

        for (User u : User.users) {
            if (u.getSessionToken().equals(sessionToken)) {
                return u.getUserID();
            }
        }

        return -1;
    }
}
