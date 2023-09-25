package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
