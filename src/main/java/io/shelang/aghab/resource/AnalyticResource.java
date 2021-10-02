package io.shelang.aghab.resource;

import io.quarkus.security.Authenticated;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticDTO;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/analytics")
@RequestScoped
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalyticResource {

  final AnalyticService analyticService;

  public AnalyticResource(AnalyticService analyticService) {
    this.analyticService = analyticService;
  }

  @GET
  @Path("/{linkId}")
  public AnalyticDTO get(@PathParam Long linkId, @QueryParam("from") String from, @QueryParam("to") String to) {
    return analyticService.getAndCount(linkId, from, to);
  }
}
