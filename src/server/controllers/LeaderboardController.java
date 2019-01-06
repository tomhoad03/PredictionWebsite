package server.controllers;

import server.Logger;
import server.models.Leaderboard;
import server.models.services.LeaderboardService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("leaderboard/")
public class LeaderboardController {

    @POST
    @Path("position")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    public String position(){

        // Selects all the records in the leaderboard, in order of total points.
        LeaderboardService.selectAllInto(Leaderboard.leaderboards);

        // Uses a counter to determine the position.
        int position = 1;

        for (Leaderboard l: Leaderboard.leaderboards) {
            // Sets the position of this users.
            l.setPosition(position);

            // Updates the users ranking in the database.
            LeaderboardService.update(l);

            // Increments the position by one.
            position++;
        }

        return "OK";
    }

}
