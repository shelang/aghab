package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.auth.UserCredentialDTO;
import io.shelang.aghab.service.dto.auth.UserDTO;
import io.shelang.aghab.service.dto.auth.UserMeDTO;
import io.shelang.aghab.service.dto.auth.UsersDTO;
import io.shelang.aghab.service.user.UserService;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

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
  @RolesAllowed({Roles.BOSS, Roles.USER})
  public UserMeDTO getMe() {
    return userService.getMe();
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{userId}")
  public UserDTO getById(@PathParam("userId") Long userId) {
    return userService.getById(userId);
  }

  @GET
  @Path("/")
  public UsersDTO search(
      @QueryParam("username") String username,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return userService.get(username, page, size);
  }

  @POST
  @Path("/")
  public UserDTO create(UserCredentialDTO userCredential) {
    return userService.create(userCredential);
  }

  @PUT
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  @RolesAllowed({Roles.BOSS, Roles.USER})
  public UserDTO update(@PathParam("id") Long id, UserCredentialDTO userCredential) {
    return userService.update(userCredential);
  }

  @POST
  @Path(("/api-token"))
  @RolesAllowed({Roles.BOSS, Roles.USER})
  public UserMeDTO generateAPIToken() {
    return userService.generateAPIToken();
  }
}
