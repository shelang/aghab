package io.shelang.aghab.resource;

import io.shelang.aghab.role.Roles;
import io.shelang.aghab.service.analytic.AnalyticService;
import io.shelang.aghab.service.dto.analytic.AnalyticDTO;
import io.shelang.aghab.service.dto.analytic.AnalyticListDTO;
import io.shelang.aghab.service.dto.analytic.AnalyticRequestDTO;
import io.shelang.aghab.service.dto.analytic.AnalyticTimeRangeRequestDTO;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
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
  public AnalyticDTO getAnalyticByLinkId(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to,
      @QueryParam("bucket") String bucket) {
    var request = new AnalyticRequestDTO().setFrom(from).setTo(to).setBucket(bucket);
    return analyticService.getAndCount(Long.valueOf(linkId), request);
  }

  @GET
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
  public AnalyticListDTO topDevicesByLinkId(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5Devices(Long.valueOf(linkId), request);
  }

  @GET
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
  public AnalyticListDTO top5OsesByLinkId(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5Oses(Long.valueOf(linkId), request);
  }

  @GET
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
  public AnalyticListDTO top5AgentNamesByLinkId(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5AgentNames(Long.valueOf(linkId), request);
  }

  @GET
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
  public AnalyticListDTO top5DeviceBrandsByLinkId(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5DeviceBrands(Long.valueOf(linkId), request);
  }

  @GET
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
  public AnalyticListDTO top5DeviceNamesByLinkId(
      @PathParam("linkId") String linkId,
      @QueryParam("from") String from,
      @QueryParam("to") String to) {
    var request = new AnalyticTimeRangeRequestDTO().setFrom(from).setTo(to);
    return analyticService.top5DeviceNames(Long.valueOf(linkId), request);
  }

  @GET
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
