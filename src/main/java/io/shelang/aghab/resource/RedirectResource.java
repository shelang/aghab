package io.shelang.aghab.resource;

import io.quarkus.qute.Location;
import io.quarkus.qute.RawString;
import io.quarkus.qute.Template;
import io.quarkus.vertx.web.Route;
import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.service.dto.RedirectDTO;
import io.shelang.aghab.service.redirect.RedirectService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.java.Log;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Objects;

@PermitAll
@RequestScoped
@Log
public class RedirectResource {

  @Inject RedirectService redirectService;

  @Inject
  @Location("hello.html")
  Template helloTemplate;

  @Inject
  @Location("iframe.html")
  Template iframeTemplate;

  @Inject
  @Location("script.html")
  Template scriptTemplate;

  @Route(path = "/r/:hash", methods = HttpMethod.GET)
  public Uni<String> redirect(RoutingContext rc) {
    return redirectService
        .redirectBy(rc)
        .onFailure()
        .recoverWithItem(() -> new RedirectDTO().setStatusCode((short) 404).setUrl(""))
        .onItem()
        .transformToUni(
            byHash -> {
              log.info(byHash.toString());
              rc.response().setStatusCode(byHash.getStatusCode());
              if (RedirectType.IFRAME.equals(byHash.getType())) {
                return Uni.createFrom()
                    .completionStage(
                        () -> iframeTemplate.data("url", byHash.getUrl()).renderAsync());
              } else if (RedirectType.SCRIPT.equals(byHash.getType())) {
                return Uni.createFrom()
                    .completionStage(
                        () ->
                            scriptTemplate
                                .data("url", byHash.getUrl())
                                .data("title", byHash.getTitle())
                                .data(
                                    "timeoutInMillis",
                                    Objects.nonNull(byHash.getTimeout())
                                        ? byHash.getTimeout()
                                        : 10000)
                                .data("script", new RawString(byHash.getContent()))
                                .renderAsync());
              } else { // Simple redirect
                rc.response().putHeader(HttpHeaders.LOCATION, byHash.getUrl());
                return Uni.createFrom().item("");
              }
            });
  }

  @Route(path = "/s/:name", methods = HttpMethod.GET)
  public Uni<String> rScript(RoutingContext rc) {
    return Uni.createFrom()
        .completionStage(
            () ->
                helloTemplate
                    .data("name", rc.request().getParam("name"))
                    .data("script", new RawString(""))
                    .renderAsync());
  }
}
