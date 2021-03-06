package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.service.dto.LinkCreateDTO;
import io.shelang.aghab.service.dto.LinkDTO;
import io.shelang.aghab.service.dto.LinksUserDTO;
import io.shelang.aghab.service.link.LinksService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

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
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LinksUserDTO getList(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
    return linksService.get(page, size);
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

  @DELETE
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam Long id) {
    linksService.delete(id);
    return Response.noContent().build();
  }
}
