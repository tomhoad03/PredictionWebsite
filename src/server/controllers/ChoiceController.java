package server.controllers;

import server.models.Choice;
import server.models.services.ChoiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("choice/")
public class ChoiceController {

    @GET
    @Path("getname")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    public String getname(@CookieParam("choiceCookie") Cookie choiceCookie) {

        // Converts the values of the cookies into integer values.
        int choiceId = Integer.parseInt(choiceCookie.getValue());

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
