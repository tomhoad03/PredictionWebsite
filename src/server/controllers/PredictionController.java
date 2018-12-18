package server.controllers;

import server.models.Prediction;
import server.models.services.PredictionService;

import server.models.User;
import server.models.services.UserService;

import server.models.Choice;
import server.models.services.ChoiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("predict/")
public class PredictionController {

    @POST
    @Path("make")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    public String make(@CookieParam("sessionToken") String sessionToken,
                       @CookieParam("choiceId") int choiceId,
                       @CookieParam("questionNum") int questionNum) {

        int userId = getUserId(sessionToken);

        return PredictionService.insert(new Prediction(Prediction.nextId(), userId, questionNum, choiceId));
    }

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
