package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.auth.UserCredentialDTO;
import io.shelang.aghab.service.dto.auth.UserDTO;
import io.shelang.aghab.service.dto.auth.UserMeDTO;
import io.shelang.aghab.service.dto.auth.UsersDTO;
import io.shelang.aghab.service.dto.workspace.UserWorkspaceRequest;
import io.shelang.aghab.service.dto.workspace.WorkspacesDTO;
import io.shelang.aghab.service.user.UserService;
import io.shelang.aghab.service.workspace.WorkspaceService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/api/v1/users")
@RequestScoped
@RequiredArgsConstructor
@RolesAllowed({Roles.BOSS})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

  final UserService userService;
  final WorkspaceService workspaceService;

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

  @GET
  @Path("/workspaces")
  @RolesAllowed({Roles.BOSS, Roles.USER})
  public WorkspacesDTO getUserWorkspaces(
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return workspaceService.getUserWorkspaces(page, size);
  }

  @GET
  @Path("/{id}/workspaces")
  @RolesAllowed({Roles.BOSS})
  public WorkspacesDTO getWorkspacesOfUserById(
      @PathParam("id") Long id,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return workspaceService.getUserWorkspaces(id, page, size);
  }

  @POST
  @Path("/workspaces")
  @RolesAllowed({Roles.BOSS})
  public Response addUserWorkspaces(UserWorkspaceRequest request) {
    workspaceService.addUserWorkspaces(request);
    return Response.noContent().build();
  }

  @DELETE
  @Path("/workspaces")
  @RolesAllowed({Roles.BOSS})
  public Response deleteUserWorkspaces(UserWorkspaceRequest request) {
    workspaceService.deleteUserWorkspaces(request);
    return Response.noContent().build();
  }
}
