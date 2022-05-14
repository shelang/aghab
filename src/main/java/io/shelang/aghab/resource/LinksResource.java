package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.LinkAlternativeTypesDTO;
import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinkDTO;
import io.shelang.aghab.service.dto.LinksUserDTO;
import io.shelang.aghab.service.link.LinksService;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
  public LinksUserDTO getList(
      @QueryParam("q") String q,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return linksService.get(q, page, size);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO get(@PathParam Long id) {
    return linksService.getById(id);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/hash/{hash}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO get(@PathParam String hash) {
    return linksService.getByHash(hash);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO create(@Valid LinkCreateDTO links) {
    return linksService.create(links);
  }

  @PUT
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
