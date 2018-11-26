package server.controllers;

import server.models.User;

        import javax.ws.rs.*;
        import javax.ws.rs.core.MediaType;

@Path("user/")
public class UserController {

    //Defines the HTTP request as post.
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)

    //Inputs the parameters of the form and then uses then outputs them again.
    public String loginHandler(@FormParam("loginUsername") String loginUsername,
                               @FormParam("loginPassword") String loginPassword) {
        return "Login details are: " + loginUsername + " " + loginPassword;
    }
}