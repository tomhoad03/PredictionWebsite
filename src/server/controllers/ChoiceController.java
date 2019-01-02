package server.controllers;

import server.Logger;
import server.models.Choice;
import server.models.services.ChoiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("choice/")
public class ChoiceController {

    @GET
    @Path("name")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    public String name(@CookieParam("choiceCookie") Cookie choiceCookie,
                          @CookieParam("questionCookie") Cookie questionCookie) {

        // Converts the values of the cookies into integer values.
        int choiceId = Integer.parseInt(choiceCookie.getValue());
        int questionNum = Integer.parseInt(questionCookie.getValue());

        // Increases the choiceId if the question has come from the team category.
        if (questionNum == 4) {
            choiceId += 20;
        }

        // Returns the name of the corresponding driver.
        ChoiceService.selectAllInto(Choice.choices);

        for (Choice c : Choice.choices) {
            if (c.getChoiceID() == (choiceId)) {
                return c.getChoiceName();
            }
        }

        return null;
    }
}
