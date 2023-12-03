package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.webhook.WebhookDTO;
import io.shelang.aghab.service.dto.webhook.WebhooksDTO;
import io.shelang.aghab.service.webhook.WebhookService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/webhook")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WebhookResource {

  final WebhookService webhookService;

  public WebhookResource(WebhookService webhookService) {
    this.webhookService = webhookService;
  }

  @GET
  @Path("/{id}")
  public WebhookDTO getById(@PathParam("id") Long id) {
    return webhookService.getByIdAndValidate(id);
  }

  @GET
  @Path("/")
  public WebhooksDTO search(
      @QueryParam("name") String name,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return new WebhooksDTO().setWebhooks(webhookService.search(name, page, size));
  }

  @POST
  @Path("/")
  public WebhookDTO create(@Valid WebhookDTO dto) {
    return webhookService.create(dto);
  }

  @PUT
  @Path("/{id}")
  public WebhookDTO update(@PathParam("id") Long id, @Valid WebhookDTO dto) {
    dto.setId(id);
    return webhookService.update(dto);
  }
}
