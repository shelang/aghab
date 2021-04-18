package io.shelang.aghab.path;

import io.shelang.aghab.model.LinkCreateDTO;
import io.shelang.aghab.model.LinkDTO;
import io.shelang.aghab.service.link.LinksService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/links")
public class LinksPath {

  @Inject LinksService linksService;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinkDTO get(@PathParam Long id) {
    return linksService.getById(id);
  }

  @GET
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
}
