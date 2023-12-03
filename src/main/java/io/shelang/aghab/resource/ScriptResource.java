package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.script.ScriptDTO;
import io.shelang.aghab.service.dto.script.ScriptsDTO;
import io.shelang.aghab.service.script.ScriptService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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
  public ScriptDTO getById(@PathParam("id") Long id) {
    return scriptService.getByIdAndValidation(id);
  }

  @GET
  @Path("/")
  public ScriptsDTO search(
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
  public ScriptDTO update(@PathParam("id") Long id, @Valid ScriptDTO dto) {
    dto.setId(id);
    return scriptService.update(dto);
  }
}
