package server.controllers;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("html/")
public class UserController {
    @POST
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    public String loginHandler(@FormParam("loginUsername") String loginUsername,
                               @FormParam("loginPassword") String loginPassword) {
        return "You have logged in!";
    }
}
