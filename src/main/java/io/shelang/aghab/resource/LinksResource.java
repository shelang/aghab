package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinksDTO;
import io.shelang.aghab.service.link.LinksService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/links")
@RequestScoped
@Authenticated
public class LinksResource {

  @Inject LinksService linksService;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinksDTO get(@PathParam Long id) {
    return linksService.getById(id);
  }

  @GET
  @Path("/hash/{hash}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinksDTO get(@PathParam String hash) {
    return linksService.getByHash(hash);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinksDTO create(@Valid LinkCreateDTO links) {
    return linksService.create(links);
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam Long id) {
    linksService.delete(id);
    return Response.noContent().build();
  }
}
