package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.script.ScriptService;
import io.shelang.aghab.service.dto.ScriptDTO;
import io.shelang.aghab.service.dto.ScriptsDTO;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/script")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScriptResource {

  final ScriptService scriptService;

  public ScriptResource(ScriptService scriptService) {
    this.scriptService = scriptService;
  }

  @GET
  @Path("/{id}")
  public ScriptDTO get(@PathParam("id") Long id) {
    return scriptService.getById(id);
  }

  @GET
  @Path("/")
  public ScriptsDTO get(
      @QueryParam("name") String name,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return new ScriptsDTO().setScripts(scriptService.get(name, page, size));
  }

  @POST
  @Path("/")
  public ScriptDTO create(@Valid ScriptDTO dto) {
    return scriptService.create(dto);
  }

  @PUT
  @Path("/{id}")
  public ScriptDTO get(@QueryParam("id") Long id, @Valid ScriptDTO dto) {
    return scriptService.update(dto);
  }
}
