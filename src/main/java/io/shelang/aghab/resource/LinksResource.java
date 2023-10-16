package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.link.*;
import io.shelang.aghab.service.link.LinksService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

@Path("/api/v1/links")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER, Roles.API})
public class LinksResource {

  @Inject
  LinksService linksService;

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
  @Produces({"application/json"})
  @Consumes({"application/json"})
  public LinksDTO getByWorkspace(@QueryParam("q") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size, @PathParam Long workspaceId) {
    return this.linksService.getByWorkspace(q, workspaceId, page, size);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO getById(@PathParam Long id) {
    return linksService.getById(id);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/hash/{hash}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO getByHash(@PathParam String hash) {
    return linksService.getByHash(hash);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO create(@Valid LinkCreateDTO links) {
    return linksService.create(links);
  }

  @PUT
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO update(@PathParam Long id, @Valid LinkCreateDTO links) {
    return linksService.update(id, links);
  }

  @DELETE
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam Long id) {
    linksService.delete(id);
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
