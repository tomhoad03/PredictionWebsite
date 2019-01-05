package server.controllers;

import server.Logger;

import server.models.Prediction;
import server.models.services.PredictionService;

import server.models.Leaderboard;
import server.models.services.LeaderboardService;

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
                    if (questionNum < 4) {
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

    // Defines the API request at /predict/score.
    @POST
    @Path("score")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Determines if the answer matches. If the user is correct, points are added to their total.
    public String score() {

        // The question answers. The number represents the choiceId of the answer.
        int question1Answer = 1;
        int question2Answer = 1;
        int question3Answer = 1;
        int question4Answer = 21;
        int question5Answer = 31;

        String success = "";

        // Gets all the predictions from the database.
        PredictionService.selectAllInto(Prediction.predictions);

        for (Prediction p : Prediction.predictions) {

            // Checks the question number of the prediction.
            switch (p.getQuestionNum()) {

                case 1:

                    // If their answer is correct for question 1, the scoring function is run.
                    if (question1Answer == p.getChoiceID()) {
                        success = addScore(p.getUserID());
                    }
                    break;

                case 2:

                    // If their answer is correct for question 2, the scoring function is run.
                    if (question2Answer == p.getChoiceID()) {
                        success = addScore(p.getUserID());
                    }
                    break;

                case 3:

                    // If their answer is correct for question 3, the scoring function is run.
                    if (question3Answer == p.getChoiceID()) {
                        success = addScore(p.getUserID());
                    }
                    break;

                case 4:

                    // If their answer is correct for question 4, the scoring function is run.
                    if (question4Answer == p.getChoiceID()) {
                        success = addScore(p.getUserID());
                    }
                    break;

                case 5:

                    // If their answer is correct for question 5, the scoring function is run.
                    if (question5Answer == p.getChoiceID()) {
                        success = addScore(p.getUserID());
                    }
                    break;

                default:
                    Logger.log("This was an invalid prediction.");
            }

            // Deletes the prediction.
            if (success.equals("OK")) {
                PredictionService.deleteById(p.getPredictionID());
            }

        }

        return "Scored";
    }

    private String addScore(int userId) {

        Logger.log("Scoring");

        // Gets the user from the leaderboard.
        Leaderboard currentUser = LeaderboardService.selectById(userId);

        // Adds points to the user.
        currentUser.setTotalPoints(currentUser.getTotalPoints() + 5);

        // Updates the record in the leaderboard.
        return LeaderboardService.update(currentUser);
    }
}
