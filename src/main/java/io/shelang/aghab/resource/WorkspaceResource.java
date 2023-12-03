package io.shelang.aghab.resource;


import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.auth.UsersDTO;
import io.shelang.aghab.service.dto.workspace.MembersRequest;
import io.shelang.aghab.service.dto.workspace.WorkspaceDTO;
import io.shelang.aghab.service.dto.workspace.WorkspacesDTO;
import io.shelang.aghab.service.workspace.WorkspaceService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/api/v1/workspaces")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER})
@RequiredArgsConstructor
public class WorkspaceResource {

  final WorkspaceService workspaceService;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public WorkspacesDTO getWorkspaces(
      @QueryParam("q") String q,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return workspaceService.search(q, page, size);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public WorkspaceDTO getWorkspace(@PathParam("id") Long id) {
    return workspaceService.getWorkspace(id);
  }

  @POST
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public WorkspaceDTO createWorkspace(WorkspaceDTO request) {
    return workspaceService.create(request);
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public WorkspaceDTO updateWorkspace(@PathParam("id") Long id, WorkspaceDTO request) {
    request.setId(id);
    return workspaceService.update(request);
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteWorkspace(@PathParam("id") Long id) {
    workspaceService.delete(id);
    return Response.noContent().build();
  }

  @GET
  @Path("/{id}/members")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public UsersDTO getMembers(
      @PathParam("id") Long id,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return workspaceService.listMember(id, page, size);
  }

  @POST
  @Path("/{id}/members")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addMembers(@PathParam("id") Long id, MembersRequest request) {
    workspaceService.addMember(id, request);
    return Response.noContent().build();
  }

  @DELETE
  @Path("/{id}/members")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteMembers(@PathParam("id") Long id, MembersRequest request) {
    workspaceService.deleteMember(id, request);
    return Response.noContent().build();
  }

}
