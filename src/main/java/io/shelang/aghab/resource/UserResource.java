package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.UserCredentialDTO;
import io.shelang.aghab.service.dto.UserDTO;
import io.shelang.aghab.service.dto.UserMeDTO;
import io.shelang.aghab.service.dto.UsersDTO;
import io.shelang.aghab.service.user.UserService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/users")
@RequestScoped
@RolesAllowed({Roles.BOSS})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

  final UserService userService;

  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @GET
  @Path("/me")
  public UserMeDTO getMe() {
    return userService.getMe();
  }

  @GET
  @Path("/{linkId}")
  public UserDTO get(
      @PathParam("id") long id,
      @QueryParam("from") String from,
      @QueryParam("to") String to,
      @QueryParam("bucket") String bucket) {
    return userService.getById(id);
  }

  @GET
  @Path("/")
  public UsersDTO get(
      @PathParam("username") String username,
      @QueryParam("from") Integer page,
      @QueryParam("to") Integer size) {
    return userService.get(username, page, size);
  }

  @POST
  @Path("/")
  public UserDTO create(UserCredentialDTO userCredential) {
    return userService.create(userCredential);
  }

  @PUT
  @Path("/{id}")
  @RolesAllowed({Roles.BOSS, Roles.USER})
  public UserDTO update(@PathParam("id") long id, UserCredentialDTO userCredential) {
    return userService.update(userCredential);
  }

  @POST
  @Path(("/api-token"))
  @RolesAllowed({Roles.BOSS, Roles.USER})
  public UserMeDTO generateAPIToken() {
    return userService.generateAPIToken();
  }
}