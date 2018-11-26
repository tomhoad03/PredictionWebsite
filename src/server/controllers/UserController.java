package server.controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("user/")
public class UserController {
    @POST
    @Path("get")
    @Produces(MediaType.TEXT_PLAIN)
    public String loginHandler(@FormParam("loginUsername") String loginUsername,
                               @FormParam("loginPassword") String loginPassword) {
        return loginUsername + loginPassword;
    }
}
