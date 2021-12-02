package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.webhook.WebhookService;
import io.shelang.aghab.service.webhook.dto.WebhookDTO;
import io.shelang.aghab.service.webhook.dto.WebhooksDTO;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
  public WebhookDTO get(@PathParam("id") Long id) {
    return webhookService.getById(id);
  }

  @GET
  @Path("/")
  public WebhooksDTO get(
      @QueryParam("name") String name,
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size) {
    return new WebhooksDTO().setWebhooks(webhookService.get(name, page, size));
  }

  @POST
  @Path("/")
  public WebhookDTO create(@Valid WebhookDTO dto) {
    return webhookService.create(dto);
  }

  @PUT
  @Path("/{id}")
  public WebhookDTO get(@PathParam("id") Long id, @Valid WebhookDTO dto) {
    return webhookService.update(id, dto);
  }
}
