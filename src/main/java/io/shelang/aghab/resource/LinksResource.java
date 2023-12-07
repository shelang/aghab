package io.shelang.aghab.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.Authenticated;
import io.shelang.aghab.exception.ValidationException;
import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.link.*;
import io.shelang.aghab.service.link.LinksService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Path("/api/v1/links")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER, Roles.API})
@Slf4j
public class LinksResource {

  @Inject
  LinksService linksService;

  @Inject
  Validator validator;

  @Inject
  ObjectMapper objectMapper;

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
  public LinksDTO getByWorkspace(@QueryParam("q") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size, @PathParam("workspaceId") Long workspaceId) {
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
  // FIXME: The validation and input body does not work!
  public LinkDTO create(String body) throws JsonProcessingException {
    LinkCreateDTO link = objectMapper.readValue(body, LinkCreateDTO.class);
    Set<ConstraintViolation<LinkCreateDTO>> validate = validator.validate(link);
    if (validate.isEmpty()) {
      return linksService.create(link);
    } else {
      throw new ValidationException(validate);
    }
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  // FIXME: The validation and input body does not work!
  public LinkDTO update(@PathParam("id") Long id, String body) throws JsonProcessingException {
    LinkCreateDTO link = objectMapper.readValue(body, LinkCreateDTO.class);
    Set<ConstraintViolation<LinkCreateDTO>> validate = validator.validate(link);
    if (validate.isEmpty()) {
      return linksService.update(id, link);
    } else {
      throw new ValidationException(validate);
    }
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") Long id) {
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
