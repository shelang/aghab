package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticDTO;
import io.shelang.aghab.service.dto.AnalyticRequestDTO;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/analytics")
@RequestScoped
@RolesAllowed({Roles.BOSS, Roles.USER})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalyticResource {

  final AnalyticService analyticService;

  public AnalyticResource(AnalyticService analyticService) {
    this.analyticService = analyticService;
  }

  @GET
  @Path("/{linkId}")
  public AnalyticDTO get(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to,
      @QueryParam("bucket") String bucket) {
    var request = new AnalyticRequestDTO().setFrom(from).setTo(to).setBucket(bucket);
    return analyticService.getAndCount(Long.valueOf(linkId), request);
  }

  @GET
  @Path("/ua/")
  public Object parse(@HeaderParam("User-Agent") String userAgent) {
    return UserAgentAnalyzer.parse(userAgent).toJson();
  }
}
