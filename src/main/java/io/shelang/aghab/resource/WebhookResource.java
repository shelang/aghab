package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.dto.webhook.WebhookDTO;
import io.shelang.aghab.service.dto.webhook.WebhooksDTO;
import io.shelang.aghab.service.webhook.WebhookService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

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
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  public WebhookDTO getById(@PathParam("id") Long id) {
    return webhookService.getById(id);
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
  @SuppressWarnings("PathAnnotation")
  @Path("/{id}")
  public WebhookDTO update(@PathParam("id") Long id, @Valid WebhookDTO dto) {
    dto.setId(id);
    return webhookService.update(dto);
  }
}
