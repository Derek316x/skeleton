//defines that this class is part of the package controllers
package controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/netid")
@Produces(MediaType.TEXT_PLAIN)
public class NetIDController {

    public NetIDController () {

    }

    @GET
    public String getNetID() {
        return "dfn22";
    }

}