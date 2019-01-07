package server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import server.models.Leaderboard;
import server.models.services.LeaderboardService;

import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@SuppressWarnings("unchecked")
@Path("leaderboard/")
public class LeaderboardController {

    // Defines the API request at /leaderboard/position.
    @POST
    @Path("position")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    // Assigns the leaderboard position of the users.
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

    // Defines the API request at /leaderboard/display.
    @GET
    @Path("display")
    @Produces(MediaType.APPLICATION_JSON)

    // Creates the leaderboard as an object.
    public String display() {

        // Gets all of the records from the leaderboard.
        String status = LeaderboardService.selectAllInto(Leaderboard.leaderboards);

        if (status.equals("OK")) {

            // Gets all of the users.
            UserService.selectAllInto(User.users);

            // Creates an array to store a leaderboard in.
            JSONArray leaderboardList = new JSONArray();

            // Looks at every user in the leaderboard.
            for (Leaderboard l : Leaderboard.leaderboards) {

                // Converts the leaderboard record into a JSON object.
                JSONObject jl = l.toJSON();

                // Looks for matching users.
                for (User u : User.users) {

                    // Returns the username of the matching user.
                    if (u.getUserID() == l.getUserID()) {
                        jl.put("username", u.getUsername());
                        break;
                    }
                }

                leaderboardList.add(jl);
            }

            // Converts the leaderboard back from the JSON form.
            return leaderboardList.toString();
        }
        else {
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }

}
