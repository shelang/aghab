package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.AnalyticDTO;
import io.shelang.aghab.service.dto.AnalyticListDTO;
import io.shelang.aghab.service.dto.AnalyticRequestDTO;
import io.shelang.aghab.service.dto.AnalyticTimeRangeRequestDTO;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

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
  @SuppressWarnings("PathAnnotation")
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
  @SuppressWarnings("PathAnnotation")
  @Path("/top/devices")
  public AnalyticListDTO topDevices(
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5Devices(null, request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{linkId}/top/devices")
  public AnalyticListDTO topDevices(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5Devices(Long.valueOf(linkId), request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/top/oses")
  public AnalyticListDTO top5Oses(
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5Oses(null, request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{linkId}/top/oses")
  public AnalyticListDTO top5Oses(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5Oses(Long.valueOf(linkId), request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/top/agent-names")
  public AnalyticListDTO top5AgentNames(
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5AgentNames(null, request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{linkId}/top/agent-names")
  public AnalyticListDTO top5AgentNames(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5AgentNames(Long.valueOf(linkId), request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/top/device-brands")
  public AnalyticListDTO top5DeviceBrands(
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5DeviceBrands(null, request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{linkId}/top/device-brands")
  public AnalyticListDTO top5DeviceBrands(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5DeviceBrands(Long.valueOf(linkId), request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/top/device-names")
  public AnalyticListDTO top5DeviceNames(
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5DeviceNames(null, request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/{linkId}/top/device-names")
  public AnalyticListDTO top5DeviceNames(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5DeviceNames(Long.valueOf(linkId), request);
  }

  @GET
  @SuppressWarnings("PathAnnotation")
  @Path("/last/ips")
  public AnalyticListDTO last10IPs(
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.getLast10UniqIP(request);
  }

  @GET
  @Path("/ua/")
  public Object parse(@HeaderParam("User-Agent") String userAgent) {
    return UserAgentAnalyzer.parse(userAgent).toJson();
  }
}
