package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.link.*;
import io.shelang.aghab.service.link.LinksService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/api/v1/links")
@RequestScoped
@RolesAllowed({ Roles.BOSS, Roles.USER, Roles.API })
@Slf4j
public class LinksResource {

  @Inject
  LinksService linksService;

  @Inject
  io.shelang.aghab.service.audit.AuditService auditService;
  @Inject
  io.shelang.aghab.service.user.TokenService tokenService;
  @jakarta.ws.rs.core.Context
  io.vertx.core.http.HttpServerRequest request;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinksDTO search(
      @QueryParam("q") String q,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return linksService.get(q, page, size);
  }

  @GET
  @Path("/workspaces/{workspaceId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinksDTO getByWorkspace(@QueryParam("q") String q, @QueryParam("page") Integer page,
      @QueryParam("size") Integer size, @PathParam("workspaceId") Long workspaceId) {
    return this.linksService.getByWorkspace(q, workspaceId, page, size);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO getById(@PathParam("id") Long id) {
    return linksService.getById(id);
  }

  @GET
  @Path("/hash/{hash}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO getByHash(@PathParam("hash") String hash) {
    return linksService.getByHash(hash);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO create(@HeaderParam("Host") String host, @HeaderParam("Origin") String origin,
      @Valid LinkCreateDTO link) {
    link.setHost(host);
    link.setOrigin(origin);
    LinkDTO dto = linksService.create(link);
    auditService.log(tokenService.getAccessTokenUserId(), link.getWorkspaceId(), "LINK_CREATED",
        "Link ID: " + dto.getId(), request.remoteAddress().host());
    return dto;
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO update(@PathParam("id") Long id, @Valid LinkCreateDTO link) {
    LinkDTO dto = linksService.update(id, link);
    auditService.log(tokenService.getAccessTokenUserId(), link.getWorkspaceId(), "LINK_UPDATED",
        "Link ID: " + id, request.remoteAddress().host());
    return dto;
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") Long id) {
    LinkDTO link = linksService.getById(id);
    linksService.delete(link.getId());
    auditService.log(tokenService.getAccessTokenUserId(), null, "LINK_DELETED", "Link ID: " + link.getId(),
        request.remoteAddress().host());
    return Response.noContent().build();
  }

  @GET
  @Authenticated
  @Path("/alt/types")
  @Produces(MediaType.APPLICATION_JSON)
  public LinkAlternativeTypesDTO getLinkAlternativeTypes() {
    return linksService.getLinkAlternativeTypes();
  }
}
