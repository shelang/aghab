package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.dto.LoginRequestDTO;
import io.shelang.aghab.service.user.AuthService;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/login")
@RequestScoped
public class UserLoginResource {

  @Inject
  AuthService authService;

  @POST
  @PermitAll
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginDTO login(@Valid LoginRequestDTO request) {
    return authService.login(request.getUsername(), request.getPassword());
  }

  @Path("/refresh")
  @POST
  @RolesAllowed({Roles.REFRESH_TOKEN})
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginDTO refresh(@HeaderParam("Authorization") String authorization) {
    return authService.refresh(authorization);
  }
}
