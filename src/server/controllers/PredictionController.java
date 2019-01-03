package server.controllers;

import server.models.Prediction;
import server.models.services.PredictionService;

import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("predict/")
public class PredictionController {

    // Defines the API request at /predict/load.
    @GET
    @Path("load/{i}")
    @Produces(MediaType.TEXT_PLAIN)

    // Passes in the loading count and userId to return the item number of the prediction.
    public int load(@CookieParam("idCookie") Cookie idCookie,
                    @PathParam("i") int questionCount) {

        // Converts the value of the userId, that is stored as a cookie, into an integer value.
        int userId = Integer.parseInt(idCookie.getValue());

        PredictionService.selectAllInto(Prediction.predictions);

        // Searches through every single prediction in the database.
        for (Prediction p : Prediction.predictions) {

            // Checks to see if the prediction is made by this user.
            if (p.getUserID() == userId) {

                // Gets the question number.
                int questionNum = p.getQuestionNum();

                // Checks if the question number matches the one it's activating.
                if (questionNum == questionCount) {
                    int choiceId = p.getChoiceID();

                    // Returns the item number based upon the category it is in.
                    if (questionNum != 5) {
                        return (20 * (questionNum - 1)) + choiceId;
                    }
                    else {
                        return choiceId + 40;
                    }
                }
            }
        }
        return -1;
    }

    // Defines the API request at /predict/make.
    @POST
    @Path("make")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Takes the choice, question and user details to make a prediction for that user.
    public String make(@CookieParam("idCookie") Cookie idCookie,
                       @CookieParam("choiceCookie") Cookie choiceCookie,
                       @CookieParam("questionCookie") Cookie questionCookie) {

        // Converts the values of the cookies into integer values.
        int userId = Integer.parseInt(idCookie.getValue());
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
}
