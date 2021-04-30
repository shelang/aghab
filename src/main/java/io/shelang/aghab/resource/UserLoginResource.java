package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.dto.LoginRequestDTO;
import io.shelang.aghab.service.user.UserLoginService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/login")
@RequestScoped
public class UserLoginResource {

  @Inject UserLoginService userLoginService;

  @POST
  @PermitAll
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginDTO login(@Valid LoginRequestDTO request) {
    return userLoginService.login(request.getUsername(), request.getPassword());
  }

  @Path("/refresh")
  @POST
  @Authenticated
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginDTO refresh(@HeaderParam("Authorization") String authorization) {
    return userLoginService.refresh(authorization);
  }
}
