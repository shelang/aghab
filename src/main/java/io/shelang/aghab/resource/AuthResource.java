package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.auth.LoginDTO;
import io.shelang.aghab.service.dto.auth.LoginRequestDTO;
import io.shelang.aghab.service.user.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/login")
@RequestScoped
public class AuthResource {

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
