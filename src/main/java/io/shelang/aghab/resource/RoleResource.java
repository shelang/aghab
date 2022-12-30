package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/v1/roles")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER})
public class RoleResource {

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllRoles() {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

}
